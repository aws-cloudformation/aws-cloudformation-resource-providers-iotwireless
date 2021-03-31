package software.amazon.iotwireless.taskdefinition;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessGatewayTaskDefinitionsRequest;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessGatewayTaskDefinitionsResponse;
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

        final ResourceModel model = request.getDesiredResourceState();

        final ListWirelessGatewayTaskDefinitionsRequest listWirelessGatewayTaskDefinitionsRequest =
                Translator.translateToListRequest(model, request.getNextToken());

        try {
            ListWirelessGatewayTaskDefinitionsResponse listWirelessGatewayTaskDefinitionsResponse =
                    proxy.injectCredentialsAndInvokeV2(listWirelessGatewayTaskDefinitionsRequest, proxyClient.client()::listWirelessGatewayTaskDefinitions);

            final List<ResourceModel> models = listWirelessGatewayTaskDefinitionsResponse.taskDefinitions().stream()
                    .map(taskdefinition -> ResourceModel.builder()
                            .id(taskdefinition.id())
                            .arn(taskdefinition.arn())
                            .loRaWANUpdateGatewayTaskEntry(Translator.translateLoRaWANUpdateGatewayTaskEntry(taskdefinition.loRaWAN()))
                            .build())
                    .collect(Collectors.toList());

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModels(models)
                    .nextToken(listWirelessGatewayTaskDefinitionsResponse.nextToken())
                    .status(OperationStatus.SUCCESS)
                    .build();
        } catch (final Exception e) {
            throw handleException(e, listWirelessGatewayTaskDefinitionsRequest);
        }
    }
}
