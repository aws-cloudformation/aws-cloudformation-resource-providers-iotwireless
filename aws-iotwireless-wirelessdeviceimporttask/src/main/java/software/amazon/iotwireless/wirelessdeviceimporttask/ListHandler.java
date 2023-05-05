package software.amazon.iotwireless.wirelessdeviceimporttask;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessDeviceImportTasksRequest;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessDeviceImportTasksResponse;
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
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        final ListWirelessDeviceImportTasksRequest listWirelessDeviceImportTasksRequest = Translator.translateToListRequest(request.getNextToken());
        ListWirelessDeviceImportTasksResponse response;
        try{
            response = proxy.injectCredentialsAndInvokeV2(listWirelessDeviceImportTasksRequest, proxyClient.client()::listWirelessDeviceImportTasks);
        } catch (final Exception e) {
            throw handleException(e);
        }

        final String nextToken = response.nextToken();
        final List<ResourceModel> models = response.wirelessDeviceImportTaskList().stream()
                .map(config -> ResourceModel.builder()
                        .id(config.id())
                        .arn(config.arn())
                        .destinationName(config.destinationName())
                        //TODO: different name
                        .creationDate(String.valueOf(config.creationTime()))
                        .sidewalk(Translator.translateToSidewalkResourceModel(config.sidewalk()))
                        //TODO: different name
                        .status(config.statusAsString())
                        .statusReason(config.statusReason())
                        //TODO: have to change long to int
                        .initializedImportedDevicesCount(Math.toIntExact(config.initializedImportedDeviceCount()))
                        .pendingImportedDevicesCount(Math.toIntExact(config.pendingImportedDeviceCount()))
                        .onboardedImportedDevicesCount(Math.toIntExact(config.onboardedImportedDeviceCount()))
                        .failedImportedDevicesCount(Math.toIntExact(config.initializedImportedDeviceCount()))
                        .build())
                .collect(Collectors.toList());

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModels(models)
                .nextToken(nextToken)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}