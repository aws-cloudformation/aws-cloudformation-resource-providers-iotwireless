package software.amazon.iotwireless.networkanalyzerconfiguration;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.ListNetworkAnalyzerConfigurationsRequest;
import software.amazon.awssdk.services.iotwireless.model.ListNetworkAnalyzerConfigurationsResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ListHandler extends BaseHandlerStd {
    @Override
    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request,
                                                                          CallbackContext callbackContext, ProxyClient<IotWirelessClient> proxyClient,
                                                                          Logger logger) {
        // Construct a body of a request.
        final ListNetworkAnalyzerConfigurationsRequest listNetworkAnalyzerConfigurationsRequest
                = Translator.translateToListRequest(request.getNextToken());

        ListNetworkAnalyzerConfigurationsResponse response= null;
        try{
            response = proxy.injectCredentialsAndInvokeV2(
                    listNetworkAnalyzerConfigurationsRequest,
                    proxyClient.client()::listNetworkAnalyzerConfigurations);
        } catch (final Exception e) {
            throw handleException(e);
        }

        final List<ResourceModel> models = response.networkAnalyzerConfigurationList().stream()
                .map(config -> ResourceModel.builder()
                        .arn(config.arn())
                        .name(config.name())
                        .build())
                .collect(Collectors.toList());

        // Construct resource models
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModels(models)
                .nextToken(response.nextToken())
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
