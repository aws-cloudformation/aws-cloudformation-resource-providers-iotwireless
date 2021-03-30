package software.amazon.iotwireless.serviceprofile;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.CreateServiceProfileRequest;
import software.amazon.awssdk.services.iotwireless.model.CreateServiceProfileResponse;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
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

        ResourceModel model = request.getDesiredResourceState();
        String clientRequestToken = request.getClientRequestToken();

        if ( model.getArn() != null || model.getId() != null ) {
            throw new CfnInvalidRequestException("Attempting to set a ReadOnly Property.");
        }

        if ( model.getLoRaWAN() != null ) {
            if (model.getLoRaWAN().getUlRate() != null || model.getLoRaWAN().getUlBucketSize() != null ||
                model.getLoRaWAN().getUlRatePolicy() != null || model.getLoRaWAN().getDlRate() != null ||
                model.getLoRaWAN().getDlBucketSize() != null || model.getLoRaWAN().getDlRatePolicy() != null ||
                model.getLoRaWAN().getDevStatusReqFreq() != null || model.getLoRaWAN().getReportDevStatusBattery() != null ||
                model.getLoRaWAN().getReportDevStatusMargin() != null || model.getLoRaWAN().getDrMin() != null ||
                model.getLoRaWAN().getDrMax() != null || model.getLoRaWAN().getChannelMask() != null ||
                model.getLoRaWAN().getPrAllowed() != null || model.getLoRaWAN().getHrAllowed() != null ||
                model.getLoRaWAN().getRaAllowed() != null || model.getLoRaWAN().getNwkGeoLoc() != null ||
                model.getLoRaWAN().getTargetPer() != null || model.getLoRaWAN().getMinGwDiversity() != null ) {
                throw new CfnInvalidRequestException("Attempting to set a ReadOnly Property.");
            }
        }

        return ProgressEvent.progress(model, callbackContext)
                .then(progress ->
                        proxy.initiate("AWS-IoTWireless-ServiceProfile::Create", proxyClient, model, callbackContext)
                                .translateToServiceRequest(resourceModel -> Translator.translateToCreateRequest(resourceModel, clientRequestToken))
                                .makeServiceCall(this::createResource)
                                .done((response) -> {
                                    model.setId(response.id());
                                    model.setArn(response.arn());
                                    return progress;
                                }))
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));
    }

    private CreateServiceProfileResponse createResource(
            CreateServiceProfileRequest createRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        CreateServiceProfileResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(createRequest, proxyClient.client()::createServiceProfile);
        } catch (final Exception e) {
            throw handleException(e, createRequest);
        }
        return response;
    }
}
