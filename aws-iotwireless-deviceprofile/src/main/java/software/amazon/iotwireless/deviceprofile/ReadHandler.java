package software.amazon.iotwireless.deviceprofile;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.GetDeviceProfileRequest;
import software.amazon.awssdk.services.iotwireless.model.GetDeviceProfileResponse;
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
                .then(progress ->
                        proxy.initiate("AWS-IoTWireless-DeviceProfile::Read", proxyClient, model, callbackContext)
                                .translateToServiceRequest(Translator::translateToReadRequest)
                                .makeServiceCall(this::getResource)
                                .done((response) -> {
                                    model.setId(response.id());
                                    model.setArn(response.arn());
                                    model.setName(response.name());
                                    model.setLoRaWANDeviceProfile(Translator.translateFromLoRaSDK(response.loRaWAN()));
                                    return progress;
                                }))
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.unsetWriteOnly(model)));
    }

    private GetDeviceProfileResponse getResource(
            GetDeviceProfileRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetDeviceProfileResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getDeviceProfile);
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
