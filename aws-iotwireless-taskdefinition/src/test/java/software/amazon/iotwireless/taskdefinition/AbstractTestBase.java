package software.amazon.iotwireless.taskdefinition;

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
    protected static final String TEST_NAME;
    protected static final String TEST_UPDATE_DATAROLE;
    protected static final String TEST_UPDATE_DATASOURCE;
    protected static final String TEST_UPDATE_SIGNATURE;
    protected static final String TEST_CUR_MODEL;
    protected static final String TEST_CUR_VERSION;
    protected static final String TEST_STATION;
    protected static final String TEST_UPDATE_MODEL;
    protected static final String NEXT_TOKEN;
    protected static final software.amazon.awssdk.services.iotwireless.model.LoRaWANUpdateGatewayTaskEntry TEST_LORAWAN_LIST;
    protected static final String TEST_UPDATEVERSION;
    protected static final software.amazon.iotwireless.taskdefinition.LoRaWANUpdateGatewayTaskCreate TEST_LORAWAN;
    protected static final software.amazon.awssdk.services.iotwireless.model.LoRaWANUpdateGatewayTaskCreate TEST_LORAWAN_RESPONSE;
    protected static final software.amazon.iotwireless.taskdefinition.UpdateWirelessGatewayTaskCreate TEST_UPDATE;
    protected static final software.amazon.awssdk.services.iotwireless.model.UpdateWirelessGatewayTaskCreate TEST_UPDATE_RESPONSE;
    protected static final software.amazon.iotwireless.taskdefinition.LoRaWANGatewayVersion TEST_CURRENT_VERSION;
    protected static final software.amazon.iotwireless.taskdefinition.LoRaWANGatewayVersion TEST_UPDATE_VERSION;
    protected static final software.amazon.awssdk.services.iotwireless.model.LoRaWANGatewayVersion TEST_CURRENT_VERSION_RESPONSE;
    protected static final software.amazon.awssdk.services.iotwireless.model.LoRaWANGatewayVersion TEST_UPDATE_VERSION_RESPONSE;
    protected static final ResourceModel TEST_CREATE_RESOURCE_MODEL;
    protected static final ResourceModel TEST_RESOURCE_MODEL;
    protected static final LoggerProxy logger;

    static{
        MOCK_CREDENTIALS = new Credentials("accessKey", "secretKey", "token");
        TEST_ID = "51a8e18c-65aa-32c3-8a24-9375e1bc8ebe";
        TEST_NAME = "test name";
        TEST_UPDATE_DATAROLE = "arn:aws:iam::358260620153:role/SDK_Test_Role";
        TEST_UPDATE_DATASOURCE = "s3://cupsalphagafirmwarebin/station";
        TEST_UPDATE_SIGNATURE = "test_signature";
        TEST_STATION = "2.0.5";
        TEST_CUR_MODEL = "linux";
        TEST_CUR_VERSION = "1.0.0";
        TEST_UPDATE_MODEL = "minihub";
        TEST_UPDATEVERSION = "1.0.1";
        NEXT_TOKEN = "4b90a7e4-b790-456b";

        TEST_CURRENT_VERSION =  software.amazon.iotwireless.taskdefinition.LoRaWANGatewayVersion.builder()
                .model(TEST_CUR_MODEL)
                .packageVersion(TEST_CUR_VERSION)
                .station(TEST_STATION)
                .build();

        TEST_CURRENT_VERSION_RESPONSE =  software.amazon.awssdk.services.iotwireless.model.LoRaWANGatewayVersion.builder()
                .model(TEST_CUR_MODEL)
                .packageVersion(TEST_CUR_VERSION)
                .station(TEST_STATION)
                .build();

        TEST_UPDATE_VERSION_RESPONSE =  software.amazon.awssdk.services.iotwireless.model.LoRaWANGatewayVersion.builder()
                .model(TEST_UPDATE_MODEL)
                .packageVersion(TEST_UPDATEVERSION)
                .station(TEST_STATION)
                .build();

        TEST_UPDATE_VERSION =  software.amazon.iotwireless.taskdefinition.LoRaWANGatewayVersion.builder()
                .model(TEST_UPDATE_MODEL)
                .packageVersion(TEST_UPDATEVERSION)
                .station(TEST_STATION)
                .build();

        TEST_LORAWAN = software.amazon.iotwireless.taskdefinition.LoRaWANUpdateGatewayTaskCreate.builder()
                .sigKeyCrc((long)1)
                .updateSignature(TEST_UPDATE_SIGNATURE)
                .currentVersion(TEST_CURRENT_VERSION)
                .updateVersion(TEST_UPDATE_VERSION)
                .build();

        TEST_LORAWAN_RESPONSE = software.amazon.awssdk.services.iotwireless.model.LoRaWANUpdateGatewayTaskCreate
                .builder()
                .sigKeyCrc((long)1)
                .updateSignature(TEST_UPDATE_SIGNATURE)
                .currentVersion(TEST_CURRENT_VERSION_RESPONSE)
                .updateVersion(TEST_UPDATE_VERSION_RESPONSE)
                .build();

        TEST_LORAWAN_LIST = software.amazon.awssdk.services.iotwireless.model.LoRaWANUpdateGatewayTaskEntry.builder()
                .currentVersion(TEST_CURRENT_VERSION_RESPONSE)
                .updateVersion(TEST_UPDATE_VERSION_RESPONSE)
                .build();


        TEST_UPDATE = software.amazon.iotwireless.taskdefinition.UpdateWirelessGatewayTaskCreate.builder()
                .updateDataRole(TEST_UPDATE_DATAROLE)
                .updateDataSource(TEST_UPDATE_DATASOURCE)
                .loRaWAN(TEST_LORAWAN)
                .build();

        TEST_UPDATE_RESPONSE = software.amazon.awssdk.services.iotwireless.model.UpdateWirelessGatewayTaskCreate
                .builder()
                .updateDataRole(TEST_UPDATE_DATAROLE)
                .updateDataSource(TEST_UPDATE_DATASOURCE)
                .loRaWAN(TEST_LORAWAN_RESPONSE)
                .build();

        TEST_CREATE_RESOURCE_MODEL = ResourceModel.builder()
                .autoCreateTasks(false)
                .name(TEST_NAME)
                .update(TEST_UPDATE)
                .build();

        TEST_RESOURCE_MODEL = ResourceModel.builder()
                .id(TEST_ID)
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
