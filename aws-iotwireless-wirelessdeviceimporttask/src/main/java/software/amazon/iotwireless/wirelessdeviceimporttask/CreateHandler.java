package software.amazon.iotwireless.wirelessdeviceimporttask;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.StartWirelessDeviceImportTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.StartWirelessDeviceImportTaskResponse;
import software.amazon.awssdk.services.iotwireless.model.StartSingleWirelessDeviceImportTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.StartSingleWirelessDeviceImportTaskResponse;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.Map;


public class CreateHandler extends BaseHandlerStd {
    private Logger logger;
    private AmazonWebServicesClientProxy proxy;
    private static final String AWS_TAG_PREFIX = "aws:" ;

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        this.logger = logger;
        this.proxy = proxy;
        final ResourceModel model = request.getDesiredResourceState();

        validateTag(TagHelper.generateTagsForCreate(request));
        if(model.getSidewalk().getSidewalkManufacturingSn()!=null){
            return ProgressEvent.progress(model, callbackContext)
                    .then(progress -> proxy.initiate("AWS-IoTWireless-WirelessDeviceImportTask::Create", proxyClient,model, callbackContext)
                            .translateToServiceRequest(resourceModel -> Translator.translateToSingleCreateRequest(request))
                            .makeServiceCall(this::invokeSingleCreateAPI)
                            .done(response -> {
                                model.setId(response.id());
                                model.setArn(response.arn());
                                return progress;
                            })
                    )
                    .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));
        } else{
            return ProgressEvent.progress(model, callbackContext)
                    .then(progress -> proxy.initiate("AWS-IoTWireless-WirelessDeviceImportTask::Create", proxyClient,model, callbackContext)
                            .translateToServiceRequest(resourceModel -> Translator.translateToCreateRequest(request))
                            .makeServiceCall(this::invokeCreateAPI)
                            .done(response -> {
                                model.setId(response.id());
                                model.setArn(response.arn());
                                return progress;
                            })
                    )
                    .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(model)));
        }
    }

    private StartWirelessDeviceImportTaskResponse invokeCreateAPI(StartWirelessDeviceImportTaskRequest request, ProxyClient<IotWirelessClient> proxyClient){
        StartWirelessDeviceImportTaskResponse response;
        try {
            response = proxy.injectCredentialsAndInvokeV2(request,proxyClient.client()::startWirelessDeviceImportTask);
        } catch (final Exception e){
            throw handleException(e);
        }
        logger.log(String.format("%s successfully created.", ResourceModel.TYPE_NAME));
        return response;
    }

    private StartSingleWirelessDeviceImportTaskResponse invokeSingleCreateAPI(StartSingleWirelessDeviceImportTaskRequest request, ProxyClient<IotWirelessClient> proxyClient){
        StartSingleWirelessDeviceImportTaskResponse response;
        try {
            response = proxy.injectCredentialsAndInvokeV2(request,
                    proxyClient.client()::startSingleWirelessDeviceImportTask);
        } catch (final Exception e){
            throw handleException(e);
        }
        logger.log(String.format("%s successfully created.", ResourceModel.TYPE_NAME));
        return response;
    }

    private void validateTag(Map<String, String> tagList) {
        if (tagList == null) {
            return;
        }
        // Check for invalid requested system tags.
        for (String key : tagList.keySet()) {
            if (key.trim().toLowerCase().startsWith(AWS_TAG_PREFIX)) {
                throw new CfnInvalidRequestException(key + " is an invalid key. aws: prefixed tag key names cannot be requested.");

            }
        }
    }
}
