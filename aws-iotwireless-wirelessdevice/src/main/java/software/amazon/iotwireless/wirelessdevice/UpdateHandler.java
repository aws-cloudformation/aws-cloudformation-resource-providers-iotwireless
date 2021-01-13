package software.amazon.iotwireless.wirelessdevice;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.UpdateWirelessDeviceRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateWirelessDeviceResponse;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessDeviceWithThingRequest;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessDeviceWithThingResponse;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.HandlerErrorCode;
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
                                .handleError((deleteDestinationRequest, exception, client, resourceModel, context) -> {
                                    if (exception instanceof ResourceNotFoundException) {
                                        return ProgressEvent.defaultFailureHandler(exception, HandlerErrorCode.NotFound);
                                    }
                                    throw exception;
                                })
                                .done(describeKeyResponse -> progress);
                    }
                    return progress;
                })
                .then(progress -> {
                    if (model.getName() != null || model.getDescription() != null || model.getId() != null) {
                        return proxy.initiate("AWS-IoTWireless-WirelessDevice::Update", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                .translateToServiceRequest(Translator::translateToFirstUpdateRequest)
                                .makeServiceCall(this::updateResource)
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
                    })
                .then(progress -> {
                        return ProgressEvent.defaultSuccessHandler(model);
                    });
                }
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
