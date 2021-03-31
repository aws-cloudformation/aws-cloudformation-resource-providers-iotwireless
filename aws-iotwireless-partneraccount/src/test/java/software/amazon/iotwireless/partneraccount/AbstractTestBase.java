package software.amazon.iotwireless.partneraccount;

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
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Credentials;
import software.amazon.cloudformation.proxy.LoggerProxy;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class AbstractTestBase {
    protected static final Credentials MOCK_CREDENTIALS;
    protected static final String TEST_ID;
    protected static final String TEST_AMAZON_ID;
    protected static final String TEST_KEY;
    protected static final String TEST_TYPE;
    protected static final String TEST_PRINT;
    protected static final ResourceModel TEST_CREATE_RESOURCE_MODEL;
    protected static final software.amazon.awssdk.services.iotwireless.model.SidewalkAccountInfo TEST_SIDEWALK_CREATE_RESPONSE;
    protected static final software.amazon.awssdk.services.iotwireless.model.SidewalkAccountInfoWithFingerprint TEST_SIDEWALK_RESPONSE;
    protected static final software.amazon.iotwireless.partneraccount.SidewalkAccountInfo TEST_SIDEWALK;
    protected static final ResourceModel TEST_RESOURCE_MODEL;
    protected static final LoggerProxy logger;

    static {
        MOCK_CREDENTIALS = new Credentials("accessKey", "secretKey", "token");
        TEST_ID = "51a8e18c-65aa-32c3-8a24-9375e1bc8ebe";
        TEST_AMAZON_ID = "12345678910111";
        TEST_KEY = "abc8ab4899346a88599180fee9e14fa1ada7b6df989425b7c6d2146dd6c81abc";
        TEST_TYPE = "Sidewalk";
        TEST_PRINT = "8e8e3af232ab69c37d60c860518b153d33e952dc3da4220bdff8fadd481828c8";

        TEST_SIDEWALK =  software.amazon.iotwireless.partneraccount.SidewalkAccountInfo.builder()
               // .amazonId(TEST_AMAZON_ID)
                .appServerPrivateKey(TEST_KEY)
                .build();

        TEST_SIDEWALK_CREATE_RESPONSE = software.amazon.awssdk.services.iotwireless.model.SidewalkAccountInfo.builder()
                .amazonId(TEST_AMAZON_ID)
                .appServerPrivateKey(TEST_KEY)
                .build();

        TEST_CREATE_RESOURCE_MODEL = ResourceModel.builder()
                .sidewalk(TEST_SIDEWALK)
                .build();

        TEST_RESOURCE_MODEL = ResourceModel.builder()
                .partnerAccountId(TEST_AMAZON_ID)
                .partnerType(TEST_TYPE)
                .build();

        TEST_SIDEWALK_RESPONSE = software.amazon.awssdk.services.iotwireless.model.SidewalkAccountInfoWithFingerprint
                .builder()
                .amazonId(TEST_AMAZON_ID)
                .fingerprint(TEST_PRINT)
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
