package software.amazon.iotwireless.partneraccount;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.ListPartnerAccountsRequest;
import software.amazon.awssdk.services.iotwireless.model.ListPartnerAccountsResponse;
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

        final ListPartnerAccountsRequest listPartnerAccountsRequest = Translator.translateToListRequest(request.getNextToken());

        try {
            ListPartnerAccountsResponse listPartnerAccountsResponse = proxy.injectCredentialsAndInvokeV2(listPartnerAccountsRequest, proxyClient.client()::listPartnerAccounts);

            final List<ResourceModel> models = listPartnerAccountsResponse.sidewalk().stream()
                    .map(account -> ResourceModel.builder()
                            .partnerAccountId(account.amazonId())
                            .arn(account.arn())
                            .fingerprint(account.fingerprint())
                            .build())
                    .collect(Collectors.toList());

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModels(models)
                    .nextToken(listPartnerAccountsResponse.nextToken())
                    .status(OperationStatus.SUCCESS)
                    .build();
        } catch (final Exception e) {
            throw handleException(e, listPartnerAccountsRequest);
        }
    }
}
