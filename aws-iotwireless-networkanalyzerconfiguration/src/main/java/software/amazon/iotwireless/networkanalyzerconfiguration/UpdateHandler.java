package software.amazon.iotwireless.networkanalyzerconfiguration;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.UpdateNetworkAnalyzerConfigurationRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateNetworkAnalyzerConfigurationResponse;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class UpdateHandler extends BaseHandlerStd {
    private Logger logger;
    private ResourceHandlerRequest<ResourceModel> resourceHandlerRequest;
    private AmazonWebServicesClientProxy proxy;

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final ProxyClient<IotWirelessClient> proxyClient,
        final Logger logger) {

        this.logger = logger;
        this.resourceHandlerRequest= request;
        this.proxy = proxy;

        final ResourceModel targetedState = request.getDesiredResourceState();
        if(request.getPreviousResourceState() == null) throw new CfnInvalidRequestException("PreviousResourceState is required.");

        return ProgressEvent.progress(targetedState, callbackContext)
            .then(progress ->
                proxy.initiate("AWS-IoTWireless-NetworkAnalyzerConfiguration::Update", proxyClient, targetedState, progress.getCallbackContext())
                    .translateToServiceRequest(resourceModel -> Translator.translateToUpdateRequest(request))
                    .makeServiceCall(this::invokeUpdateAPI)
                    .progress())
            .then(progress -> ProgressEvent.defaultSuccessHandler(request.getDesiredResourceState()));
    }

    private UpdateNetworkAnalyzerConfigurationResponse invokeUpdateAPI(UpdateNetworkAnalyzerConfigurationRequest request, ProxyClient<IotWirelessClient> proxyClient){
        UpdateNetworkAnalyzerConfigurationResponse response = null;

        try {
            response = proxy.injectCredentialsAndInvokeV2(
                    request, proxyClient.client()::updateNetworkAnalyzerConfiguration);
        } catch (final Exception e) {
            throw handleException(e);
        }

        logger.log(String.format("%s has successfully been updated.", ResourceModel.TYPE_NAME));
        return response;
    }
}
