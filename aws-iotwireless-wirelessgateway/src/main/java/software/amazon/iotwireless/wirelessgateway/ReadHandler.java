package software.amazon.iotwireless.wirelessgateway;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessGatewayRequest;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessGatewayResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class ReadHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress -> proxy.initiate("AWS-IoTWireless-WirelessGateway::Read", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::getResource)
                        .done(getResponse -> {
                            model.setId(getResponse.id());
                            model.setArn(getResponse.arn());
                            model.setName(getResponse.name());
                            model.setDescription(getResponse.description());
                            model.setLoRaWANGateway(Translator.translateToLoRaWAN(getResponse.loRaWAN()));
                            model.setThingArn(getResponse.thingArn());
                            model.setThingName(getResponse.thingName());
                            return ProgressEvent.progress(model, callbackContext);
                        })
                )
                .then(progress -> {
                    return ProgressEvent.defaultSuccessHandler(Translator.unsetWriteOnly(model));
                });
    }

    private GetWirelessGatewayResponse getResource(
            GetWirelessGatewayRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetWirelessGatewayResponse response = null;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getWirelessGateway);
        } catch (final Exception e) {
            throw handleException(e, getRequest);
        }
        return response;
    }
}