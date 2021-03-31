package software.amazon.iotwireless.partneraccount;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.GetPartnerAccountRequest;
import software.amazon.awssdk.services.iotwireless.model.GetPartnerAccountResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
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
                .then(progress -> proxy.initiate("AWS-IoTWireless-PartnerAccount::Read", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::getResource)
                        .done(response -> {
                            model.setSidewalkResponse(Translator.translateToSidewalkResponse(response.sidewalk()));
                            model.setAccountLinked(response.accountLinked());
                            return ProgressEvent.progress(model, callbackContext);
                        })
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(model));
    }

    private GetPartnerAccountResponse getResource(
            GetPartnerAccountRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetPartnerAccountResponse response = null;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getPartnerAccount);
        } catch (final Exception e) {
            throw handleException(e, getRequest);
        }
        return response;
    }
}
