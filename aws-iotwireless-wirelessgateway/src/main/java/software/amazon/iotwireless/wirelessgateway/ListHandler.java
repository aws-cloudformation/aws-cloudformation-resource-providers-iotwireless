package software.amazon.iotwireless.wirelessgateway;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessGatewaysRequest;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessGatewaysResponse;
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

        final ListWirelessGatewaysRequest listWirelessGatewaysRequest = Translator.translateToListRequest(request.getNextToken());

        try {
            ListWirelessGatewaysResponse listWirelessGatewaysResponse = proxy.injectCredentialsAndInvokeV2(listWirelessGatewaysRequest, proxyClient.client()::listWirelessGateways);

            final List<ResourceModel> models = listWirelessGatewaysResponse.wirelessGatewayList().stream()
                    .map(gateway -> ResourceModel.builder()
                            .name(gateway.name())
                            .id(gateway.id())
                            .description(gateway.description())
                            .loRaWAN(Translator.translateToLoRaWAN(gateway.loRaWAN()))
                            .lastUplinkReceivedAt(gateway.lastUplinkReceivedAt())
                            .arn(gateway.arn())
                            .build())
                    .collect(Collectors.toList());

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModels(models)
                    .nextToken(listWirelessGatewaysResponse.nextToken())
                    .status(OperationStatus.SUCCESS)
                    .build();
        } catch (final Exception e) {
            throw handleException(e, listWirelessGatewaysRequest);
        }
    }
}
