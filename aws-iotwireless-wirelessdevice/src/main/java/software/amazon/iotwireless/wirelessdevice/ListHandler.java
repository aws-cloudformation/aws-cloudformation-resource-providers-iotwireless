package software.amazon.iotwireless.wirelessdevice;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessDevicesRequest;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessDevicesResponse;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ProgressEvent;
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
        ResourceModel model = request.getDesiredResourceState();

        final ListWirelessDevicesRequest listWirelessDevicesRequest = Translator.translateToListRequest(request.getNextToken());

        try {
            ListWirelessDevicesResponse listWirelessDevicesResponse = proxy.injectCredentialsAndInvokeV2(listWirelessDevicesRequest, proxyClient.client()::listWirelessDevices);

            final List<ResourceModel> models = listWirelessDevicesResponse.wirelessDeviceList().stream()
                    .map(device -> ResourceModel.builder()
                            .type(device.typeAsString())
                            .name(device.name())
                            .id(device.id())
                            .destinationName(device.destinationName())
                            .loRaWAN(Translator.translateToLoRaWAN(device.loRaWAN()))
                            .arn(device.arn())
                            .lastUplinkReceivedAt(device.lastUplinkReceivedAt())
                            .build())
                    .collect(Collectors.toList());

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModels(models)
                    .nextToken(listWirelessDevicesResponse.nextToken())
                    .status(OperationStatus.SUCCESS)
                    .build();
        } catch (final Exception e) {
            throw handleException(e, listWirelessDevicesRequest);
        }
    }
}
