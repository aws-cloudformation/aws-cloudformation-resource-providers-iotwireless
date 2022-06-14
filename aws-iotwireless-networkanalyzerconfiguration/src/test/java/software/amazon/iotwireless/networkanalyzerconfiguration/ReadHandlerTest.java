package software.amazon.iotwireless.networkanalyzerconfiguration;

import java.time.Duration;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.GetNetworkAnalyzerConfigurationRequest;
import software.amazon.awssdk.services.iotwireless.model.GetNetworkAnalyzerConfigurationResponse;
import software.amazon.awssdk.services.iotwireless.model.ListTagsForResourceRequest;
import software.amazon.awssdk.services.iotwireless.model.ListTagsForResourceResponse;
import software.amazon.awssdk.services.iotwireless.model.TraceContent;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class ReadHandlerTest extends AbstractTestBase {
    @Mock
    private AmazonWebServicesClientProxy proxy;

    @Mock
    private ProxyClient<IotWirelessClient> proxyClient;

    @Mock
    IotWirelessClient sdkClient;

    @BeforeEach
    public void setup() {
        proxy = new AmazonWebServicesClientProxy(logger, MOCK_CREDENTIALS, () -> Duration.ofSeconds(600).toMillis());
        sdkClient = mock(IotWirelessClient.class);
        proxyClient = MOCK_PROXY(proxy, sdkClient);
    }

    @AfterEach
    public void tear_down() {
        verify(sdkClient, atLeastOnce()).serviceName();
        verifyNoMoreInteractions(sdkClient);
    }

    @Test
    public void handleRequest_SimpleSuccess() {
        final ReadHandler handler = new ReadHandler();

        final GetNetworkAnalyzerConfigurationResponse getNetworkAnalyzerConfigurationResponse= GetNetworkAnalyzerConfigurationResponse.builder()
                .arn(TEST_ARN)
                .name(TEST_NAME)
                .description(TEST_DESCRIPTION)
                .traceContent(TraceContent.builder().wirelessDeviceFrameInfo("ENABLED").logLevel("INFO").build())
                .wirelessDevices(TEST_WIRELESS_DEVICES)
                .wirelessGateways(TEST_WIRELESS_GATEWAYS)
                .build();

        final ListTagsForResourceResponse listTagsForResourceResponse = ListTagsForResourceResponse.builder().build();

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
                .desiredResourceState(TEST_RESOURCE_MODEL)
                .build();

        when(proxyClient.client().getNetworkAnalyzerConfiguration(any(GetNetworkAnalyzerConfigurationRequest.class))).thenReturn(getNetworkAnalyzerConfigurationResponse);
        when(proxyClient.client().listTagsForResource(any(ListTagsForResourceRequest.class))).thenReturn(listTagsForResourceResponse);

        final ProgressEvent<ResourceModel, CallbackContext> response = handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS);
        assertThat(response.getCallbackDelaySeconds()).isEqualTo(0);
        assertThat(response.getResourceModel()).isEqualTo(request.getDesiredResourceState());
        assertThat(response.getResourceModels()).isNull();
        assertThat(response.getMessage()).isNull();
        assertThat(response.getErrorCode()).isNull();
    }
}
