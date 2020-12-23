package software.amazon.iotwireless.serviceprofile;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.DeleteServiceProfileRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteServiceProfileResponse;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
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
        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress -> proxy.initiate("IoTServiceProfile::Delete", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToDeleteRequest)
                        .makeServiceCall(this::deleteResource)
                        .handleError((deleteServiceProfileRequest, exception, client, resourceModel, context) -> {
                            if (exception instanceof ResourceNotFoundException) {
                                return ProgressEvent.defaultFailureHandler(exception, HandlerErrorCode.NotFound);
                            }
                            throw exception;
                        })
                        .progress()
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(null));
    }

    private DeleteServiceProfileResponse deleteResource(
            final DeleteServiceProfileRequest deleteGatewayRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        DeleteServiceProfileResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(deleteGatewayRequest, proxyClient.client()::deleteServiceProfile);
        } catch (final ResourceNotFoundException e) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, deleteGatewayRequest.id());
        } catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;
    }
}
