package software.amazon.iotwireless.wirelessdevice;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.UpdateWirelessDeviceRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateWirelessDeviceResponse;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessDeviceWithThingRequest;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessDeviceWithThingResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

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
                        return proxy.initiate("AWS-IoTWireless-WirelessDevice::AssociateThing", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                .translateToServiceRequest(Translator::associateWirelessDeviceWithThing)
                                .makeServiceCall(this::associateThing)
                                .progress();
                    }
                    return progress;
                })
                .then(progress -> {
                    if (model.getName() != null || model.getDescription() != null || model.getId() != null || model.getLoRaWAN() != null) {
                        return proxy.initiate("AWS-IoTWireless-WirelessGateway::Update", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                .translateToServiceRequest(Translator::translateToFirstUpdateRequest)
                                .makeServiceCall(this::updateResource)
                                .progress();
                    }
                    return progress;
                })
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));
    }

    private UpdateWirelessDeviceResponse updateResource(
            UpdateWirelessDeviceRequest updateRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        UpdateWirelessDeviceResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::updateWirelessDevice);
        } catch (final Exception e) {
            throw handleException(e, updateRequest);
        }
        return response;
    }

    private AssociateWirelessDeviceWithThingResponse associateThing(
            AssociateWirelessDeviceWithThingRequest updateRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        AssociateWirelessDeviceWithThingResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::associateWirelessDeviceWithThing);
        } catch (final Exception e) {
            throw handleException(e, updateRequest);
        }
        return response;
    }
}
