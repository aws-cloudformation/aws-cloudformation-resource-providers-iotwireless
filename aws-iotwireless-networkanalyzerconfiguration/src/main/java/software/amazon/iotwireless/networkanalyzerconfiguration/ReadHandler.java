package software.amazon.iotwireless.networkanalyzerconfiguration;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.GetNetworkAnalyzerConfigurationRequest;
import software.amazon.awssdk.services.iotwireless.model.GetNetworkAnalyzerConfigurationResponse;
import software.amazon.awssdk.services.iotwireless.model.ListTagsForResourceRequest;
import software.amazon.awssdk.services.iotwireless.model.ListTagsForResourceResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ReadHandler extends BaseHandlerStd {
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
        final ResourceModel model = request.getDesiredResourceState();

        return ProgressEvent.progress(model,callbackContext)
                .then(progress -> proxy.initiate("AWS-IoTWireless-NetworkAnalyzerConfiguration::Read", proxyClient, model, callbackContext)
                    .translateToServiceRequest(Translator::translateToReadRequest)
                    .makeServiceCall(this::invokeReadAPI)
                    .done(response -> {
                        model.setTraceContent(Translator.translateToTraceContentResourceModel(response.traceContent()));
                        model.setWirelessDevices(response.wirelessDevices());
                        model.setWirelessGateways(response.wirelessGateways());
                        model.setDescription(response.description());
                        model.setArn(response.arn());
                        return ProgressEvent.progress(model,callbackContext);
                    })
                 )
                .then(progress -> proxy.initiate("AWS-IoTWireless-NetworkAnalyzerConfiguration::Read::TagList", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToListTagForResourceRequest)
                        .makeServiceCall(this::invokeListTagAPI)
                        .done(response->{
                                model.setTags(response.tags().stream()
                                        .map(tag ->
                                                software.amazon.iotwireless.networkanalyzerconfiguration.Tag.builder()
                                                        .key(tag.key())
                                                        .value(tag.value()).build())
                                        .collect(Collectors.toSet()));
                                return ProgressEvent.progress(model,callbackContext);
                        })
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));
    }

    private  GetNetworkAnalyzerConfigurationResponse invokeReadAPI(GetNetworkAnalyzerConfigurationRequest request,
                                                                 ProxyClient<IotWirelessClient> proxyClient) {
        GetNetworkAnalyzerConfigurationResponse response= null;
        try {
            response= proxy.injectCredentialsAndInvokeV2(
                    request, proxyClient.client()::getNetworkAnalyzerConfiguration);
        } catch (final Exception e) {
            throw  handleException(e);
        }

        logger.log(String.format("%s has successfully been read.", ResourceModel.TYPE_NAME));
        return response;
    }

    private  ListTagsForResourceResponse invokeListTagAPI(ListTagsForResourceRequest request,
                                                                   ProxyClient<IotWirelessClient> proxyClient) {

        ListTagsForResourceResponse response= null;
        try {
            response= proxy.injectCredentialsAndInvokeV2(
                    request, proxyClient.client()::listTagsForResource);

        } catch (final Exception e) {
            throw  handleException(e);
        }

        logger.log(String.format("%s's tags has successfully been read.", ResourceModel.TYPE_NAME));
        return response;
    }
}
