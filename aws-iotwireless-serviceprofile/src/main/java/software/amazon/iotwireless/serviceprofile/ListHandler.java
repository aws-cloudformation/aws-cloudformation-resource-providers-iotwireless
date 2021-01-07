package software.amazon.iotwireless.serviceprofile;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.model.ListServiceProfilesRequest;
import software.amazon.awssdk.services.iotwireless.model.ListServiceProfilesResponse;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ListHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {
        ResourceModel model = request.getDesiredResourceState();

        final ListServiceProfilesRequest listServiceProfilesRequest = Translator.translateToListRequest(model, request.getNextToken());

        try {
            ListServiceProfilesResponse listServiceProfilesResponse = proxy.injectCredentialsAndInvokeV2(listServiceProfilesRequest, ClientBuilder.getClient()::listServiceProfiles);

            final List<ResourceModel> models = listServiceProfilesResponse.serviceProfileList().stream()
                    .map(serviceProfile -> ResourceModel.builder().id(serviceProfile.id()).build())
                    .collect(Collectors.toList());

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModels(models)
                    .nextToken(listServiceProfilesResponse.nextToken())
                    .status(OperationStatus.SUCCESS)
                    .build();
        } catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
    }
}