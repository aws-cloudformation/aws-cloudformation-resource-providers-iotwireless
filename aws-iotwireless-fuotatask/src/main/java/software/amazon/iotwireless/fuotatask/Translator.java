package software.amazon.iotwireless.fuotatask;

import software.amazon.awssdk.services.iotwireless.model.AssociateMulticastGroupWithFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessDeviceWithFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.CreateFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateMulticastGroupFromFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateWirelessDeviceFromFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.GetFuotaTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.ListFuotaTasksRequest;
import software.amazon.awssdk.services.iotwireless.model.LoRaWANFuotaTask;
import software.amazon.awssdk.services.iotwireless.model.UpdateFuotaTaskRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * This class is a centralized placeholder for
 * - api request construction
 * - object translation to/from aws sdk
 * - resource model construction for read/list handlers
 */

public class Translator {
    //Translate from Resource Model Tags to SDK Tags
    static List<software.amazon.awssdk.services.iotwireless.model.Tag>
    translateTag(final Collection<software.amazon.iotwireless.fuotatask.Tag> tags) {
        List<software.amazon.awssdk.services.iotwireless.model.Tag> newTagList = new ArrayList<>();
        if (tags == null) {
            return newTagList;
        }
        for (software.amazon.iotwireless.fuotatask.Tag tag : tags) {
            software.amazon.awssdk.services.iotwireless.model.Tag newTag = software.amazon.awssdk.services.iotwireless.model.Tag.builder()
                    .key(tag.getKey())
                    .value(tag.getValue())
                    .build();
            newTagList.add(newTag);
        }
        return newTagList;
    }

    static CreateFuotaTaskRequest translateToCreateRequest(final ResourceModel model, String clientRequestToken) {
        Collection<software.amazon.iotwireless.fuotatask.Tag> tags = null;
        if (model.getTags() != null) {
            tags = new HashSet<>(model.getTags());
        }

        LoRaWANFuotaTask loRaWANFuotaTask = LoRaWANFuotaTask.builder()
                .rfRegion(model.getLoRaWAN().getRfRegion())
                .build();

        return CreateFuotaTaskRequest.builder()
                .name(model.getName())
                .description(model.getDescription())
                .clientRequestToken(clientRequestToken)
                .loRaWAN(loRaWANFuotaTask)
                .firmwareUpdateImage(model.getFirmwareUpdateImage())
                .firmwareUpdateRole(model.getFirmwareUpdateRole())
                .tags(translateTag(tags))
                .build();
    }

    static GetFuotaTaskRequest translateToReadRequest(final ResourceModel model) {
        return GetFuotaTaskRequest.builder()
                .id(model.getId())
                .build();
    }

    static DeleteFuotaTaskRequest translateToDeleteRequest(final ResourceModel model) {
        return DeleteFuotaTaskRequest.builder()
                .id(model.getId())
                .build();
    }

    static UpdateFuotaTaskRequest translateToUpdateRequest(final ResourceModel model) {
        LoRaWANFuotaTask loRaWANFuotaTask = null;
        if (model.getLoRaWAN() != null) {
            loRaWANFuotaTask = LoRaWANFuotaTask.builder()
                    .rfRegion(model.getLoRaWAN().getRfRegion())
                    .build();
        }

        return UpdateFuotaTaskRequest.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .loRaWAN(loRaWANFuotaTask)
                .firmwareUpdateImage(model.getFirmwareUpdateImage())
                .firmwareUpdateRole(model.getFirmwareUpdateRole())
                .build();
    }

    static AssociateWirelessDeviceWithFuotaTaskRequest translateToAssociateWirelessDeviceRequest(final ResourceModel model) {
        return AssociateWirelessDeviceWithFuotaTaskRequest.builder()
                .id(model.getId())
                .wirelessDeviceId(model.getAssociateWirelessDevice())
                .build();
    }

    static DisassociateWirelessDeviceFromFuotaTaskRequest translateToDisassociateWirelessDeviceRequest(final ResourceModel model) {
        return DisassociateWirelessDeviceFromFuotaTaskRequest.builder()
                .id(model.getId())
                .wirelessDeviceId(model.getDisassociateWirelessDevice())
                .build();
    }

    static AssociateMulticastGroupWithFuotaTaskRequest translateToAssociateMulticastGroupRequest(final ResourceModel model) {
        return AssociateMulticastGroupWithFuotaTaskRequest.builder()
                .id(model.getId())
                .multicastGroupId(model.getAssociateMulticastGroup())
                .build();
    }

    static DisassociateMulticastGroupFromFuotaTaskRequest translateToDisassociateMulticastGroupRequest(final ResourceModel model) {
        return DisassociateMulticastGroupFromFuotaTaskRequest.builder()
                .id(model.getId())
                .multicastGroupId(model.getDisassociateMulticastGroup())
                .build();
    }

    static ListFuotaTasksRequest translateToListRequest(final String nextToken) {
        return ListFuotaTasksRequest.builder()
                .nextToken(nextToken)
                .build();
    }

    // Returned at the end of Create, Read, Update handlers to make sure they all return the same thing
    static ResourceModel setModel(final ResourceModel model) {
        return ResourceModel.builder()
                .name(model.getName())
                .description(model.getDescription())
                .loRaWAN(model.getLoRaWAN())
                .firmwareUpdateImage(model.getFirmwareUpdateImage())
                .firmwareUpdateRole(model.getFirmwareUpdateRole())
                .arn(model.getArn())
                .id(model.getId())
                .tags(model.getTags())
                .fuotaTaskStatus(model.getFuotaTaskStatus())
                .associateWirelessDevice(model.getAssociateWirelessDevice())
                .disassociateWirelessDevice(model.getDisassociateWirelessDevice())
                .associateMulticastGroup(model.getAssociateMulticastGroup())
                .disassociateMulticastGroup(model.getDisassociateMulticastGroup())
                .build();
    }
}
