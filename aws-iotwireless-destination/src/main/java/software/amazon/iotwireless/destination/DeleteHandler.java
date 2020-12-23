package software.amazon.iotwireless.destination;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.DeleteDestinationRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteDestinationResponse;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;

import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.proxy.*;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;

public class DeleteHandler extends BaseHandlerStd {
    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(final AmazonWebServicesClientProxy proxy,
                                                                          final ResourceHandlerRequest<ResourceModel> request,
                                                                          final CallbackContext callbackContext,
                                                                          final ProxyClient<IotWirelessClient> proxyClient,
                                                                          final Logger logger) {
        final ResourceModel model = request.getDesiredResourceState();
        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress -> proxy.initiate("IoTDestination::Delete", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToDeleteRequest)
                        .makeServiceCall(this::deleteResource)
                        .handleError((deleteDestinationRequest, exception, client, resourceModel, context) -> {
                            if (exception instanceof ResourceNotFoundException) {
                                return ProgressEvent.defaultFailureHandler(exception, HandlerErrorCode.NotFound);
                            }
                            throw exception;
                        })
                        .progress()
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(null));
    }

    private DeleteDestinationResponse deleteResource(
            DeleteDestinationRequest deleteDestinationRequest,
            final  ProxyClient<IotWirelessClient> proxyClient) {
        DeleteDestinationResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(deleteDestinationRequest, proxyClient.client()::deleteDestination);
        } catch (final ResourceNotFoundException e) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, deleteDestinationRequest.name());
        }  catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException  e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;
    }
}
