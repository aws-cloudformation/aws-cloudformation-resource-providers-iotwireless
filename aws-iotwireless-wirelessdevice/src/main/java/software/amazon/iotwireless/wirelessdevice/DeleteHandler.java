package software.amazon.iotwireless.wirelessdevice;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.*;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.proxy.*;

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
        } catch (final ResourceNotFoundException e) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, deleteRequest.id());
        } catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;
    }

    private DisassociateWirelessDeviceFromThingResponse disassociateThing(
            final DisassociateWirelessDeviceFromThingRequest deleteRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        DisassociateWirelessDeviceFromThingResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(deleteRequest, proxyClient.client()::disassociateWirelessDeviceFromThing);
        } catch (final ResourceNotFoundException e) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, deleteRequest.id());
        }  catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;
    }
}
