package software.amazon.iotwireless.taskdefinition;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessGatewayTaskDefinitionRequest;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessGatewayTaskDefinitionResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class ReadHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress -> proxy.initiate("AWS-IoTWireless-TaskDefinition::Read", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::getResource)
                        .done(getResponse -> {
                            model.setAutoCreateTasks(getResponse.autoCreateTasks());
                            model.setName(getResponse.name());
                            model.setUpdate(Translator.translateToUpdate(getResponse.update()));
                            model.setArn(getResponse.arn());
                            return ProgressEvent.progress(model, callbackContext);
                        })
                )
                .then(progress -> {
                    return ProgressEvent.defaultSuccessHandler(model);
                });

    }

    private GetWirelessGatewayTaskDefinitionResponse getResource(
            GetWirelessGatewayTaskDefinitionRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetWirelessGatewayTaskDefinitionResponse response = null;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getWirelessGatewayTaskDefinition);
        } catch (final Exception e) {
            throw handleException(e, getRequest);
        }
        return response;
    }
}
