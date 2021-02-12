package software.amazon.iotwireless.wirelessgateway;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.CreateWirelessGatewayRequest;
import software.amazon.awssdk.services.iotwireless.model.CreateWirelessGatewayResponse;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class CreateHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        ResourceModel model = request.getDesiredResourceState();
        String clientRequestToken = request.getClientRequestToken();

        if (model.getId() != null || model.getArn() != null || model.getThingArn() != null ) {
            throw new CfnInvalidRequestException("Attempting to set a ReadOnly Property.");
        }
        return ProgressEvent.progress(model, callbackContext)
                .then(progress ->
                        proxy.initiate("AWS-IoTWireless-WirelessGateway::Create", proxyClient, model, callbackContext)
                                .translateToServiceRequest(resourceModel -> Translator.translateToCreateRequest(resourceModel, clientRequestToken))
                                .makeServiceCall(this::createResource)
                                .done((response) -> {
                                    model.setId(response.id());
                                    model.setArn(response.arn());
                                    return progress;
                                }))
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));
    }

    private CreateWirelessGatewayResponse createResource(
            CreateWirelessGatewayRequest createRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        CreateWirelessGatewayResponse response = null;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(createRequest, proxyClient.client()::createWirelessGateway);
        } catch (final Exception e) {
            throw handleException(e, createRequest);
        }
        return response;
    }
}
