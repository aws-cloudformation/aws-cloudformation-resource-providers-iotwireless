package software.amazon.iotwireless.wirelessgateway;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.UpdateWirelessGatewayRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateWirelessGatewayResponse;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessGatewayWithThingRequest;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessGatewayWithThingResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;
import software.amazon.cloudformation.proxy.ProgressEvent;

public class UpdateHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress -> {
                    if (model.getThingArn() != null) {
                        return proxy.initiate("AWS-IoTWireless-WirelessGateway::AssociateThing", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                .translateToServiceRequest(Translator::associateWirelessGatewayWithThing)
                                .makeServiceCall(this::associateThing)
                                .progress();
                    }
                    return progress;
                })
                .then(progress -> {
                    if (model.getName() != null || model.getDescription() != null || model.getId() != null) {
                        return proxy.initiate("AWS-IoTWireless-WirelessGateway::Update", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                .translateToServiceRequest(Translator::translateToFirstUpdateRequest)
                                .makeServiceCall(this::updateResource)
                                .progress();
                    }
                    return progress;
                })
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));
    }

    private UpdateWirelessGatewayResponse updateResource(
            UpdateWirelessGatewayRequest updateRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        UpdateWirelessGatewayResponse response = null;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::updateWirelessGateway);
        } catch (final Exception e) {
            throw handleException(e, updateRequest);
        }
        return response;
    }

    private AssociateWirelessGatewayWithThingResponse associateThing(
            AssociateWirelessGatewayWithThingRequest updateRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        AssociateWirelessGatewayWithThingResponse response = null;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::associateWirelessGatewayWithThing);
        } catch (final Exception e) {
            throw handleException(e, updateRequest);
        }
        return response;
    }
}
