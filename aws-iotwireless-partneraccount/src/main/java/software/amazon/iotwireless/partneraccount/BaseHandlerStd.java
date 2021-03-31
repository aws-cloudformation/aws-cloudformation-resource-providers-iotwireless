package software.amazon.iotwireless.partneraccount;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;

import software.amazon.awssdk.services.iotwireless.model.GetPartnerAccountRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdatePartnerAccountRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateAwsAccountFromPartnerAccountRequest;
import software.amazon.awssdk.services.iotwireless.model.AssociateAwsAccountWithPartnerAccountRequest;
import software.amazon.awssdk.services.iotwireless.model.IotWirelessRequest;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.ConflictException;
import software.amazon.awssdk.services.iotwireless.model.InternalServerException;
import software.amazon.awssdk.services.iotwireless.model.ThrottlingException;
import software.amazon.awssdk.services.iotwireless.model.ValidationException;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.ResourceAlreadyExistsException;
import software.amazon.cloudformation.exceptions.CfnInternalFailureException;
import software.amazon.cloudformation.exceptions.CfnThrottlingException;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;
import software.amazon.cloudformation.proxy.ProgressEvent;

public abstract class BaseHandlerStd extends BaseHandler<CallbackContext> {

    public enum Type
    {
        Sidewalk;
    }

    @Override
    public final ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {
        return handleRequest(
                proxy,
                request,
                callbackContext != null ? callbackContext : new CallbackContext(),
                proxy.newProxy(ClientBuilder::getClient),
                logger
        );
    }

    protected abstract ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger);

    private String extractResourceIdFromRequests(final Object request) {
        if (request instanceof DisassociateAwsAccountFromPartnerAccountRequest) {
            final DisassociateAwsAccountFromPartnerAccountRequest disassociateAwsAccountFromPartnerAccountRequest =
                    (DisassociateAwsAccountFromPartnerAccountRequest) request;
            return disassociateAwsAccountFromPartnerAccountRequest.partnerAccountId();
        } else if (request instanceof AssociateAwsAccountWithPartnerAccountRequest) {
            final AssociateAwsAccountWithPartnerAccountRequest associatePartnerAccountRequest = (AssociateAwsAccountWithPartnerAccountRequest) request;
            return associatePartnerAccountRequest.sidewalk().amazonId();
        } else if (request instanceof GetPartnerAccountRequest) {
            final GetPartnerAccountRequest getPartnerAccountRequest = (GetPartnerAccountRequest) request;
            return getPartnerAccountRequest.partnerAccountId();
        } else if (request instanceof UpdatePartnerAccountRequest) {
            final UpdatePartnerAccountRequest updatePartnerAccountRequest = (UpdatePartnerAccountRequest) request;
            return updatePartnerAccountRequest.partnerAccountId();
        } else {
            return "";
        }
    }

    public RuntimeException handleException(final Exception error, final IotWirelessRequest request) {
        if (error instanceof ResourceNotFoundException) {
            return new CfnNotFoundException(ResourceModel.TYPE_NAME, extractResourceIdFromRequests(request));
        } if (error instanceof ConflictException) {
            return new ResourceAlreadyExistsException(error);
        } if (error instanceof InternalServerException) {
            return new CfnInternalFailureException(error);
        } if (error instanceof ThrottlingException) {
            return new CfnThrottlingException(ResourceModel.TYPE_NAME, error);
        } if (error instanceof ValidationException) {
            return new CfnInvalidRequestException(ResourceModel.TYPE_NAME, error);
        } if (error instanceof AccessDeniedException) {
            return new CfnAccessDeniedException(ResourceModel.TYPE_NAME, error);
        } else {
            return new CfnGeneralServiceException(ResourceModel.TYPE_NAME, error);
        }
    }
}