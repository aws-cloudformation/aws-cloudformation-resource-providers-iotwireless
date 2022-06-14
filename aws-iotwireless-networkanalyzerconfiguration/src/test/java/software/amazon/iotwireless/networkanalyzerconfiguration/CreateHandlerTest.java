package software.amazon.iotwireless.networkanalyzerconfiguration;

import java.time.Duration;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.CreateNetworkAnalyzerConfigurationRequest;
import software.amazon.awssdk.services.iotwireless.model.CreateNetworkAnalyzerConfigurationResponse;
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
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateHandlerTest extends AbstractTestBase {

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
        final software.amazon.iotwireless.networkanalyzerconfiguration.CreateHandler handler = new CreateHandler();

        final CreateNetworkAnalyzerConfigurationResponse createNetworkAnalyzerConfigurationResponse= CreateNetworkAnalyzerConfigurationResponse.builder()
                .arn(TEST_ARN)
                .name(TEST_NAME)
                .build();
        when(proxyClient.client().createNetworkAnalyzerConfiguration(any(CreateNetworkAnalyzerConfigurationRequest.class))).thenReturn(createNetworkAnalyzerConfigurationResponse);

        final ResourceHandlerRequest<software.amazon.iotwireless.networkanalyzerconfiguration.ResourceModel> request =
                ResourceHandlerRequest.<software.amazon.iotwireless.networkanalyzerconfiguration.ResourceModel>builder().desiredResourceState(TEST_CREATE_RESOURCE_MODEL).build();
        final ProgressEvent<ResourceModel, software.amazon.iotwireless.networkanalyzerconfiguration.CallbackContext> response = handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS);
        assertThat(response.getCallbackDelaySeconds()).isEqualTo(0);
        assertThat(response.getResourceModel()).isEqualTo(request.getDesiredResourceState());
        assertThat(response.getResourceModels()).isNull();
        assertThat(response.getMessage()).isNull();
        assertThat(response.getErrorCode()).isNull();
    }

}
