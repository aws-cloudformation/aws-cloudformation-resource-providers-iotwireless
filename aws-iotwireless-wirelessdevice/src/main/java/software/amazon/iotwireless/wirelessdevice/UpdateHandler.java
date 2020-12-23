package software.amazon.iotwireless.wirelessdevice;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.*;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.proxy.*;


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
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.unsetWriteOnly(model)));
    }

    private UpdateWirelessDeviceResponse updateResource(
            UpdateWirelessDeviceRequest updateRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        UpdateWirelessDeviceResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::updateWirelessDevice);
        } catch (final ResourceNotFoundException e) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, updateRequest.id());
        }  catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;
    }

    private AssociateWirelessDeviceWithThingResponse associateThing(
            AssociateWirelessDeviceWithThingRequest updateRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        AssociateWirelessDeviceWithThingResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::associateWirelessDeviceWithThing);
        } catch (final ResourceNotFoundException e) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, updateRequest.id());
        } catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;
    }
}
