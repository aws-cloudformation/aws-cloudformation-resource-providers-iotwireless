package software.amazon.iotwireless.serviceprofile;


import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.GetServiceProfileRequest;
import software.amazon.awssdk.services.iotwireless.model.GetServiceProfileResponse;
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

        return ProgressEvent.progress(model, callbackContext)
                .then(progress -> proxy.initiate("AWS-IoTWireless-ServiceProfile::Read", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::getResource)
                        .handleError((deleteServiceProfileRequest, exception, client, resourceModel, context) -> {
                            if (exception instanceof ResourceNotFoundException) {
                                return ProgressEvent.defaultFailureHandler(exception, HandlerErrorCode.NotFound);
                            }
                            throw exception;
                        })
                        .done(getResponse -> {
                            model.setId(getResponse.id());
                            model.setArn(getResponse.arn());
                            model.setName(getResponse.name());
                            model.setLoRaWANGetServiceProfileInfo(Translator.translateFromLoRaSDK(getResponse.loRaWAN()));
                            return ProgressEvent.progress(model, callbackContext);
                        })
                )
                .then(progress -> {
                    return ProgressEvent.defaultSuccessHandler(Translator.unsetWriteOnly(model));
                });
    }

    private GetServiceProfileResponse getResource(
            GetServiceProfileRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetServiceProfileResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getServiceProfile);
        } catch (final ResourceNotFoundException e) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, getRequest.id());
        } catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;
    }
}
