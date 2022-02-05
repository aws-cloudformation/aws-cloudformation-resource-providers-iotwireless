package software.amazon.iotwireless.fuotatask;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.CreateFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.CreateFuotaTaskResponse;
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
                        proxy.initiate("AWS-IoTWireless-FuotaTask::Create", proxyClient, model, callbackContext)
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

    private CreateFuotaTaskResponse createResource(
            CreateFuotaTaskRequest createRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        CreateFuotaTaskResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(createRequest, proxyClient.client()::createFuotaTask);
        } catch (final Exception e) {
            throw handleException(e, createRequest);
        }
        return response;
    }
}