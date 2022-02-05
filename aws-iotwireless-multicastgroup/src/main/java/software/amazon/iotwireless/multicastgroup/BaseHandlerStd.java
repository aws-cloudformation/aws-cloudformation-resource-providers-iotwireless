package software.amazon.iotwireless.multicastgroup;

import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AccessDeniedException;
import software.amazon.awssdk.services.iotwireless.model.ConflictException;
import software.amazon.awssdk.services.iotwireless.model.DeleteMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.GetMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.InternalServerException;
import software.amazon.awssdk.services.iotwireless.model.IotWirelessRequest;
import software.amazon.awssdk.services.iotwireless.model.ThrottlingException;
import software.amazon.awssdk.services.iotwireless.model.UpdateMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.ValidationException;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.CfnInternalFailureException;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.exceptions.CfnThrottlingException;
import software.amazon.cloudformation.exceptions.ResourceAlreadyExistsException;
import software.amazon.cloudformation.exceptions.ResourceNotFoundException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.function.Function;

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

    private String extractResourceIdFromMcRequests(final IotWirelessRequest request) {
        if (request instanceof DeleteMulticastGroupRequest) {
            final DeleteMulticastGroupRequest deleteMulticastGroupRequest = (DeleteMulticastGroupRequest) request;
            return deleteMulticastGroupRequest.id();
        }
        if (request instanceof GetMulticastGroupRequest) {
            final GetMulticastGroupRequest getMulticastGroupRequest = (GetMulticastGroupRequest) request;
            return getMulticastGroupRequest.id();
        }
        if (request instanceof UpdateMulticastGroupRequest) {
            final UpdateMulticastGroupRequest updateMulticastGroupRequest = (UpdateMulticastGroupRequest) request;
            return updateMulticastGroupRequest.id();
        } else {
            return "";
        }
    }

    public RuntimeException handleException(
            final Exception error,
            final IotWirelessRequest request,
            final String typeName,
            final Function<IotWirelessRequest, String> extractResourceIdFromRequests) {
        if (error instanceof ResourceNotFoundException) {
            return new CfnNotFoundException(typeName, extractResourceIdFromRequests.apply(request));
        }
        if (error instanceof ConflictException) {
            return new ResourceAlreadyExistsException(error);
        }
        if (error instanceof InternalServerException) {
            return new CfnInternalFailureException(error);
        }
        if (error instanceof ThrottlingException) {
            return new CfnThrottlingException(typeName, error);
        }
        if (error instanceof ValidationException) {
            return new CfnInvalidRequestException(typeName, error);
        }
        if (error instanceof AccessDeniedException) {
            return new CfnAccessDeniedException(typeName, error);
        }
        // Handle other kinds of error with status code
        if (error instanceof SdkServiceException) {
            switch (((SdkServiceException) error).statusCode()) {
                case 400:
                    return new CfnInvalidRequestException(typeName, error);
                case 403:
                    return new CfnAccessDeniedException(typeName, error);
                case 404:
                    return new CfnNotFoundException(typeName, extractResourceIdFromRequests.apply(request));
                case 409:
                    return new ResourceAlreadyExistsException(error);
                case 429:
                    return new CfnThrottlingException(typeName, error);
                case 500:
                    return new CfnInternalFailureException(error);
            }
        }
        return new CfnGeneralServiceException(typeName, error);
    }

    public RuntimeException handleException(final Exception error, final IotWirelessRequest request) {
        return handleException(error, request, ResourceModel.TYPE_NAME, this::extractResourceIdFromMcRequests);
    }
}
