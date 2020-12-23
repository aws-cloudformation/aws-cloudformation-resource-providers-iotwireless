package software.amazon.iotwireless.wirelessgateway;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.*;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.proxy.*;


public class UpdateHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress -> {
                    if (model.getThingArn() != null) {
                        return proxy.initiate("AWS-IoTWireless-WirelessGateway::AssociateThing", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                .translateToServiceRequest(Translator::associateWirelessGatewayWithThing)
                                .makeServiceCall(this::associateThing)
                                .done(describeKeyResponse -> progress);
                    }
                    return progress;
                })
                .then(progress -> {
                    if (model.getName() != null || model.getDescription() != null || model.getId() != null) {
                        return proxy.initiate("AWS-IoTWireless-WirelessGateway::Update", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                .translateToServiceRequest(Translator::translateToFirstUpdateRequest)
                                .makeServiceCall(this::updateResource)
                                .done(describeKeyResponse -> progress);
                    }
                    return progress;
                })
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.unsetWriteOnly(model)));
    }

    private UpdateWirelessGatewayResponse updateResource(
            UpdateWirelessGatewayRequest updateRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        UpdateWirelessGatewayResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::updateWirelessGateway);
        } catch (final ResourceNotFoundException e) {
            throw new CfnNotFoundException(ResourceModel.TYPE_NAME, updateRequest.id());
        } catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;
    }

    private AssociateWirelessGatewayWithThingResponse associateThing(
            AssociateWirelessGatewayWithThingRequest updateRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        AssociateWirelessGatewayWithThingResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::associateWirelessGatewayWithThing);
        } catch (final AccessDeniedException e) {
            throw new CfnAccessDeniedException(ResourceModel.TYPE_NAME, e);
        } catch (final AwsServiceException e) {
            throw new CfnGeneralServiceException(ResourceModel.TYPE_NAME, e);
        }
        return response;
    }
}
