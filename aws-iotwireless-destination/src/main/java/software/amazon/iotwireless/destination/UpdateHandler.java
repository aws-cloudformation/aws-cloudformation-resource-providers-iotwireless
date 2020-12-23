package software.amazon.iotwireless.destination;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.awssdk.services.iotwireless.model.UpdateDestinationRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateDestinationResponse;
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
                .then(progress -> proxy.initiate("AWS-IoTWireless-Destination::Update", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                        .translateToServiceRequest(Translator::translateToFirstUpdateRequest)
                        .makeServiceCall(this::updateResource)
                        .handleError((deleteDestinationRequest, exception, client, resourceModel, context) -> {
                            if (exception instanceof ResourceNotFoundException) {
                                return ProgressEvent.defaultFailureHandler(exception, HandlerErrorCode.NotFound);
                            }
                            throw exception;
                        })
                        .done(describeKeyResponse -> progress)
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.unsetWriteOnly(model)));
    }

    private UpdateDestinationResponse updateResource(
            UpdateDestinationRequest updateRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        UpdateDestinationResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::updateDestination);
        } catch (final ResourceNotFoundException e) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, updateRequest.name());
        }  catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;

    }
}
