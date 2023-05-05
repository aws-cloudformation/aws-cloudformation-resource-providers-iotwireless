package software.amazon.iotwireless.wirelessdeviceimporttask;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.ConflictException;
import software.amazon.awssdk.services.iotwireless.model.InternalServerException;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.awssdk.services.iotwireless.model.ThrottlingException;
import software.amazon.awssdk.services.iotwireless.model.ValidationException;
import software.amazon.cloudformation.exceptions.*;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public abstract class BaseHandlerStd extends BaseHandler<CallbackContext> {
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

    public RuntimeException handleException(final Exception error) {
        if (error instanceof ResourceNotFoundException) {
            return new CfnNotFoundException(error);
        } if (error instanceof ConflictException) {
            return new CfnAlreadyExistsException(error);
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
