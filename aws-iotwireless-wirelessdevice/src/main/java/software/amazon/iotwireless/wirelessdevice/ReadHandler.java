package software.amazon.iotwireless.wirelessdevice;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessDeviceRequest;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessDeviceResponse;
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
                .then(progress -> proxy.initiate("AWS-IoTWireless-WirelessDevice::Read", proxyClient, request.getDesiredResourceState(), callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::getResource)
                        .done(getResponse -> {
                            model.setId(getResponse.id());
                            model.setArn(getResponse.arn());
                            model.setType(getResponse.typeAsString());
                            model.setName(getResponse.name());
                            model.setDescription(getResponse.description());
                            model.setDestinationName(getResponse.destinationName());
                            model.setLoRaWANDevice(Translator.translateToLoRaWANDeviceSDK(getResponse.loRaWAN()));
                            model.setThingArn(getResponse.thingArn());
                            model.setThingName(getResponse.thingName());
                            return ProgressEvent.progress(model, callbackContext);
                        })
                )
                .then(progress -> {
                    return ProgressEvent.defaultSuccessHandler(Translator.unsetWriteOnly(model));
                });
    }

    private GetWirelessDeviceResponse getResource(
            GetWirelessDeviceRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetWirelessDeviceResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getWirelessDevice);
        } catch (final ResourceNotFoundException e) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, getRequest.identifier());
        } catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;
    }
}
