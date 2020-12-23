package software.amazon.iotwireless.serviceprofile;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.ListServiceProfilesRequest;
import software.amazon.awssdk.services.iotwireless.model.ListServiceProfilesResponse;
import software.amazon.cloudformation.proxy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListHandlerTest {

    @Mock
    private AmazonWebServicesClientProxy proxy;

    @Mock
    private Logger logger;

    private ProxyClient<IotWirelessClient> proxyClient;

    IotWirelessClient sdkClient;

    @BeforeEach
    public void setup() {
        proxy = mock(AmazonWebServicesClientProxy.class);
        logger = mock(Logger.class);
        sdkClient = mock(IotWirelessClient.class);
//        proxyClient = MOCK_PROXY(proxy, sdkClient);
        }

    @Test
    public void handleRequest_SimpleSuccess() {
//        final ListHandler handler = new ListHandler();
//
//        final ListServiceProfilesResponse listServiceProfilesResponse = ListServiceProfilesResponse.builder()
//                .totalCount(null)
//                .nextToken(null)
//                .build();
//
//        ResourceModel model = ResourceModel.builder()
//                .maxResults(null)
//                .nextToken(null)
//                .build();
//
//        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
//            .desiredResourceState(model)
//            .build();
//
//        when(proxyClient.client().listServiceProfiles(any(ListServiceProfilesRequest.class))).thenReturn(listServiceProfilesResponse);
//        assertThat(proxy).isNotNull();
//        assertThat(request).isNotNull();
//        assertThat(logger).isNotNull();
//        assertThat(logger).isNotNull();
//        assertThat(handler).isNotNull();
//        assertThat(new CallbackContext()).isNotNull();
//        assertThat(handler.handleRequest(proxy, request, new CallbackContext(), logger)).isNotNull();
//
//        final ProgressEvent<ResourceModel, CallbackContext> response = handler.handleRequest(proxy, request, new CallbackContext(), logger);
//
//        assertThat(response).isNotNull();
//        assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS);
//        assertThat(response.getCallbackContext()).isNull();
//        assertThat(response.getCallbackDelaySeconds()).isEqualTo(0);
//        assertThat(response.getResourceModel()).isNull();
//        assertThat(response.getResourceModels()).isNotNull();
//        assertThat(response.getMessage()).isNull();
//        assertThat(response.getErrorCode()).isNull();
    }
}
