package software.amazon.iotwireless.multicastgroup;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessDeviceWithMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessDeviceWithMulticastGroupResponse;
import software.amazon.awssdk.services.iotwireless.model.DisassociateWirelessDeviceFromMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateWirelessDeviceFromMulticastGroupResponse;
import software.amazon.awssdk.services.iotwireless.model.UpdateMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateMulticastGroupResponse;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.exceptions.ResourceAlreadyExistsException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class UpdateHandler extends BaseHandlerStd {

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IotWirelessClient> proxyClient,
            final Logger logger) {

        final ResourceModel desiredModel = request.getDesiredResourceState();

        if (request.getRollback() != null && request.getRollback()) {
            final ResourceModel previousModel = request.getPreviousResourceState();

            return ProgressEvent.progress(desiredModel, callbackContext)
                    .then(progress -> proxy.initiate("AWS-IoTWireless-MulticastGroup::UpdateRollback", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                            .translateToServiceRequest(Translator::translateToUpdateRequest)
                            .makeServiceCall(this::updateMulticastGroup)
                            .progress()
                    )
                    .then(progress -> {
                        if (previousModel.getAssociateWirelessDevice() == null) return progress;
                        return proxy.initiate("AWS-IoTWireless-MulticastGroup::AssociateWirelessDeviceRollback", proxyClient, previousModel, progress.getCallbackContext())
                                .translateToServiceRequest(model -> DisassociateWirelessDeviceFromMulticastGroupRequest.builder()
                                        .id(model.getId())
                                        .wirelessDeviceId(model.getAssociateWirelessDevice())
                                        .build())
                                .makeServiceCall((updateRequest, serviceCallProxyClient) -> {
                                    try {
                                        return disassociateWirelessDevice(updateRequest, serviceCallProxyClient);
                                    } catch (final Exception e) {
                                        if (e instanceof CfnNotFoundException)
                                            return null; // acceptable because association might have failed or was not called
                                        throw e;
                                    }
                                })
                                .progress();
                    })
                    .then(progress -> {
                        if (previousModel.getDisassociateWirelessDevice() == null) return progress;
                        return proxy.initiate("AWS-IoTWireless-MulticastGroup::DisassociateWirelessDeviceRollback", proxyClient, previousModel, progress.getCallbackContext())
                                .translateToServiceRequest(model -> AssociateWirelessDeviceWithMulticastGroupRequest.builder()
                                        .id(model.getId())
                                        .wirelessDeviceId(model.getDisassociateWirelessDevice())
                                        .build())
                                .makeServiceCall((updateRequest, serviceCallProxyClient) -> {
                                    try {
                                        return associateWirelessDevice(updateRequest, serviceCallProxyClient);
                                    } catch (final Exception e) {
                                        if (e instanceof ResourceAlreadyExistsException)
                                            return null; // acceptable because disassociation might have failed or was not called
                                        throw e;
                                    }
                                })
                                .progress();
                    })
                    .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(desiredModel)));
        }

        if (desiredModel.getAssociateWirelessDevice() != null
                && desiredModel.getAssociateWirelessDevice().equals(desiredModel.getDisassociateWirelessDevice())) {
            throw new CfnInvalidRequestException("Cannot associate and disassociate the same wireless device");
        }

        return ProgressEvent.progress(desiredModel, callbackContext)
                .then(progress -> {
                    if (desiredModel.getDisassociateWirelessDevice() == null) return progress;
                    return proxy.initiate("AWS-IoTWireless-MulticastGroup::DisassociateWirelessDevice", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                            .translateToServiceRequest(Translator::translateToDisassociateWirelessDeviceRequest)
                            .makeServiceCall(this::disassociateWirelessDevice)
                            .progress();
                })
                .then(progress -> {
                    if (desiredModel.getAssociateWirelessDevice() == null) return progress;
                    return proxy.initiate("AWS-IoTWireless-MulticastGroup::AssociateWirelessDevice", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                            .translateToServiceRequest(Translator::translateToAssociateWirelessDeviceRequest)
                            .makeServiceCall(this::associateWirelessDevice)
                            .progress();
                })
                .then(progress -> proxy.initiate("AWS-IoTWireless-MulticastGroup::Update", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                        .translateToServiceRequest(Translator::translateToUpdateRequest)
                        .makeServiceCall(this::updateMulticastGroup)
                        .progress()
                )
                .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(desiredModel)));
    }

    private DisassociateWirelessDeviceFromMulticastGroupResponse disassociateWirelessDevice(DisassociateWirelessDeviceFromMulticastGroupRequest updateRequest, ProxyClient<IotWirelessClient> proxyClient) {
        try {
            return proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::disassociateWirelessDeviceFromMulticastGroup);
        } catch (final Exception e) {
            throw handleException(e, updateRequest, "AWS::IoTWireless::WirelessDevice",
                    request -> ((DisassociateWirelessDeviceFromMulticastGroupRequest) request).wirelessDeviceId());
        }
    }

    private AssociateWirelessDeviceWithMulticastGroupResponse associateWirelessDevice(AssociateWirelessDeviceWithMulticastGroupRequest updateRequest, ProxyClient<IotWirelessClient> proxyClient) {
        try {
            return proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::associateWirelessDeviceWithMulticastGroup);
        } catch (final Exception e) {
            throw handleException(e, updateRequest, "AWS::IoTWireless::WirelessDevice",
                    request -> ((AssociateWirelessDeviceWithMulticastGroupRequest) request).wirelessDeviceId());
        }
    }

    private UpdateMulticastGroupResponse updateMulticastGroup(UpdateMulticastGroupRequest updateRequest, ProxyClient<IotWirelessClient> proxyClient) {
        try {
            return proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::updateMulticastGroup);
        } catch (final Exception e) {
            throw handleException(e, updateRequest);
        }
    }
}
