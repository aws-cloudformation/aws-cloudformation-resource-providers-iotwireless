package software.amazon.iotwireless.multicastgroup;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.ListMulticastGroupsRequest;
import software.amazon.awssdk.services.iotwireless.model.ListMulticastGroupsResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ProgressEvent;
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

        final ListMulticastGroupsRequest listMulticastGroupsRequest = Translator.translateToListRequest(request.getNextToken());

        try {
            ListMulticastGroupsResponse listMulticastGroupsResponse = proxy.injectCredentialsAndInvokeV2(listMulticastGroupsRequest, proxyClient.client()::listMulticastGroups);

            final List<ResourceModel> models = listMulticastGroupsResponse.multicastGroupList().stream()
                    .map(multicastGroup -> ResourceModel.builder()
                            .arn(multicastGroup.arn())
                            .id(multicastGroup.id())
                            .name(multicastGroup.name())
                            .build())
                    .collect(Collectors.toList());

            String nextToken = listMulticastGroupsResponse.nextToken();
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModels(models)
                    .nextToken(nextToken)
                    .status(OperationStatus.SUCCESS)
                    .build();
        } catch (Exception e) {
            throw handleException(e, listMulticastGroupsRequest);
        }
    }
}