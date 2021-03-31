package software.amazon.iotwireless.partneraccount;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.DisassociateAwsAccountFromPartnerAccountRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateAwsAccountFromPartnerAccountResponse;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.HandlerErrorCode;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class DeleteHandler extends BaseHandlerStd {
    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(final AmazonWebServicesClientProxy proxy,
                                                                          final ResourceHandlerRequest<ResourceModel> request,
                                                                          final CallbackContext callbackContext,
                                                                          final ProxyClient<IotWirelessClient> proxyClient,
                                                                          final Logger logger) {
        final ResourceModel model = request.getDesiredResourceState();
        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress -> proxy.initiate("IoTPartnerAccount::Delete", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToDeleteRequest)
                        .makeServiceCall(this::deleteResource)
                        .progress()
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(null));
    }

    private DisassociateAwsAccountFromPartnerAccountResponse deleteResource(
            DisassociateAwsAccountFromPartnerAccountRequest deleteRequest,
            final  ProxyClient<IotWirelessClient> proxyClient) {
        DisassociateAwsAccountFromPartnerAccountResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(deleteRequest, proxyClient.client()::disassociateAwsAccountFromPartnerAccount);
        } catch (final Exception e) {
            throw handleException(e, deleteRequest);
        }
        return response;
    }
}

