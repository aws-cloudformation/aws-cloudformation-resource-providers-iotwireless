package software.amazon.iotwireless.wirelessdeviceimporttask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.time.Instant;
import software.amazon.awssdk.services.iotwireless.model.SidewalkGetStartImportInfo;
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
    protected static final String TEST_ID;
    protected static final String TEST_DESTINATION_NAME;
    protected static final Instant TEST_CREATION_TIME;
    protected static final String TEST_STATUS;
    protected static final String TEST_STATUS_REASON;
    protected static final long TEST_INITIALIZED_IMPORTED_DEVICES_COUNT;
    protected static final long TEST_PENDING_IMPORTED_DEVICES_COUNT;
    protected static final long TEST_ONBOARDED_IMPORTED_DEVICES_COUNT;
    protected static final long TEST_FAILED_IMPORTED_DEVICES_COUNT;
    protected static final ResourceModel TEST_CREATE_RESOURCE_MODEL;
    protected static final ResourceModel TEST_UPDATE_RESOURCE_MODEL_PREVIOUS_STATE;
    protected static final ResourceModel TEST_UPDATE_RESOURCE_MODEL_DESIRED_STATE;
    protected static final ResourceModel TEST_RESOURCE_MODEL;
    protected static final SidewalkGetStartImportInfo TEST_SIDEWALK;
    protected static final List<String> TEST_DEVICE_CREATION_FILE_LIST;

    static {
        MOCK_CREDENTIALS = new Credentials("accessKey", "secretKey", "token");
        logger = new LoggerProxy();
        TEST_ARN = "arn";
        TEST_ID = "id";
        TEST_DESTINATION_NAME = "destinationName";
        TEST_CREATION_TIME = Instant.parse("2017-02-03T10:37:30.00Z");
        TEST_STATUS = "status";
        TEST_STATUS_REASON = "statusReason";
        TEST_INITIALIZED_IMPORTED_DEVICES_COUNT = 1;
        TEST_PENDING_IMPORTED_DEVICES_COUNT = 1;
        TEST_ONBOARDED_IMPORTED_DEVICES_COUNT = 1;
        TEST_FAILED_IMPORTED_DEVICES_COUNT = 1;
        TEST_DEVICE_CREATION_FILE_LIST = new ArrayList<>(Arrays.asList("test0", "test1"));

        TEST_SIDEWALK = SidewalkGetStartImportInfo.builder()
                .deviceCreationFileList(TEST_DEVICE_CREATION_FILE_LIST)
                .role("Role")
                .build();

        TEST_UPDATE_RESOURCE_MODEL_PREVIOUS_STATE = ResourceModel.builder()
                .sidewalk(new Sidewalk())
                .build();
        TEST_UPDATE_RESOURCE_MODEL_DESIRED_STATE = ResourceModel.builder()
                .sidewalk(new Sidewalk())
                .build();

        TEST_RESOURCE_MODEL = ResourceModel.builder()
                .id(TEST_ID)
                .build();

        TEST_CREATE_RESOURCE_MODEL =  ResourceModel.builder()
                .destinationName(TEST_DESTINATION_NAME)
                .statusReason(TEST_STATUS_REASON)
                .sidewalk(new Sidewalk())
                .build();
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
