package client

import (
	"testing"
	"time"
)

func TestNewBuilder(t *testing.T) {
	builder := NewBuilder()
	if builder == nil {
		t.Fatal("Builder should not be nil")
	}

	if builder.config.BaseURL != DefaultConfig.BaseURL {
		t.Errorf("Expected BaseURL %s, got %s", DefaultConfig.BaseURL, builder.config.BaseURL)
	}

	if builder.config.Timeout != DefaultConfig.Timeout {
		t.Errorf("Expected Timeout %v, got %v", DefaultConfig.Timeout, builder.config.Timeout)
	}
}

func TestBuilderSetToken(t *testing.T) {
	builder := NewBuilder()
	result := builder.SetToken("test-token")

	if result != builder {
		t.Error("SetToken should return the same builder instance")
	}

	if builder.config.Token != "test-token" {
		t.Errorf("Expected token 'test-token', got '%s'", builder.config.Token)
	}
}

func TestBuilderSetBaseURL(t *testing.T) {
	builder := NewBuilder()
	customURL := "https://custom.api.com"
	builder.SetBaseURL(customURL)

	if builder.config.BaseURL != customURL {
		t.Errorf("Expected BaseURL '%s', got '%s'", customURL, builder.config.BaseURL)
	}
}

func TestBuilderSetTimeout(t *testing.T) {
	builder := NewBuilder()
	timeout := 5 * time.Second
	builder.SetTimeout(timeout)

	if builder.config.Timeout != timeout {
		t.Errorf("Expected timeout %v, got %v", timeout, builder.config.Timeout)
	}
}

func TestBuilderSetRetryCount(t *testing.T) {
	builder := NewBuilder()
	builder.SetRetryCount(5)

	if builder.config.RetryCount != 5 {
		t.Errorf("Expected retry count 5, got %d", builder.config.RetryCount)
	}
}

func TestBuilderSetRetryDelay(t *testing.T) {
	builder := NewBuilder()
	delay := 2 * time.Second
	builder.SetRetryDelay(delay)

	if builder.config.RetryDelay != delay {
		t.Errorf("Expected retry delay %v, got %v", delay, builder.config.RetryDelay)
	}
}

func TestBuilderSetDeviceID(t *testing.T) {
	builder := NewBuilder()
	deviceID := "test-device-123"
	builder.SetDeviceID(deviceID)

	if builder.config.DeviceID != deviceID {
		t.Errorf("Expected device ID '%s', got '%s'", deviceID, builder.config.DeviceID)
	}
}

func TestBuilderSetAppVersion(t *testing.T) {
	builder := NewBuilder()
	version := "5.60.0"
	builder.SetAppVersion(version)

	if builder.config.AppVersion != version {
		t.Errorf("Expected app version '%s', got '%s'", version, builder.config.AppVersion)
	}
}

func TestBuilderBuildWithoutToken(t *testing.T) {
	builder := NewBuilder()
	client, err := builder.Build()

	if err == nil {
		t.Error("Expected error when building without token")
	}

	if client != nil {
		t.Error("Client should be nil when build fails")
	}

	buildErr, ok := err.(*BuildError)
	if !ok {
		t.Error("Error should be of type BuildError")
	}

	if buildErr.Message == "" {
		t.Error("BuildError should have a message")
	}
}

func TestBuilderBuildWithToken(t *testing.T) {
	builder := NewBuilder()
	client, err := builder.SetToken("test-token").Build()

	if err != nil {
		t.Errorf("Build should succeed with token: %v", err)
	}

	if client == nil {
		t.Error("Client should not be nil")
	}
}

func TestBuilderMustBuild(t *testing.T) {
	builder := NewBuilder().SetToken("test-token")
	client := builder.MustBuild()

	if client == nil {
		t.Error("Client should not be nil")
	}
}

func TestBuilderMustBuildPanic(t *testing.T) {
	defer func() {
		if r := recover(); r == nil {
			t.Error("MustBuild should panic without token")
		}
	}()

	builder := NewBuilder()
	builder.MustBuild()
}

func TestBuilderChaining(t *testing.T) {
	builder := NewBuilder()

	result := builder.
		SetToken("test-token").
		SetBaseURL("https://custom.api.com").
		SetTimeout(5 * time.Second).
		SetRetryCount(3).
		SetRetryDelay(time.Second).
		SetDeviceID("device-123").
		SetAppVersion("5.60.0")

	if result != builder {
		t.Error("All setter methods should return the same builder instance")
	}

	client, err := builder.Build()
	if err != nil {
		t.Errorf("Build should succeed: %v", err)
	}

	if client == nil {
		t.Error("Client should not be nil")
	}
}

func TestClientModules(t *testing.T) {
	client := NewBuilder().SetToken("test-token").MustBuild()

	if client.Groups() == nil {
		t.Error("Groups module should not be nil")
	}

	if client.Topics() == nil {
		t.Error("Topics module should not be nil")
	}

	if client.Users() == nil {
		t.Error("Users module should not be nil")
	}

	if client.Checkins() == nil {
		t.Error("Checkins module should not be nil")
	}

	if client.Dashboard() == nil {
		t.Error("Dashboard module should not be nil")
	}
}

func TestClientModulesSingleton(t *testing.T) {
	client := NewBuilder().SetToken("test-token").MustBuild()

	groups1 := client.Groups()
	groups2 := client.Groups()

	if groups1 != groups2 {
		t.Error("Multiple calls to Groups() should return the same instance")
	}
}

func TestMultipleClientsIndependent(t *testing.T) {
	client1 := NewBuilder().SetToken("token-1").MustBuild()
	client2 := NewBuilder().SetToken("token-2").MustBuild()

	if client1 == client2 {
		t.Error("Different clients should be different instances")
	}

	if client1.Groups() == client2.Groups() {
		t.Error("Different clients should have different module instances")
	}
}

func TestBuildErrorError(t *testing.T) {
	err := &BuildError{Message: "test error"}

	if err.Error() != "test error" {
		t.Errorf("Expected error message 'test error', got '%s'", err.Error())
	}
}
