package software.amazon.iotwireless.wirelessgateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.logging.Logger;
import software.amazon.awssdk.awscore.AwsRequest;
import software.amazon.awssdk.awscore.AwsResponse;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.WirelessGatewayStatistics;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Credentials;
import software.amazon.cloudformation.proxy.LoggerProxy;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class AbstractTestBase {
  protected static final Credentials MOCK_CREDENTIALS;
  protected static final String TEST_ARN;
  protected static final String TEST_ID;
  protected static final List<software.amazon.iotwireless.wirelessgateway.Tag> TEST_TAGS;
  protected static final software.amazon.iotwireless.wirelessgateway.Tag TEST_TAG;
  protected static final String TEST_KEY;
  protected static final String TEST_VALUE;
  protected static final software.amazon.iotwireless.wirelessgateway.LoRaWANGateway TEST_LORA;
  protected static final software.amazon.awssdk.services.iotwireless.model.LoRaWANGateway TEST_LORAWAN;
  protected static final String TEST_GATEWAY_EUI;
  protected static final String TEST_RF_REGION;
  protected static final ResourceModel TEST_CREATE_RESOURCE_MODEL;
  protected static final ResourceModel TEST_RESOURCE_MODEL;
  protected static final String TEST_NAME;
  protected static final String TEST_DESCRIPTION;
  protected static final String TEST_NEXT_TOKEN;
  protected static final ArrayList<WirelessGatewayStatistics> TEST_GATEWAY_LIST;
  protected static final WirelessGatewayStatistics TEST_GATEWAY;
  protected static final String TEST_LAST_UPLINK_RECEIVED_AT;
  protected static final CallbackContext TEST_CALLBACK;
  protected static final ResourceHandlerRequest<ResourceModel> TEST_REQUEST;
  protected static final LoggerProxy logger;

  static {
    MOCK_CREDENTIALS = new Credentials("accessKey", "secretKey", "token");
    TEST_ARN = "arn";
    TEST_ID = "51a8e18c-65aa-32c3-8a24-9375e1bc8ebe";
    TEST_KEY = "key";
    TEST_VALUE = "value";
    TEST_GATEWAY_EUI = "01101101101422f2";
    TEST_RF_REGION = "US915";
    TEST_NAME = "test name";
    TEST_DESCRIPTION = "test_description";
    TEST_NEXT_TOKEN = "test next token";
    TEST_LAST_UPLINK_RECEIVED_AT = "2020-09-24T00:52:52.802771964Z";


    TEST_TAGS = new ArrayList<software.amazon.iotwireless.wirelessgateway.Tag>();
    TEST_TAG = new software.amazon.iotwireless.wirelessgateway.Tag();
    TEST_TAG.setKey(TEST_KEY);
    TEST_TAG.setValue(TEST_VALUE);
    TEST_TAGS.add(TEST_TAG);

    TEST_LORA = software.amazon.iotwireless.wirelessgateway.LoRaWANGateway.builder()
            .gatewayEui(TEST_GATEWAY_EUI)
            .rfRegion(TEST_RF_REGION)
            .build();

    TEST_LORAWAN = software.amazon.awssdk.services.iotwireless.model.LoRaWANGateway.builder()
            .gatewayEui(TEST_GATEWAY_EUI)
            .rfRegion(TEST_RF_REGION)
            .build();

    TEST_GATEWAY_LIST = new ArrayList<WirelessGatewayStatistics>();
    TEST_GATEWAY = WirelessGatewayStatistics.builder()
            .arn(TEST_ARN)
            .id(TEST_ID)
            .name(TEST_NAME)
            .description(TEST_DESCRIPTION)
            .loRaWAN(TEST_LORAWAN)
            .lastUplinkReceivedAt(TEST_LAST_UPLINK_RECEIVED_AT)
            .build();
    TEST_GATEWAY_LIST.add(TEST_GATEWAY);

    TEST_CREATE_RESOURCE_MODEL = ResourceModel.builder()
            .name(TEST_NAME)
            .description(TEST_DESCRIPTION)
            .loRaWAN(TEST_LORA)
            .tags(TEST_TAGS)
            .build();

    TEST_RESOURCE_MODEL = ResourceModel.builder()
            .id(TEST_ID)
            .build();

    TEST_CALLBACK = new CallbackContext();
    TEST_REQUEST = ResourceHandlerRequest.<ResourceModel>builder()
            .desiredResourceState(TEST_RESOURCE_MODEL)
            .build();

    logger = new LoggerProxy();
  }
  static ProxyClient<IotWirelessClient> MOCK_PROXY(
    final AmazonWebServicesClientProxy proxy,
    final IotWirelessClient sdkClient) {
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
        return sdkClient;
      }
    };
  }
}
