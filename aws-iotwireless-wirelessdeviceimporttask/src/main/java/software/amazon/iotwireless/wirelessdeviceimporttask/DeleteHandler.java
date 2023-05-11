package software.amazon.iotwireless.wirelessdeviceimporttask;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.DeleteWirelessDeviceImportTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteWirelessDeviceImportTaskResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class DeleteHandler extends BaseHandlerStd {
    private Logger logger;
    private AmazonWebServicesClientProxy proxy;

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        this.logger = logger;
        this.proxy = proxy;

        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress ->
                        proxy.initiate("AWS-IoTWireless-WirelessDeviceImportTask::Delete", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                .translateToServiceRequest(Translator::translateToDeleteRequest)
                                .makeServiceCall(this::invokeDeleteAPI)
                                .progress()
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(null ));
    }

    private DeleteWirelessDeviceImportTaskResponse invokeDeleteAPI(DeleteWirelessDeviceImportTaskRequest request, ProxyClient<IotWirelessClient> proxyClient) {
        DeleteWirelessDeviceImportTaskResponse response;
        try {
            response = proxy.injectCredentialsAndInvokeV2(request,proxyClient.client()::deleteWirelessDeviceImportTask);
        } catch (final Exception e) {
            throw  handleException(e);
        }

        logger.log(String.format("%s successfully deleted.", ResourceModel.TYPE_NAME));
        return response;
    }
}