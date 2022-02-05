package software.amazon.iotwireless.multicastgroup;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.CreateMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.CreateMulticastGroupResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class CreateHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        ResourceModel model = request.getDesiredResourceState();
        String clientRequestToken = request.getClientRequestToken();

        return ProgressEvent.progress(model, callbackContext)
                .then(progress ->
                        proxy.initiate("AWS-IoTWireless-MulticastGroup::Create", proxyClient, model, callbackContext)
                                .translateToServiceRequest(resourceModel -> Translator.translateToCreateRequest(resourceModel, clientRequestToken))
                                .makeServiceCall(this::createResource)
                                .done((response) -> {
                                    model.setArn(response.arn());
                                    model.setId(response.id());
                                    return progress;
                                })
                )
                .then(progress ->
                        ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));
    }

    private CreateMulticastGroupResponse createResource(
            CreateMulticastGroupRequest createRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        CreateMulticastGroupResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(createRequest, proxyClient.client()::createMulticastGroup);
        } catch (final Exception e) {
            throw handleException(e, createRequest);
        }
        return response;
    }
}