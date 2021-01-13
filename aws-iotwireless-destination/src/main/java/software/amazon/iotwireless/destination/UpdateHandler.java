package software.amazon.iotwireless.destination;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.UpdateDestinationRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateDestinationResponse;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.exceptions.CfnNotUpdatableException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.HandlerErrorCode;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class UpdateHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();
        final ResourceModel previousModel = request.getPreviousResourceState();
        // Make sure the user isn't trying to change readOnly or createOnly properties
        if (previousModel != null) {
            if (!previousModel.getArn().equals(model.getArn()) ||
                    !previousModel.getName().equals(model.getName())) {
                throw new CfnNotUpdatableException(ResourceModel.TYPE_NAME, model.getArn());
            }
        }

        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress -> proxy.initiate("AWS-IoTWireless-Destination::Update", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                        .translateToServiceRequest(Translator::translateToFirstUpdateRequest)
                        .makeServiceCall(this::updateResource)
                        .progress()
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(model));
    }

    private UpdateDestinationResponse updateResource(
            UpdateDestinationRequest updateRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        UpdateDestinationResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::updateDestination);
        } catch (final Exception e) {
            throw handleException(e, updateRequest);
        }
        return response;

    }
}
