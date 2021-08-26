package software.amazon.iotwireless.multicastgroup;

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
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class AbstractTestBase {
    protected static final Credentials MOCK_CREDENTIALS;
    protected static final String TEST_NAME;
    protected static final String TEST_DESCRIPTION;
    protected static final String TEST_ARN;
    protected static final String TEST_ID;
    protected static final LoRaWAN TEST_LORAWAN;
    protected static final String TEST_STARTTIME;
    protected static final String TEST_RFREGION;
    protected static final String TEST_DICLASS;
    protected static final Integer TEST_NUMBER_OF_DEVICES_REQUESTED;
    protected static final Integer TEST_NUMBER_OF_DEVICES_IN_GROUP;
    protected static final List<Tag> TEST_TAGS;
    protected static final Tag TEST_TAG;
    protected static final String TEST_KEY;
    protected static final String TEST_VALUE;
    protected static final String TEST_STATUS;
    protected static final String TEST_NEXT_TOKEN;
    protected static final List<ResourceModel> TEST_FUOTATASK_LIST;
    protected static final ResourceModel TEST_FUOTATASK;
    protected static final ResourceModel TEST_CREATE_RESOURCE_MODEL;
    protected static final ResourceModel TEST_RESOURCE_MODEL;
    protected static final CallbackContext TEST_CALLBACK;
    protected static final ResourceHandlerRequest<ResourceModel> TEST_REQUEST;
    protected static final LoggerProxy logger;

    static {
        MOCK_CREDENTIALS = new Credentials("accessKey", "secretKey", "token");
        TEST_NAME = "test name";
        TEST_DESCRIPTION = "test_description";
        TEST_ARN = "arn";
        TEST_ID = "id";

        TEST_STARTTIME = "1625267340";
        TEST_RFREGION = "rfregion";
        TEST_DICLASS = "ClassB";
        TEST_NUMBER_OF_DEVICES_REQUESTED = 0;
        TEST_NUMBER_OF_DEVICES_IN_GROUP = 1;
        TEST_LORAWAN = LoRaWAN.builder()
                .rfRegion(TEST_RFREGION)
                .dlClass(TEST_DICLASS)
                .numberOfDevicesRequested(TEST_NUMBER_OF_DEVICES_REQUESTED)
                .numberOfDevicesInGroup(TEST_NUMBER_OF_DEVICES_IN_GROUP)
                .build();

        TEST_STATUS = "Pending";
        TEST_NEXT_TOKEN = "test next token";

        TEST_KEY = "key";
        TEST_VALUE = "value";
        TEST_TAGS = new ArrayList<>();
        TEST_TAG = new Tag();
        TEST_TAG.setKey(TEST_KEY);
        TEST_TAG.setValue(TEST_VALUE);
        TEST_TAGS.add(TEST_TAG);

        TEST_FUOTATASK_LIST = new ArrayList<>();
        TEST_FUOTATASK = ResourceModel.builder()
                .arn(TEST_ARN)
                .id(TEST_ID)
                .name(TEST_NAME)
                .description(TEST_DESCRIPTION)
                .loRaWAN(TEST_LORAWAN)
                .status(TEST_STATUS)
                .build();
        TEST_FUOTATASK_LIST.add(TEST_FUOTATASK);

        TEST_CREATE_RESOURCE_MODEL = ResourceModel.builder()
                .name(TEST_NAME)
                .description(TEST_DESCRIPTION)
                .loRaWAN(LoRaWAN.builder().rfRegion(TEST_RFREGION).build())
                .tags(TEST_TAGS)
                .build();

        TEST_RESOURCE_MODEL = ResourceModel.builder()
                .id(TEST_ID)
                .build();

        TEST_CALLBACK = new CallbackContext();
        TEST_REQUEST = ResourceHandlerRequest.<ResourceModel>builder()
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
