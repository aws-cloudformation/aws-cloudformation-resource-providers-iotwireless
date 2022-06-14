package software.amazon.iotwireless.networkanalyzerconfiguration;

// TODO: replace all usage of SdkClient with your service client type, e.g; YourServiceAsyncClient
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.CreateNetworkAnalyzerConfigurationRequest;
import software.amazon.awssdk.services.iotwireless.model.CreateNetworkAnalyzerConfigurationResponse;
import software.amazon.awssdk.services.iotwireless.model.CreateWirelessGatewayTaskDefinitionResponse;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.Map;


public class CreateHandler extends BaseHandlerStd {
    private static final String AWS_TAG_PREFIX = "aws:" ;
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
        ResourceModel model = request.getDesiredResourceState();

        validateTag(TagHelper.generateTagsForCreate(request));
        return ProgressEvent.progress(model, callbackContext)
            .then(progress ->
                    {
                        return proxy.initiate("AWS-IoTWireless-NetworkAnalyzerConfiguration::Create", proxyClient, model, callbackContext)
                                .translateToServiceRequest(resourceModel -> Translator.translateToCreateRequest(request))
                                .makeServiceCall(this::invokeCreateAPI)
                                .done(response -> {
                                    model.setArn(response.arn());
                                    return progress;
                                });
                    }
                )
            .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));
    }

    private void validateTag(Map<String, String> tagList) {
        if (tagList == null) {
            return;
        }

        // Check for invalid requested system tags.
        for (String key : tagList.keySet()) {
            if (key.trim().toLowerCase().startsWith(AWS_TAG_PREFIX)) {
                throw new CfnInvalidRequestException(key + " is an invalid key. aws: prefixed tag key names cannot be requested.");
            }
        }
    }

    private CreateNetworkAnalyzerConfigurationResponse invokeCreateAPI(CreateNetworkAnalyzerConfigurationRequest request, ProxyClient<IotWirelessClient> proxyClient){
        CreateNetworkAnalyzerConfigurationResponse response= null;
        try {
            response = proxy.injectCredentialsAndInvokeV2(request,
                    proxyClient.client()::createNetworkAnalyzerConfiguration);
        } catch (final Exception e){
            throw handleException(e);
        }
        logger.log(String.format("%s successfully created.", ResourceModel.TYPE_NAME));
        return response;
    }
}
