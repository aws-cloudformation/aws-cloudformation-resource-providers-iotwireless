package software.amazon.iotwireless.networkanalyzerconfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import software.amazon.awssdk.awscore.AwsRequest;
import software.amazon.awssdk.awscore.AwsResponse;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Credentials;
import software.amazon.cloudformation.proxy.LoggerProxy;
import software.amazon.cloudformation.proxy.ProxyClient;

public class AbstractTestBase {
  protected static final Credentials MOCK_CREDENTIALS;
  protected static final LoggerProxy logger;
  protected static final String TEST_ARN;
  protected static final String TEST_NAME;
  protected static final String TEST_DESCRIPTION;
  protected static final ResourceModel TEST_CREATE_RESOURCE_MODEL;
  protected static final ResourceModel TEST_RESOURCE_MODEL;
  protected static final ResourceModel TEST_UPDATE_RESOURCE_MODEL_PREVIOUS_STATE;
  protected static final ResourceModel TEST_UPDATE_RESOURCE_MODEL_DESIRED_STATE;
  protected static final TraceContent TEST_TRACE_CONTENT;
  protected static final List<String> TEST_WIRELESS_DEVICES_PREVIOUS;
  protected static final List<String> TEST_WIRELESS_GATEWAYS_PREVIOUS;
  protected static final List<String> TEST_WIRELESS_DEVICES;
  protected static final List<String> TEST_WIRELESS_GATEWAYS;

  static {
    MOCK_CREDENTIALS = new Credentials("accessKey", "secretKey", "token");
    TEST_ARN = "arn";
    TEST_NAME = "id";
    TEST_DESCRIPTION = "Description";

    TEST_TRACE_CONTENT = new TraceContent();
    TEST_TRACE_CONTENT.setLogLevel("INFO");
    TEST_TRACE_CONTENT.setWirelessDeviceFrameInfo("ENABLED");

    TEST_WIRELESS_DEVICES_PREVIOUS = new ArrayList<>(Arrays.asList("testDevice0", "testDevice1"));
    TEST_WIRELESS_GATEWAYS_PREVIOUS = new ArrayList<>(Arrays.asList("testGateways0", "testGateways1"));
    TEST_WIRELESS_DEVICES = new ArrayList<>(Arrays.asList("testDevice1", "testDevice2"));
    TEST_WIRELESS_GATEWAYS = new ArrayList<>(Arrays.asList("testGateways1", "testGateways2"));

    TEST_UPDATE_RESOURCE_MODEL_PREVIOUS_STATE = ResourceModel.builder()
            .name(TEST_NAME)
            .description(TEST_DESCRIPTION)
            .traceContent(TEST_TRACE_CONTENT)
            .wirelessDevices(TEST_WIRELESS_DEVICES_PREVIOUS)
            .wirelessGateways(TEST_WIRELESS_GATEWAYS_PREVIOUS)
            .build();
    TEST_UPDATE_RESOURCE_MODEL_DESIRED_STATE = ResourceModel.builder()
            .arn(TEST_ARN)
            .name(TEST_NAME)
            .description(TEST_DESCRIPTION)
            .traceContent(TEST_TRACE_CONTENT)
            .wirelessDevices(TEST_WIRELESS_DEVICES)
            .wirelessGateways(TEST_WIRELESS_GATEWAYS)
            .build();

    TEST_CREATE_RESOURCE_MODEL =  ResourceModel.builder()
            .name(TEST_NAME)
            .description(TEST_DESCRIPTION)
            .traceContent(TEST_TRACE_CONTENT)
            .wirelessDevices(TEST_WIRELESS_DEVICES)
            .wirelessGateways(TEST_WIRELESS_GATEWAYS)
            .build();
    TEST_RESOURCE_MODEL = ResourceModel.builder().name(TEST_NAME).build();

    logger = new LoggerProxy();
  }
  static ProxyClient<IotWirelessClient> MOCK_PROXY(
    final AmazonWebServicesClientProxy proxy,
    final IotWirelessClient client) {
    return new ProxyClient<IotWirelessClient>() {
      @Override
      public <RequestT extends AwsRequest, ResponseT extends AwsResponse> ResponseT
      injectCredentialsAndInvokeV2(RequestT request, Function<RequestT, ResponseT> requestFunction) {
        return proxy.injectCredentialsAndInvokeV2(request, requestFunction);
      }

      @Override
      public <RequestT extends AwsRequest, ResponseT extends AwsResponse>
      CompletableFuture<ResponseT>
      injectCredentialsAndInvokeV2Async(RequestT request, Function<RequestT, CompletableFuture<ResponseT>> requestFunction) {
        throw new UnsupportedOperationException();
      }

      @Override
      public <RequestT extends AwsRequest, ResponseT extends AwsResponse, IterableT extends SdkIterable<ResponseT>>
      IterableT
      injectCredentialsAndInvokeIterableV2(RequestT request, Function<RequestT, IterableT> requestFunction) {
        return proxy.injectCredentialsAndInvokeIterableV2(request, requestFunction);
      }

      @Override
      public <RequestT extends AwsRequest, ResponseT extends AwsResponse> ResponseInputStream<ResponseT>
      injectCredentialsAndInvokeV2InputStream(RequestT requestT, Function<RequestT, ResponseInputStream<ResponseT>> function) {
        throw new UnsupportedOperationException();
      }

      @Override
      public <RequestT extends AwsRequest, ResponseT extends AwsResponse> ResponseBytes<ResponseT>
      injectCredentialsAndInvokeV2Bytes(RequestT requestT, Function<RequestT, ResponseBytes<ResponseT>> function) {
        throw new UnsupportedOperationException();
      }

      @Override
      public IotWirelessClient client() {
        return client;
      }
    };
  }
}
