package software.amazon.iotwireless.deviceprofile;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.CreateDeviceProfileRequest;
import software.amazon.awssdk.services.iotwireless.model.CreateDeviceProfileResponse;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.proxy.*;

public class CreateHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        ResourceModel model = request.getDesiredResourceState();
        String clientRequestToken = request.getClientRequestToken();

        if ( model.getArn() != null ) {
            throw new CfnInvalidRequestException("Attempting to set a ReadOnly Property.");
        }

        return ProgressEvent.progress(model, callbackContext)
                .then(progress ->
                        proxy.initiate("AWS-IoTWireless-DeviceProfile::Create", proxyClient, model, callbackContext)
                                .translateToServiceRequest(resourceModel -> Translator.translateToCreateRequest(resourceModel, clientRequestToken))
                                .makeServiceCall(this::createResource)
                                .done((response) -> {
                                    model.setId(response.id());
                                    model.setArn(response.arn());
                                    return progress;
                                }))
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.unsetWriteOnly(model)));
    }

    private CreateDeviceProfileResponse createResource(
            CreateDeviceProfileRequest createRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        CreateDeviceProfileResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(createRequest, proxyClient.client()::createDeviceProfile);
        } catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;
    }
}
