package software.amazon.iotwireless.deviceprofile;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.GetDeviceProfileRequest;
import software.amazon.awssdk.services.iotwireless.model.GetDeviceProfileResponse;
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

        return ProgressEvent.progress(model, callbackContext)
                .then(progress ->
                        proxy.initiate("AWS-IoTWireless-DeviceProfile::Read", proxyClient, model, callbackContext)
                                .translateToServiceRequest(Translator::translateToReadRequest)
                                .makeServiceCall(this::getResource)
                                .done((response) -> {
                                    model.setId(response.id());
                                    model.setArn(response.arn());
                                    model.setName(response.name());
                                    model.setLoRaWAN(Translator.translateFromLoRaSDK(response.loRaWAN()));
                                    return ProgressEvent.progress(model, callbackContext);
                                }))
                .then(progress -> {
                    return ProgressEvent.defaultSuccessHandler(model);
                });
    }

    private GetDeviceProfileResponse getResource(
            GetDeviceProfileRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetDeviceProfileResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getDeviceProfile);
        } catch (final Exception e) {
            throw handleException(e, getRequest);
        }
        return response;
    }
}
