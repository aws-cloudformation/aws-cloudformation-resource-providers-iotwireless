package software.amazon.iotwireless.networkanalyzerconfiguration;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.DeleteNetworkAnalyzerConfigurationRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteNetworkAnalyzerConfigurationResponse;
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
                proxy.initiate("AWS-IoTWireless-NetworkAnalyzerConfiguration::Delete", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                    .translateToServiceRequest(Translator::translateToDeleteRequest)
                    .makeServiceCall(this::invokeDeleteAPI)
                    .progress()
            )
           .then(progress -> ProgressEvent.defaultSuccessHandler(null ));
    }

    private DeleteNetworkAnalyzerConfigurationResponse invokeDeleteAPI(DeleteNetworkAnalyzerConfigurationRequest request, ProxyClient<IotWirelessClient> proxyClient) {
        DeleteNetworkAnalyzerConfigurationResponse response= null;
        try {
            proxy.injectCredentialsAndInvokeV2(request,proxyClient.client()::deleteNetworkAnalyzerConfiguration);
        } catch (final Exception e) {
            throw  handleException(e);
        }

        logger.log(String.format("%s successfully deleted.", ResourceModel.TYPE_NAME));
        return response;
    }
}
