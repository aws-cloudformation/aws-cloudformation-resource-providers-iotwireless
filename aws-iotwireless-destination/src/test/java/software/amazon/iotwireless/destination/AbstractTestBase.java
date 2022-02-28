package software.amazon.iotwireless.destination;

import software.amazon.awssdk.awscore.AwsRequest;
import software.amazon.awssdk.awscore.AwsResponse;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.Destinations;
import software.amazon.cloudformation.proxy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractTestBase {
    protected static final Credentials MOCK_CREDENTIALS;
    protected static final String TEST_NAME;
    protected static final String TEST_EXPRESSION_TYPE;
    protected static final String TEST_EXPRESSION;
    protected static final String TEST_DESCRIPTION;
    protected static final Set<software.amazon.iotwireless.destination.Tag> TEST_TAGS;
    protected static final software.amazon.iotwireless.destination.Tag TEST_TAG;
    protected static final String TEST_KEY;
    protected static final String TEST_VALUE;
    protected static final String TEST_ROLE_ARN;
    protected static final String TEST_ARN;
    protected static final Integer TEST_TOTAL_COUNT;
    protected static final String TEST_NEXT_TOKEN;
    protected static final List<Destinations> TEST_DESTINATION_LIST;
    protected static final Destinations TEST_DESTINATION;
    protected static final ResourceModel TEST_CREATE_RESOURCE_MODEL;
    protected static final ResourceModel TEST_RESOURCE_MODEL;
    protected static final CallbackContext TEST_CALLBACK;
    protected static final ResourceHandlerRequest<ResourceModel> TEST_REQUEST;
    protected static final LoggerProxy logger;

    static {
        MOCK_CREDENTIALS = new Credentials("accessKey", "secretKey", "token");
        TEST_NAME = "test name";
        TEST_EXPRESSION_TYPE = "text_expression_type";
        TEST_EXPRESSION = "test_expression";
        TEST_DESCRIPTION = "test_description";
        TEST_ROLE_ARN = "test_description";
        TEST_ARN = "arn";
        TEST_KEY = "key";
        TEST_VALUE = "value";
        TEST_TOTAL_COUNT = 1;
        TEST_NEXT_TOKEN = "test next token";

        TEST_TAGS = new HashSet<software.amazon.iotwireless.destination.Tag>();
        TEST_TAG = new software.amazon.iotwireless.destination.Tag();
        TEST_TAG.setKey(TEST_KEY);
        TEST_TAG.setValue(TEST_VALUE);
        TEST_TAGS.add(TEST_TAG);

        TEST_DESTINATION_LIST = new ArrayList<Destinations>();
        TEST_DESTINATION = Destinations.builder()
                .arn(TEST_ARN)
                .name(TEST_NAME)
                .expressionType(TEST_EXPRESSION_TYPE)
                .expression(TEST_EXPRESSION)
                .description(TEST_DESCRIPTION)
                .roleArn(TEST_ROLE_ARN)
                .build();
        TEST_DESTINATION_LIST.add(TEST_DESTINATION);


        TEST_CREATE_RESOURCE_MODEL = ResourceModel.builder()
                .name(TEST_NAME)
                .expressionType(TEST_EXPRESSION_TYPE)
                .expression(TEST_EXPRESSION)
                .description(TEST_DESCRIPTION)
                .roleArn(TEST_ROLE_ARN)
                .tags(TEST_TAGS)
                .build();

        TEST_RESOURCE_MODEL = ResourceModel.builder()
                .name(TEST_NAME)
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
