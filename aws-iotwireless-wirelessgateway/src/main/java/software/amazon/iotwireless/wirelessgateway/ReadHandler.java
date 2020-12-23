package software.amazon.iotwireless.wirelessgateway;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessGatewayRequest;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessGatewayResponse;
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
                .then(progress -> proxy.initiate("AWS-IoTWireless-WirelessGateway::Read", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::getResource)
                        .done(getResponse -> {
                            model.setId(getResponse.id());
                            model.setArn(getResponse.arn());
                            model.setName(getResponse.name());
                            model.setDescription(getResponse.description());
                            model.setLoRaWANGateway(Translator.translateToLoRaWAN(getResponse.loRaWAN()));
                            model.setThingArn(getResponse.thingArn());
                            model.setThingName(getResponse.thingName());
                            return ProgressEvent.progress(model, callbackContext);
                        })
                )
                .then(progress -> {
                    return ProgressEvent.defaultSuccessHandler(Translator.unsetWriteOnly(model));
                });
    }

    private GetWirelessGatewayResponse getResource(
            GetWirelessGatewayRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetWirelessGatewayResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getWirelessGateway);
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
