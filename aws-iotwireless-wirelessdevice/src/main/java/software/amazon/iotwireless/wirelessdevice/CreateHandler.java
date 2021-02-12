package software.amazon.iotwireless.wirelessdevice;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.CreateWirelessDeviceRequest;
import software.amazon.awssdk.services.iotwireless.model.CreateWirelessDeviceResponse;
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

        if (model.getTags() != null) {
            logger.log(String.format("ERROR: Currently does not support tags, resource not created."));
            throw new IllegalArgumentException("Currently does not support tags, resource not created.");
        }
        return ProgressEvent.progress(model, callbackContext)
                .then(progress ->
                        proxy.initiate("AWS-IoTWireless-WirelessDevice::Create", proxyClient, model, callbackContext)
                                .translateToServiceRequest(resourceModel -> Translator.translateToCreateRequest(resourceModel, clientRequestToken))
                                .makeServiceCall(this::createResource)
                                .done((response) -> {
                                    model.setId(response.id());
                                    model.setArn(response.arn());
                                    return progress;
                                }))
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));
    }

    private CreateWirelessDeviceResponse createResource(
            CreateWirelessDeviceRequest createRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        CreateWirelessDeviceResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(createRequest, proxyClient.client()::createWirelessDevice);
        } catch (final Exception e) {
            throw handleException(e, createRequest);
        }
        return response;
    }
}
