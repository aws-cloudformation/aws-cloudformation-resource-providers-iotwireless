package software.amazon.iotwireless.wirelessgateway;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessGatewaysRequest;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessGatewaysResponse;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.proxy.*;

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

        try {
            ListWirelessGatewaysResponse listWirelessGatewaysResponse = proxyClient.injectCredentialsAndInvokeV2(Translator.translateToListRequest(
                    request.getNextToken()), proxyClient.client()::listWirelessGateways);

            final List<ResourceModel> models = listWirelessGatewaysResponse.wirelessGatewayList().stream()
                    .map(wirelessGateway -> ResourceModel.builder().id(wirelessGateway.id()).build())
                    .collect(Collectors.toList());

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModels(models)
                    .nextToken(listWirelessGatewaysResponse.nextToken())
                    .status(OperationStatus.SUCCESS)
                    .build();
        } catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
    }
}
