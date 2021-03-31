package software.amazon.iotwireless.partneraccount;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AssociateAwsAccountWithPartnerAccountRequest;
import software.amazon.awssdk.services.iotwireless.model.AssociateAwsAccountWithPartnerAccountResponse;
import software.amazon.awssdk.services.iotwireless.model.GetPartnerAccountRequest;
import software.amazon.awssdk.services.iotwireless.model.GetPartnerAccountResponse;
import software.amazon.awssdk.services.iotwireless.model.ConflictException;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.exceptions.ResourceAlreadyExistsException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class CreateHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();
        final ResourceModel previousModel = request.getPreviousResourceState();
        String clientRequestToken = request.getClientRequestToken();

        if ( model.getArn() != null || model.getSidewalkResponse() != null ) {
            throw new CfnInvalidRequestException("Attempting to set a ReadOnly Property.");
        }

        if (previousModel != null) {
            if (previousModel.getPartnerAccountId().equals(model.getPartnerAccountId())) {
                throw new ResourceAlreadyExistsException(ResourceModel.TYPE_NAME, model.getPartnerAccountId());
            }
        }

        return ProgressEvent.progress(model, callbackContext)
                .then(progress -> proxy.initiate("AWS-IoTWireless-PartnerAccount::Read", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::getResource)
                        .progress())
                .then(progress ->
                        proxy.initiate("AWS-IoTWireless-PartnerAccount::Create", proxyClient, model, callbackContext)
                                .translateToServiceRequest(resourceModel -> Translator.translateToCreateRequest(resourceModel, clientRequestToken))
                                .makeServiceCall(this::createResource)
                                .done((response) -> {
                                    model.setSidewalk(Translator.translateToSidewalk(response.sidewalk()));
                                    model.setPartnerAccountId(response.sidewalk().amazonId());
                                    model.setArn(response.arn());
                                    return progress;
                                }))
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));
    }

    private AssociateAwsAccountWithPartnerAccountResponse createResource(
            AssociateAwsAccountWithPartnerAccountRequest createRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        AssociateAwsAccountWithPartnerAccountResponse response = null;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(createRequest, proxyClient.client()::associateAwsAccountWithPartnerAccount);
        } catch (final Exception e) {
            throw handleException(e, createRequest);
        }
        return response;
    }

    private GetPartnerAccountResponse getResource(
            GetPartnerAccountRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetPartnerAccountResponse response = null;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getPartnerAccount);
        } catch (final ResourceNotFoundException e) {
        }
        if (response != null) {
            throw new ResourceAlreadyExistsException(ResourceModel.TYPE_NAME, "Resource Already Exists");
        }
        return response;
    }
}
