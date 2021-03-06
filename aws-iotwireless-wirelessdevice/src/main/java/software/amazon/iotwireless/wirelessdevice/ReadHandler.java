package software.amazon.iotwireless.wirelessdevice;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessDeviceRequest;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessDeviceResponse;
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
                .then(progress -> proxy.initiate("AWS-IoTWireless-WirelessDevice::Read", proxyClient, request.getDesiredResourceState(), callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::getResource)
                        .done(getResponse -> {
                            model.setId(getResponse.id());
                            model.setArn(getResponse.arn());
                            model.setType(getResponse.typeAsString());
                            model.setName(getResponse.name());
                            model.setDescription(getResponse.description());
                            model.setDestinationName(getResponse.destinationName());
                            model.setLoRaWAN(Translator.translateToLoRaWANDeviceSDK(getResponse.loRaWAN()));
                            model.setThingArn(getResponse.thingArn());
                            model.setThingName(getResponse.thingName());
                            return ProgressEvent.progress(model, callbackContext);
                        })
                )
                .then(progress -> {
                    return ProgressEvent.defaultSuccessHandler(model);
                });
    }

    private GetWirelessDeviceResponse getResource(
            GetWirelessDeviceRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetWirelessDeviceResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getWirelessDevice);
        } catch (final Exception e) {
            throw handleException(e, getRequest);
        }
        return response;
    }
}
