package software.amazon.iotwireless.destination;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.ListDestinationsRequest;
import software.amazon.awssdk.services.iotwireless.model.ListDestinationsResponse;
import software.amazon.cloudformation.proxy.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;


@ExtendWith(MockitoExtension.class)
public class ListHandlerTest extends AbstractTestBase {

    IotWirelessClient sdkClient;
    @Mock
    private AmazonWebServicesClientProxy proxy;
    @Mock
    private Logger logger;
    private ProxyClient<IotWirelessClient> proxyClient;

    @BeforeEach
    public void setup() {
        proxy = mock(AmazonWebServicesClientProxy.class);
        logger = mock(Logger.class);
        sdkClient = mock(IotWirelessClient.class);
        proxyClient = MOCK_PROXY(proxy, sdkClient);
    }

    @Test
    public void handleRequest_SimpleSuccess() {
//        final ListHandler handler = new ListHandler();
//
//        final ListDestinationsResponse listDestinationsResponse = ListDestinationsResponse.builder()
//                .nextToken(TEST_NEXT_TOKEN)
//                .destinationList(TEST_DESTINATION_LIST)
//                .build();
//
//        assertThat(listDestinationsResponse.destinationList()).isNotNull();
//
//
//        ResourceModel model = ResourceModel.builder()
//                .build();
//
//        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
//                .desiredResourceState(model)
//                .build();
//
//        when(proxyClient.client().listDestinations(any(ListDestinationsRequest.class))).thenReturn(listDestinationsResponse);
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
