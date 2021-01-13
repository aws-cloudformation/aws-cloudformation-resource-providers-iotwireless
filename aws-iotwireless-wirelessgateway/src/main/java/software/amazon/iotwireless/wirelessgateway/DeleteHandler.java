package software.amazon.iotwireless.wirelessgateway;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.DeleteWirelessGatewayRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteWirelessGatewayResponse;
import software.amazon.awssdk.services.iotwireless.model.DisassociateWirelessGatewayFromThingRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateWirelessGatewayFromThingResponse;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.HandlerErrorCode;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;
import software.amazon.cloudformation.proxy.ProgressEvent;

public class DeleteHandler extends BaseHandlerStd {
    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(final AmazonWebServicesClientProxy proxy,
                                                                          final ResourceHandlerRequest<ResourceModel> request,
                                                                          final CallbackContext callbackContext,
                                                                          final ProxyClient<IotWirelessClient> proxyClient,
                                                                          final Logger logger) {
        final ResourceModel model = request.getDesiredResourceState();
        return ProgressEvent.progress(model, callbackContext)
                .then(progress -> proxy.initiate("IoTWirelessGateway::DisassociateThing", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToDisassociateRequest)
                        .makeServiceCall(this::disassociateThing)
                        .progress()
                )
                .then(progress -> proxy.initiate("IoTWirelessGateway::Delete", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToDeleteRequest)
                        .makeServiceCall(this::deleteResource)
                        .progress()
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(null));
    }

    private DeleteWirelessGatewayResponse deleteResource(
            final DeleteWirelessGatewayRequest deleteRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        DeleteWirelessGatewayResponse response = null;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(deleteRequest, proxyClient.client()::deleteWirelessGateway);
        } catch (final Exception e) {
            throw handleException(e, deleteRequest);
        }
        return response;
    }

    private DisassociateWirelessGatewayFromThingResponse disassociateThing(
            final DisassociateWirelessGatewayFromThingRequest deleteRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        DisassociateWirelessGatewayFromThingResponse response = null;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(deleteRequest, proxyClient.client()::disassociateWirelessGatewayFromThing);
        } catch (final Exception e) {
            throw handleException(e, deleteRequest);
        }
        return response;
    }
}
