package software.amazon.iotwireless.fuotatask;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.DeleteFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteFuotaTaskResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class DeleteHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();
        return ProgressEvent.progress(model, callbackContext)
                .then(progress -> proxy.initiate("AWS-IoTWireless-FuotaTask::Delete", proxyClient, model, progress.getCallbackContext())
                        .translateToServiceRequest(Translator::translateToDeleteRequest)
                        .makeServiceCall(this::deleteResource)
                        .progress()
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(null));
    }

    private DeleteFuotaTaskResponse deleteResource(
            DeleteFuotaTaskRequest deleteRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        DeleteFuotaTaskResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(deleteRequest, proxyClient.client()::deleteFuotaTask);
        } catch (final Exception e) {
            throw handleException(e, deleteRequest);
        }
        return response;
    }
}