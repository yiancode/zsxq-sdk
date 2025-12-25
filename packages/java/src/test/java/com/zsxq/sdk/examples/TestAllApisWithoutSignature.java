package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.model.Group;
import com.zsxq.sdk.request.CheckinsRequest.CreateCheckinParams;
import com.zsxq.sdk.request.CheckinsRequest.UpdateCheckinParams;
import com.zsxq.sdk.request.CheckinsRequest.ListCheckinsOptions;

import java.util.List;

/**
 * 测试禁用签名后，所有接口是否正常工作
 */
public class TestAllApisWithoutSignature {
    public static void main(String[] args) {
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.exit(1);
        }

        try {
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    .disableSignature()  // 禁用签名
                    .build();

            System.out.println("=== 测试禁用签名后各接口的表现 ===");
            System.out.println("配置：禁用签名");
            System.out.println("========================================\n");

            int successCount = 0;
            int failCount = 0;

            // 测试 1: 查询星球信息 (GET)
            System.out.println("【测试 1/6】查询星球信息 (GET)...");
            try {
                Group group = client.groups().get(groupId);
                if (group != null) {
                    System.out.println("  ✅ 成功: " + group.getName());
                    successCount++;
                } else {
                    System.out.println("  ⚠️  返回 null");
                    failCount++;
                }
            } catch (Exception e) {
                System.out.println("  ❌ 失败: " + e.getMessage());
                failCount++;
            }

            // 测试 2: 查询训练营列表 (GET)
            System.out.println("\n【测试 2/6】查询训练营列表 (GET)...");
            try {
                List<Checkin> checkins = client.checkins().list(
                        groupId,
                        new ListCheckinsOptions().scope("all").count(5)
                );
                System.out.println("  ✅ 成功: 找到 " + checkins.size() + " 个训练营");
                successCount++;
            } catch (Exception e) {
                System.out.println("  ❌ 失败: " + e.getMessage());
                failCount++;
            }

            // 测试 3: 查询单个训练营 (GET)
            System.out.println("\n【测试 3/6】查询单个训练营 (GET)...");
            try {
                // 先获取一个训练营ID
                List<Checkin> allCheckins = client.checkins().list(groupId);
                if (!allCheckins.isEmpty()) {
                    String checkinId = String.valueOf(allCheckins.get(0).getCheckinId());
                    Checkin checkin = client.checkins().get(groupId, checkinId);
                    if (checkin != null) {
                        System.out.println("  ✅ 成功: " + checkin.getTitle());
                        successCount++;
                    } else {
                        System.out.println("  ⚠️  返回 null");
                        failCount++;
                    }
                } else {
                    System.out.println("  ⚠️  跳过（没有训练营）");
                }
            } catch (Exception e) {
                System.out.println("  ❌ 失败: " + e.getMessage());
                failCount++;
            }

            // 测试 4: 创建训练营 (POST)
            System.out.println("\n【测试 4/6】创建训练营 (POST)...");
            String newCheckinId = null;
            try {
                CreateCheckinParams createParams = new CreateCheckinParams()
                        .title("签名测试-" + System.currentTimeMillis())
                        .text("测试禁用签名后创建是否正常")
                        .checkinDays(7)
                        .type("accumulated")
                        .showTopicsOnTimeline(false)
                        .expirationTime("2026-01-15T23:59:59.000+0800");

                Checkin newCheckin = client.checkins().create(groupId, createParams);
                if (newCheckin != null) {
                    newCheckinId = String.valueOf(newCheckin.getCheckinId());
                    System.out.println("  ✅ 成功: 创建了训练营 ID=" + newCheckinId);
                    successCount++;
                } else {
                    System.out.println("  ⚠️  返回 null");
                    failCount++;
                }
            } catch (Exception e) {
                System.out.println("  ❌ 失败: " + e.getMessage());
                failCount++;
            }

            // 测试 5: 更新训练营 (PUT)
            System.out.println("\n【测试 5/6】更新训练营 (PUT)...");
            if (newCheckinId != null) {
                try {
                    UpdateCheckinParams updateParams = new UpdateCheckinParams()
                            .status("closed");

                    Checkin updatedCheckin = client.checkins().update(groupId, newCheckinId, updateParams);
                    if (updatedCheckin != null) {
                        System.out.println("  ✅ 成功: 状态=" + updatedCheckin.getStatus());
                        successCount++;
                    } else {
                        // 验证是否真的更新成功
                        Checkin verifyCheckin = client.checkins().get(groupId, newCheckinId);
                        if (verifyCheckin != null && "closed".equals(verifyCheckin.getStatus())) {
                            System.out.println("  ✅ 成功: 虽然返回 null，但状态已更新为 closed");
                            successCount++;
                        } else {
                            System.out.println("  ⚠️  返回 null，且状态未更新");
                            failCount++;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("  ❌ 失败: " + e.getMessage());
                    failCount++;
                }
            } else {
                System.out.println("  ⚠️  跳过（创建失败）");
            }

            // 测试 6: 查询训练营统计 (GET)
            System.out.println("\n【测试 6/6】查询训练营统计 (GET)...");
            if (newCheckinId != null) {
                try {
                    var stats = client.checkins().getStatistics(groupId, newCheckinId);
                    System.out.println("  ✅ 成功: 获取到统计信息");
                    successCount++;
                } catch (Exception e) {
                    System.out.println("  ❌ 失败: " + e.getMessage());
                    failCount++;
                }
            } else {
                System.out.println("  ⚠️  跳过（创建失败）");
            }

            // 输出总结
            System.out.println("\n========================================");
            System.out.println("测试总结:");
            System.out.println("  成功: " + successCount);
            System.out.println("  失败: " + failCount);
            System.out.println("========================================");

            if (failCount == 0) {
                System.out.println("\n✅ 结论：禁用签名后，所有接口都正常工作！");
                System.out.println("   可以安全地默认禁用签名。");
            } else {
                System.out.println("\n⚠️  结论：有 " + failCount + " 个接口失败");
                System.out.println("   需要保留签名功能，让用户根据需要选择。");
            }

        } catch (Exception e) {
            System.err.println("\n整体测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
