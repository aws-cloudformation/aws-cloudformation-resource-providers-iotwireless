package software.amazon.iotwireless.destination;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.GetDestinationRequest;
import software.amazon.awssdk.services.iotwireless.model.GetDestinationResponse;
import software.amazon.awssdk.services.iotwireless.model.ResourceNotFoundException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.HandlerErrorCode;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class ReadHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress -> proxy.initiate("AWS-IoTWireless-Destination::Read", proxyClient, request.getDesiredResourceState(), callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::getResource)
                        .progress()
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));

    }

    private GetDestinationResponse getResource(
            GetDestinationRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetDestinationResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getDestination);
        } catch (final Exception e) {
            throw handleException(e, getRequest);
        }
        return response;
    }
}
