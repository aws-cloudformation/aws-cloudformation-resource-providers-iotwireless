package software.amazon.iotwireless.multicastgroup;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.GetMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.GetMulticastGroupResponse;
import software.amazon.awssdk.services.iotwireless.model.LoRaWANMulticastGet;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class ReadHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        ResourceModel model = request.getDesiredResourceState();

        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress -> proxy.initiate("AWS-IoTWireless-MulticastGroup::Read", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::getMulticastGroup)
                        .done(response -> {
                            LoRaWANMulticastGet requestLoRaWan = response.loRaWAN();
                            LoRaWAN modelLoRaWan = LoRaWAN.builder()
                                    .rfRegion(requestLoRaWan.rfRegion().toString())
                                    .dlClass(requestLoRaWan.dlClassAsString())
                                    .numberOfDevicesRequested(requestLoRaWan.numberOfDevicesRequested())
                                    .numberOfDevicesInGroup(requestLoRaWan.numberOfDevicesInGroup())
                                    .build();

                            model.setArn(response.arn());
                            model.setId(response.id());
                            model.setName(response.name());
                            model.setDescription(response.description());
                            model.setLoRaWAN(modelLoRaWan);
                            model.setStatus(response.status());
                            return ProgressEvent.progress(model, callbackContext);
                        })
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(model));
    }

    private GetMulticastGroupResponse getMulticastGroup(
            GetMulticastGroupRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetMulticastGroupResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getMulticastGroup);
        } catch (final Exception e) {
            throw handleException(e, getRequest);
        }
        return response;
    }
}