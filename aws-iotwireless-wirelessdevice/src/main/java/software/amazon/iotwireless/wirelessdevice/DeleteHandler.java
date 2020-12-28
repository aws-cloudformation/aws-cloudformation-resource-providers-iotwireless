package software.amazon.iotwireless.wirelessdevice;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.DeleteWirelessDeviceRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteWirelessDeviceResponse;
import software.amazon.awssdk.services.iotwireless.model.DisassociateWirelessDeviceFromThingRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateWirelessDeviceFromThingResponse;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.HandlerErrorCode;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class DeleteHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(final AmazonWebServicesClientProxy proxy,
                                                                          final ResourceHandlerRequest<ResourceModel> request,
                                                                          final CallbackContext callbackContext,
                                                                          final ProxyClient<IotWirelessClient> proxyClient,
                                                                          final Logger logger) {
        final ResourceModel model = request.getDesiredResourceState();
        return ProgressEvent.progress(model, callbackContext)
                .then(progress -> proxy.initiate("IoTWirelessDevice::DisassociateThing", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToDisassociateRequest)
                        .makeServiceCall(this::disassociateThing)
                        .progress()
                )
                .then(progress -> proxy.initiate("IoTWirelessDevice::Delete", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToDeleteRequest)
                        .makeServiceCall(this::deleteResource)
                        .handleError((deleteDeviceRequest, exception, client, resourceModel, context) -> {
                            if (exception instanceof ResourceNotFoundException) {
                                return ProgressEvent.defaultFailureHandler(exception, HandlerErrorCode.NotFound);
                            }
                            throw exception;
                        })
                        .progress()
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(null));
    }

    private DeleteWirelessDeviceResponse deleteResource(
            final DeleteWirelessDeviceRequest deleteRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        DeleteWirelessDeviceResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(deleteRequest, proxyClient.client()::deleteWirelessDevice);
        } catch (final Exception e) {
            throw handleException(e, deleteRequest);
        }
        return response;
    }

    private DisassociateWirelessDeviceFromThingResponse disassociateThing(
            final DisassociateWirelessDeviceFromThingRequest deleteRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        DisassociateWirelessDeviceFromThingResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(deleteRequest, proxyClient.client()::disassociateWirelessDeviceFromThing);
        } catch (final Exception e) {
            throw handleException(e, deleteRequest);
        }
        return response;
    }
}