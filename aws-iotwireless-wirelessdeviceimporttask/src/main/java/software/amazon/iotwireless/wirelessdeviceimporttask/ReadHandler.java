package software.amazon.iotwireless.wirelessdeviceimporttask;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessDeviceImportTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessDeviceImportTaskResponse;
import software.amazon.awssdk.services.iotwireless.model.ListTagsForResourceRequest;
import software.amazon.awssdk.services.iotwireless.model.ListTagsForResourceResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

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
                .then(progress -> proxy.initiate("AWS-IoTWireless-WirelessDeviceImportTask::Read", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::invokeReadAPI)
                        .done(response -> {
                            model.setId(response.id());
                            model.setArn(response.arn());
                            model.setDestinationName(response.destinationName());
                            //TODO: different name, have to change Instant to string
                            model.setCreationDate(String.valueOf(response.creationTime()));
                            model.setSidewalk(Translator.translateToSidewalkResourceModel(response.sidewalk()));
                            //TODO: different name
                            model.setStatus(response.statusAsString());
                            model.setStatusReason(response.statusReason());
                            //TODO: have to change long to int
                            model.setInitializedImportedDevicesCount(Math.toIntExact(response.initializedImportedDeviceCount()));
                            model.setPendingImportedDevicesCount(Math.toIntExact(response.pendingImportedDeviceCount()));
                            model.setOnboardedImportedDevicesCount(Math.toIntExact(response.onboardedImportedDeviceCount()));
                            model.setFailedImportedDevicesCount(Math.toIntExact(response.failedImportedDeviceCount()));
                            return ProgressEvent.progress(model, callbackContext);
                        })
                )
                .then(progress -> proxy.initiate("AWS-IoTWireless-WirelessDeviceImportTask::Read::TagList", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToListTagForResourceRequest)
                        .makeServiceCall(this::invokeListTagAPI)
                        .done(response -> {
                            model.setTags(response.tags().stream()
                                    .map(tag -> Tag.builder()
                                            .key(tag.key())
                                            .value(tag.value()).build())
                                    .collect(Collectors.toSet()));
                            return ProgressEvent.progress(model, callbackContext);
                        })
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));
    }

    private GetWirelessDeviceImportTaskResponse invokeReadAPI(GetWirelessDeviceImportTaskRequest request, ProxyClient<IotWirelessClient> proxyClient) {
        GetWirelessDeviceImportTaskResponse response;
        try {
            response = proxy.injectCredentialsAndInvokeV2(request, proxyClient.client()::getWirelessDeviceImportTask);
        } catch (final Exception e) {
            throw handleException(e);
        }
        logger.log(String.format("%s has successfully been read.", ResourceModel.TYPE_NAME));
        return response;
    }

    private ListTagsForResourceResponse invokeListTagAPI(ListTagsForResourceRequest request, ProxyClient<IotWirelessClient> proxyClient) {
        ListTagsForResourceResponse response = null;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::listTagsForResource);

        } catch (final Exception e) {
            throw handleException(e);
        }

        return response;
    }
}