package software.amazon.iotwireless.fuotatask;

import com.amazonaws.util.StringUtils;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.GetFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.GetFuotaTaskResponse;
import software.amazon.awssdk.services.iotwireless.model.LoRaWANFuotaTaskGetInfo;
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
                .then(progress -> proxy.initiate("AWS-IoTWireless-FuotaTask::Read", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToReadRequest)
                        .makeServiceCall(this::getFuotaTask)
                        .done(response -> {
                            LoRaWANFuotaTaskGetInfo requestLoRaWan = response.loRaWAN();
                            String startTime = null;
                            if (requestLoRaWan.startTime() != null) {
                                startTime = StringUtils.fromLong(requestLoRaWan.startTime().getEpochSecond());
                            }
                            LoRaWAN modelLoRaWan = LoRaWAN.builder()
                                    .startTime(startTime)
                                    .rfRegion(requestLoRaWan.rfRegion())
                                    .build();

                            model.setArn(response.arn());
                            model.setId(response.id());
                            model.setName(response.name());
                            model.setDescription(response.description());
                            model.setLoRaWAN(modelLoRaWan);
                            model.setFirmwareUpdateImage(response.firmwareUpdateImage());
                            model.setFirmwareUpdateRole(response.firmwareUpdateRole());
                            model.setFuotaTaskStatus(response.statusAsString());
                            return ProgressEvent.progress(model, callbackContext);
                        })
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(model));
    }

    private GetFuotaTaskResponse getFuotaTask(
            GetFuotaTaskRequest getRequest,
            final ProxyClient<IotWirelessClient> proxyClient) {
        GetFuotaTaskResponse response;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(getRequest, proxyClient.client()::getFuotaTask);
        } catch (final Exception e) {
            throw handleException(e, getRequest);
        }
        return response;
    }
}
