package software.amazon.iotwireless.fuotatask;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.ListFuotaTasksRequest;
import software.amazon.awssdk.services.iotwireless.model.ListFuotaTasksResponse;
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

        final ListFuotaTasksRequest listFuotaTasksRequest = Translator.translateToListRequest(request.getNextToken());

        try {
            ListFuotaTasksResponse listFuotaTasksResponse = proxy.injectCredentialsAndInvokeV2(listFuotaTasksRequest, proxyClient.client()::listFuotaTasks);

            final List<ResourceModel> models = listFuotaTasksResponse.fuotaTaskList().stream()
                    .map(fuotaTask -> ResourceModel.builder()
                            .arn(fuotaTask.arn())
                            .id(fuotaTask.id())
                            .name(fuotaTask.name())
                            .build())
                    .collect(Collectors.toList());

            String nextToken = listFuotaTasksResponse.nextToken();
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModels(models)
                    .nextToken(nextToken)
                    .status(OperationStatus.SUCCESS)
                    .build();
        } catch (Exception e) {
            throw handleException(e, listFuotaTasksRequest);
        }
    }
}
