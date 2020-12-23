package software.amazon.iotwireless.destination;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.GetDestinationRequest;
import software.amazon.awssdk.services.iotwireless.model.GetDestinationResponse;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.proxy.*;

public class ReadHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress -> proxy.initiate("AWS-IoTWireless-Destination::Read", proxyClient, request.getDesiredResourceState(), callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::getResource)
                        .handleError((deleteDestinationRequest, exception, client, resourceModel, context) -> {
                            if (exception instanceof ResourceNotFoundException) {
                                return ProgressEvent.defaultFailureHandler(exception, HandlerErrorCode.NotFound);
                            }
                            throw exception;
                        })
                        .done(response -> {
                            model.setName(response.name());
                            model.setArn(response.arn());
                            model.setExpressionType(response.expressionTypeAsString());
                            model.setExpression(response.expression());
                            model.setDescription(response.description());
                            model.setRoleArn(response.roleArn());
                            return ProgressEvent.progress(model, callbackContext);
                        })
                )
                .then(progress -> {
                    return ProgressEvent.defaultSuccessHandler(Translator.unsetWriteOnly(model));
                });
    }

    private GetDestinationResponse getResource(
            GetDestinationRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetDestinationResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getDestination);
        } catch (final ResourceNotFoundException e) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, getRequest.name());
        } catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;
    }
}
