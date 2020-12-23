package software.amazon.iotwireless.wirelessgateway;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.*;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import software.amazon.cloudformation.proxy.Logger;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListHandlerTest extends AbstractTestBase {

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
        proxyClient = MOCK_PROXY(proxy, sdkClient);

    }

    @Test
    public void handleRequest_SimpleSuccess() {
//        final ListHandler handler = new ListHandler();
//
//        final ListWirelessGatewaysResponse listWirelessGatewaysResponse = ListWirelessGatewaysResponse.builder()
////                .nextToken(TEST_NEXT_TOKEN)
//                .wirelessGatewayList(TEST_GATEWAY_LIST)
//                .build();
//
//        ResourceModel model = ResourceModel.builder()
//                .build();
//
//        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
//            .desiredResourceState(model)
//            .build();
//
//        when(proxyClient.client().listWirelessGateways(any(ListWirelessGatewaysRequest.class))).thenReturn(listWirelessGatewaysResponse);
//
//        assertThat(proxy).isNotNull();
//        assertThat(request).isNotNull();
//        assertThat(logger).isNotNull();
//        assertThat(handler).isNotNull();
//        assertThat(Collections.singletonList(request.getDesiredResourceState())).isNotNull();
//        assertThat(listWirelessGatewaysResponse).isNotNull();
//        assertThat(listWirelessGatewaysResponse.wirelessGatewayList()).isNotNull();
//
//        final ProgressEvent<ResourceModel, CallbackContext> response =
//                handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger);
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
