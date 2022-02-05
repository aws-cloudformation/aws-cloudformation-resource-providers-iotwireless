package software.amazon.iotwireless.fuotatask;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.awssdk.services.iotwireless.model.AssociateMulticastGroupWithFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.AssociateMulticastGroupWithFuotaTaskResponse;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessDeviceWithFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessDeviceWithFuotaTaskResponse;
import software.amazon.awssdk.services.iotwireless.model.DisassociateMulticastGroupFromFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateMulticastGroupFromFuotaTaskResponse;
import software.amazon.awssdk.services.iotwireless.model.DisassociateWirelessDeviceFromFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateWirelessDeviceFromFuotaTaskResponse;
import software.amazon.awssdk.services.iotwireless.model.UpdateFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateFuotaTaskResponse;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.exceptions.BaseHandlerException;
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
                            .makeServiceCall(this::updateFuotaTask)
                            .progress()
                    )
                    .then(progress -> {
                                if (previousModel.getAssociateMulticastGroup() == null) return progress;
                                return proxy.initiate("AWS-IoTWireless-MulticastGroup::AssociateMulticastGroupRollback", proxyClient, previousModel, progress.getCallbackContext())
                                        .translateToServiceRequest(model -> DisassociateMulticastGroupFromFuotaTaskRequest.builder()
                                                .id(model.getId())
                                                .multicastGroupId(model.getAssociateMulticastGroup())
                                                .build())
                                        .makeServiceCall((updateRequest, serviceCallProxyClient) -> {
                                            try {
                                                return disassociateMulticastGroup(updateRequest, serviceCallProxyClient);
                                            } catch (final Exception e) {
                                                if (e instanceof CfnNotFoundException)
                                                    return null; // acceptable because association might have failed or was not called
                                                throw e;
                                            }
                                        })
                                        .progress();
                            }
                    )
                    .then(progress -> {
                                if (previousModel.getAssociateWirelessDevice() == null) return progress;
                                return proxy.initiate("AWS-IoTWireless-MulticastGroup::AssociateWirelessDeviceRollback", proxyClient, previousModel, progress.getCallbackContext())
                                        .translateToServiceRequest(model -> DisassociateWirelessDeviceFromFuotaTaskRequest.builder()
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
                            }
                    )
                    .then(progress -> {
                                if (previousModel.getDisassociateMulticastGroup() == null) return progress;
                                return proxy.initiate("AWS-IoTWireless-MulticastGroup::DisassociateMulticastGroupRollback", proxyClient, previousModel, progress.getCallbackContext())
                                        .translateToServiceRequest(model -> AssociateMulticastGroupWithFuotaTaskRequest.builder()
                                                .id(model.getId())
                                                .multicastGroupId(model.getDisassociateMulticastGroup())
                                                .build())
                                        .makeServiceCall((updateRequest, serviceCallProxyClient) -> {

                                            try {
                                                return associateMulticastGroup(updateRequest, serviceCallProxyClient);
                                            } catch (final Exception e) {
                                                if (e instanceof ResourceAlreadyExistsException)
                                                    return null; // acceptable because disassociation might have failed or was not called
                                                throw e;
                                            }

                                        })
                                        .progress();
                            }
                    )
                    .then(progress -> {
                                if (previousModel.getDisassociateWirelessDevice() == null) return progress;
                                return proxy.initiate("AWS-IoTWireless-MulticastGroup::DisassociateWirelessDeviceRollback", proxyClient, previousModel, progress.getCallbackContext())
                                        .translateToServiceRequest(model -> AssociateWirelessDeviceWithFuotaTaskRequest.builder()
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
                            }
                    )
                    .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(desiredModel)));
        }

        if (desiredModel.getAssociateWirelessDevice() != null
                && desiredModel.getAssociateWirelessDevice().equals(desiredModel.getDisassociateWirelessDevice())) {
            throw new CfnInvalidRequestException("Cannot associate and disassociate the same wireless device");
        }

        if (desiredModel.getAssociateMulticastGroup() != null
                && desiredModel.getAssociateMulticastGroup().equals(desiredModel.getDisassociateMulticastGroup())) {
            throw new CfnInvalidRequestException("Cannot associate and disassociate the same multicast group");
        }

        try {
            return ProgressEvent.progress(desiredModel, callbackContext)
                    .then(progress -> {
                        if (desiredModel.getDisassociateWirelessDevice() == null) return progress;
                        return proxy.initiate("AWS-IoTWireless-FuotaTask::DisassociateWirelessDevice", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                .translateToServiceRequest(Translator::translateToDisassociateWirelessDeviceRequest)
                                .makeServiceCall(this::disassociateWirelessDevice)
                                .progress();
                    })
                    .then(progress -> {
                        if (desiredModel.getDisassociateMulticastGroup() == null) return progress;
                        return proxy.initiate("AWS-IoTWireless-FuotaTask::DisassociateMulticastGroup", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                .translateToServiceRequest(Translator::translateToDisassociateMulticastGroupRequest)
                                .makeServiceCall(this::disassociateMulticastGroup)
                                .progress();

                    })
                    .then(progress -> {
                        if (desiredModel.getAssociateWirelessDevice() == null) return progress;
                        return proxy.initiate("AWS-IoTWireless-FuotaTask::AssociateWirelessDevice", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                .translateToServiceRequest(Translator::translateToAssociateWirelessDeviceRequest)
                                .makeServiceCall(this::associateWirelessDevice)
                                .progress();
                    })
                    .then(progress -> {
                        if (desiredModel.getAssociateMulticastGroup() == null) return progress;
                        return proxy.initiate("AWS-IoTWireless-FuotaTask::AssociateMulticastGroup", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                .translateToServiceRequest(Translator::translateToAssociateMulticastGroupRequest)
                                .makeServiceCall(this::associateMulticastGroup)
                                .progress();
                    })
                    .then(progress ->
                            proxy.initiate("AWS-IoTWireless-FuotaTask::Update", proxyClient, progress.getResourceModel(), progress.getCallbackContext())
                                    .translateToServiceRequest(Translator::translateToUpdateRequest)
                                    .makeServiceCall(this::updateFuotaTask)
                                    .progress()
                    )
                    .then(progress -> ProgressEvent.defaultSuccessHandler(Translator.setModel(desiredModel)));
        } catch (final BaseHandlerException e) {
            return ProgressEvent.failed(desiredModel, callbackContext, e.getErrorCode(), e.getMessage());
        }
    }

    private DisassociateWirelessDeviceFromFuotaTaskResponse disassociateWirelessDevice(DisassociateWirelessDeviceFromFuotaTaskRequest updateRequest, ProxyClient<IotWirelessClient> proxyClient) {
        try {
            return proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::disassociateWirelessDeviceFromFuotaTask);
        } catch (final Exception e) {
            throw handleException(e, updateRequest, "AWS::IoTWireless::WirelessDevice",
                    request -> ((DisassociateWirelessDeviceFromFuotaTaskRequest) request).wirelessDeviceId());
        }
    }

    private DisassociateMulticastGroupFromFuotaTaskResponse disassociateMulticastGroup(DisassociateMulticastGroupFromFuotaTaskRequest updateRequest, ProxyClient<IotWirelessClient> proxyClient) {
        try {
            return proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::disassociateMulticastGroupFromFuotaTask);
        } catch (final Exception e) {
            throw handleException(e, updateRequest, "AWS::IoTWireless::MulticastGroup",
                    request -> ((DisassociateMulticastGroupFromFuotaTaskRequest) request).multicastGroupId());
        }
    }

    private AssociateWirelessDeviceWithFuotaTaskResponse associateWirelessDevice(AssociateWirelessDeviceWithFuotaTaskRequest updateRequest, ProxyClient<IotWirelessClient> proxyClient) {
        try {
            return proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::associateWirelessDeviceWithFuotaTask);
        } catch (final Exception e) {
            throw handleException(e, updateRequest, "AWS::IoTWireless::WirelessDevice",
                    request -> ((AssociateWirelessDeviceWithFuotaTaskRequest) request).wirelessDeviceId());
        }
    }

    private AssociateMulticastGroupWithFuotaTaskResponse associateMulticastGroup(AssociateMulticastGroupWithFuotaTaskRequest updateRequest, ProxyClient<IotWirelessClient> proxyClient) {
        try {
            return proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::associateMulticastGroupWithFuotaTask);
        } catch (final Exception e) {
            throw handleException(e, updateRequest, "AWS::IoTWireless::MulticastGroup",
                    request -> ((AssociateMulticastGroupWithFuotaTaskRequest) request).multicastGroupId());
        }
    }

    private UpdateFuotaTaskResponse updateFuotaTask(UpdateFuotaTaskRequest updateRequest, ProxyClient<IotWirelessClient> proxyClient) {
        try {
            return proxyClient.injectCredentialsAndInvokeV2(updateRequest, proxyClient.client()::updateFuotaTask);
        } catch (final Exception e) {
            throw handleException(e, updateRequest);
        }
    }
}