import { describe, it, expect, beforeEach, jest } from '@jest/globals';
import axios from 'axios';
import { HttpClient } from './http-client';
import { ZsxqConfig } from '../client/zsxq-config';
import {
  ZsxqException,
  NetworkException,
  TimeoutException,
  TokenInvalidException,
} from '../exception';

jest.mock('axios');

describe('HttpClient', () => {
  let httpClient: HttpClient;
  let config: ZsxqConfig;
  let mockAxios: any;

  beforeEach(() => {
    config = {
      token: 'test-token',
      baseUrl: 'https://api.zsxq.com',
      timeout: 10000,
      retryCount: 2,
      retryDelay: 0, // 设置为0以加快测试
      appVersion: '5.54.4',
      deviceId: 'test-device-id',
    };

    mockAxios = {
      get: jest.fn(),
      post: jest.fn(),
      put: jest.fn(),
      delete: jest.fn(),
    };

    (axios.create as any).mockReturnValue(mockAxios);
    httpClient = new HttpClient(config);
  });

  describe('GET 请求', () => {
    it('应该成功发送 GET 请求', async () => {
      const mockData = { id: 1, name: 'Test Group' };
      mockAxios.get.mockResolvedValue({
        data: {
          succeeded: true,
          resp_data: { groups: [mockData] },
        },
      });

      const result = await httpClient.get<{ groups: any[] }>('/v2/groups');

      expect(result).toEqual({ groups: [mockData] });
      expect(mockAxios.get).toHaveBeenCalledWith(
        '/v2/groups',
        expect.objectContaining({
          params: undefined,
          headers: expect.objectContaining({
            authorization: 'test-token',
          }),
        }),
      );
    });

    it('应该传递查询参数', async () => {
      mockAxios.get.mockResolvedValue({
        data: {
          succeeded: true,
          resp_data: { topics: [] },
        },
      });

      await httpClient.get('/v2/topics', { count: 10, scope: 'all' });

      expect(mockAxios.get).toHaveBeenCalledWith(
        '/v2/topics',
        expect.objectContaining({
          params: { count: 10, scope: 'all' },
        }),
      );
    });

    it('应该包含所有必需的请求头', async () => {
      mockAxios.get.mockResolvedValue({
        data: { succeeded: true, resp_data: {} },
      });

      await httpClient.get('/v2/test');

      const callArgs = mockAxios.get.mock.calls[0][1];
      expect(callArgs.headers).toMatchObject({
        authorization: 'test-token',
        'x-aduid': 'test-device-id',
      });
      expect(callArgs.headers['x-timestamp']).toBeDefined();
      expect(callArgs.headers['x-signature']).toBeDefined();
      expect(callArgs.headers['x-request-id']).toBeDefined();
    });
  });

  describe('POST 请求', () => {
    it('应该成功发送 POST 请求', async () => {
      const postData = { content: 'Test content' };
      mockAxios.post.mockResolvedValue({
        data: {
          succeeded: true,
          resp_data: { topic: { id: 1 } },
        },
      });

      const result = await httpClient.post<{ topic: any }>('/v2/topics', postData);

      expect(result).toEqual({ topic: { id: 1 } });
      expect(mockAxios.post).toHaveBeenCalledWith('/v2/topics', postData, expect.any(Object));
    });

    it('应该生成请求签名', async () => {
      mockAxios.post.mockResolvedValue({
        data: { succeeded: true, resp_data: {} },
      });

      await httpClient.post('/v2/test', { data: 'test' });

      const callArgs = mockAxios.post.mock.calls[0][2];
      expect(callArgs.headers['x-signature']).toMatch(/^[a-f0-9]{40}$/); // HMAC-SHA1 hex
    });
  });

  describe('PUT 请求', () => {
    it('应该成功发送 PUT 请求', async () => {
      const putData = { status: 'updated' };
      mockAxios.put.mockResolvedValue({
        data: {
          succeeded: true,
          resp_data: { topic: { id: 1, status: 'updated' } },
        },
      });

      const result = await httpClient.put<{ topic: any }>('/v2/topics/1', putData);

      expect(result).toEqual({ topic: { id: 1, status: 'updated' } });
      expect(mockAxios.put).toHaveBeenCalledWith('/v2/topics/1', putData, expect.any(Object));
    });
  });

  describe('DELETE 请求', () => {
    it('应该成功发送 DELETE 请求', async () => {
      mockAxios.delete.mockResolvedValue({
        data: { succeeded: true, resp_data: {} },
      });

      await httpClient.delete('/v2/topics/1');

      expect(mockAxios.delete).toHaveBeenCalledWith('/v2/topics/1', expect.any(Object));
    });
  });

  describe('错误处理', () => {
    it('应该处理 API 业务错误', async () => {
      mockAxios.get.mockRejectedValue({
        response: {
          status: 401,
          data: {
            succeeded: false,
            code: 10001,
            error: 'Token 无效',
          },
        },
        message: 'Request failed with status code 401',
      });

      await expect(httpClient.get('/v2/test')).rejects.toMatchObject({
        code: 10001,
        name: 'TokenInvalidException',
        message: 'Token 无效',
      });
    });

    it('应该处理网络超时', async () => {
      mockAxios.get.mockRejectedValue({
        code: 'ECONNABORTED',
        message: 'timeout of 10000ms exceeded',
      });

      await expect(httpClient.get('/v2/test')).rejects.toMatchObject({
        name: 'TimeoutException',
        message: 'timeout of 10000ms exceeded',
      });
    });

    it('应该处理网络错误', async () => {
      mockAxios.get.mockRejectedValue({
        message: 'Network Error',
        request: {},
      });

      await expect(httpClient.get('/v2/test')).rejects.toMatchObject({
        name: 'NetworkException',
        message: 'Network Error',
      });
    });

    it('应该处理服务器 HTTP 错误', async () => {
      mockAxios.get.mockRejectedValue({
        response: {
          status: 500,
          data: 'Internal Server Error',
        },
        message: 'Request failed with status code 500',
      });

      await expect(httpClient.get('/v2/test')).rejects.toMatchObject({
        name: 'ZsxqException',
        code: 500,
      });
    });
  });

  describe('重试机制', () => {
    it('应该在网络错误时重试并最终成功', async () => {
      let callCount = 0;
      mockAxios.get.mockImplementation(() => {
        callCount++;
        if (callCount < 3) {
          const error: any = {
            message: 'Network Error',
            request: {},
          };
          return Promise.reject(error);
        }
        return Promise.resolve({
          data: { succeeded: true, resp_data: { result: 'success' } },
        });
      });

      const result = await httpClient.get('/v2/test');

      expect(result).toEqual({ result: 'success' });
      expect(callCount).toBe(3);
    });

    it('应该在超时时重试并最终成功', async () => {
      let callCount = 0;
      mockAxios.get.mockImplementation(() => {
        callCount++;
        if (callCount === 1) {
          const error: any = {
            code: 'ECONNABORTED',
            message: 'timeout',
          };
          return Promise.reject(error);
        }
        return Promise.resolve({
          data: { succeeded: true, resp_data: { result: 'success' } },
        });
      });

      await httpClient.get('/v2/test');

      expect(callCount).toBe(2);
    });

    it('应该在达到最大重试次数后抛出错误', async () => {
      let callCount = 0;
      mockAxios.get.mockImplementation(() => {
        callCount++;
        const error: any = {
          message: 'Network Error',
          request: {},
        };
        return Promise.reject(error);
      });

      try {
        await httpClient.get('/v2/test');
        fail('应该抛出错误');
      } catch (error: any) {
        expect(error.name).toBe('NetworkException');
        expect(callCount).toBe(3); // 初始请求 + 2次重试
      }
    });

    it('不应该对业务错误进行重试', async () => {
      let callCount = 0;
      mockAxios.get.mockImplementation(() => {
        callCount++;
        const error: any = {
          response: {
            status: 401,
            data: {
              succeeded: false,
              code: 10001,
              error: 'Token 无效',
            },
          },
        };
        return Promise.reject(error);
      });

      try {
        await httpClient.get('/v2/test');
        fail('应该抛出错误');
      } catch (error: any) {
        expect(error.code).toBe(10001);
        expect(callCount).toBe(1); // 不重试
      }
    });
  });

  describe('响应处理', () => {
    it('应该处理成功响应', async () => {
      mockAxios.get.mockResolvedValue({
        data: {
          succeeded: true,
          resp_data: { value: 'test' },
        },
      });

      const result = await httpClient.get<{ value: string }>('/v2/test');

      expect(result).toEqual({ value: 'test' });
    });

    it('应该处理失败响应（有 code）', async () => {
      mockAxios.get.mockResolvedValue({
        data: {
          succeeded: false,
          code: 10001,
          error: 'Token 无效',
        },
      });

      await expect(httpClient.get('/v2/test')).rejects.toThrow(TokenInvalidException);
    });

    it('应该处理失败响应（有 info）', async () => {
      mockAxios.get.mockResolvedValue({
        data: {
          succeeded: false,
          code: 99999,
          info: '未知错误',
        },
      });

      await expect(httpClient.get('/v2/test')).rejects.toThrow(ZsxqException);
    });
  });
});
