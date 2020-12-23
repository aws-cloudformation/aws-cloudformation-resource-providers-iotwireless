package software.amazon.iotwireless.deviceprofile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.awscore.AwsRequest;
import software.amazon.awssdk.awscore.AwsResponse;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.cloudformation.proxy.*;

public class AbstractTestBase {
  protected static final Credentials MOCK_CREDENTIALS;
  protected static final String TEST_TYPE;
  protected static final String TEST_DESTINATION_NAME;
  protected static final String TEST_ARN;
  protected static final String TEST_ID;
  protected static final List<software.amazon.iotwireless.deviceprofile.Tag> TEST_TAGS;
  protected static final software.amazon.iotwireless.deviceprofile.Tag TEST_TAG;
  protected static final software.amazon.iotwireless.deviceprofile.LoRaWANDeviceProfile TEST_LORA_CREATE;
  protected static final software.amazon.awssdk.services.iotwireless.model.LoRaWANDeviceProfile TEST_LORAWAN;
  protected static final String TEST_KEY;
  protected static final String TEST_VALUE;
  protected static final String TEST_DEV_EUI;
  protected static final String TEST_DEVICE_PROFILE_ID;
  protected static final String TEST_SERVICE_PROFILE_ID;
  protected static final String TEST_APP_KEY;
  protected static final String TEST_APP_EUI;
  protected static final ResourceModel TEST_CREATE_RESOURCE_MODEL;
  protected static final ResourceModel TEST_RESOURCE_MODEL;
  protected static final String TEST_NAME;
  protected static final String TEST_DESCRIPTION;
  protected static final String TEST_NEXT_TOKEN;
  protected static final String TEST_LAST_UPLINK_RECEIVED_AT;
  protected static final CallbackContext TEST_CALLBACK;
  protected static final ResourceHandlerRequest<ResourceModel> TEST_REQUEST;
  protected static final LoggerProxy logger;

  static {
    MOCK_CREDENTIALS = new Credentials("accessKey", "secretKey", "token");
    TEST_TYPE = "type";
    TEST_DESTINATION_NAME = "test_destination_name";
    TEST_ARN = "arn";
    TEST_ID = "51a8e18c-65aa-32c3-8a24-9375e1bc8ebe";
    TEST_KEY = "key";
    TEST_VALUE = "value";
    TEST_DEV_EUI = "test dev eui";
    TEST_DEVICE_PROFILE_ID = "test device profile id";
    TEST_SERVICE_PROFILE_ID = "test service profile id";
    TEST_NAME = "test name";
    TEST_DESCRIPTION = "test_description";
    TEST_NEXT_TOKEN = "test next token";
    TEST_LAST_UPLINK_RECEIVED_AT = "2020-09-24T00:52:52.802771964Z";
    TEST_APP_EUI = "test app eui";
    TEST_APP_KEY = "test app key";

    TEST_TAGS = new ArrayList<software.amazon.iotwireless.deviceprofile.Tag>();
    TEST_TAG = new software.amazon.iotwireless.deviceprofile.Tag();
    TEST_TAG.setKey(TEST_KEY);
    TEST_TAG.setValue(TEST_VALUE);
    TEST_TAGS.add(TEST_TAG);

    TEST_LORA_CREATE = new software.amazon.iotwireless.deviceprofile.LoRaWANDeviceProfile();
    TEST_LORA_CREATE.setSupportsClassB(true);

    //TODO: include more of the fields
    TEST_LORAWAN = software.amazon.awssdk.services.iotwireless.model.LoRaWANDeviceProfile.builder()
            .supportsClassB(true)
            .classBTimeout(1)
            .build();

    TEST_CREATE_RESOURCE_MODEL = ResourceModel.builder()
            .name(TEST_NAME)
            .loRaWANDeviceProfile(TEST_LORA_CREATE)
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
