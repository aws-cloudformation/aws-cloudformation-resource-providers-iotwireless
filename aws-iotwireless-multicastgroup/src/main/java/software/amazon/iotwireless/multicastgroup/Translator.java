package software.amazon.iotwireless.multicastgroup;

import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessDeviceWithMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.CreateMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateWirelessDeviceFromMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.GetMulticastGroupRequest;
import software.amazon.awssdk.services.iotwireless.model.ListMulticastGroupsRequest;
import software.amazon.awssdk.services.iotwireless.model.LoRaWANMulticast;
import software.amazon.awssdk.services.iotwireless.model.UpdateMulticastGroupRequest;

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
    translateTag(final Collection<software.amazon.iotwireless.multicastgroup.Tag> tags) {
        List<software.amazon.awssdk.services.iotwireless.model.Tag> newTagList = new ArrayList<>();
        if (tags == null) {
            return newTagList;
        }
        for (software.amazon.iotwireless.multicastgroup.Tag tag : tags) {
            software.amazon.awssdk.services.iotwireless.model.Tag newTag = software.amazon.awssdk.services.iotwireless.model.Tag.builder()
                    .key(tag.getKey())
                    .value(tag.getValue())
                    .build();
            newTagList.add(newTag);
        }
        return newTagList;
    }

    static CreateMulticastGroupRequest translateToCreateRequest(final ResourceModel model, String clientRequestToken) {
        Collection<software.amazon.iotwireless.multicastgroup.Tag> tags = null;
        if (model.getTags() != null) {
            tags = new HashSet<>(model.getTags());
        }

        LoRaWAN modelLoRaWan = model.getLoRaWAN();
        LoRaWANMulticast loRaWANMulticast = LoRaWANMulticast.builder()
                .rfRegion(modelLoRaWan.getRfRegion())
                .dlClass(modelLoRaWan.getDlClass())
                .build();

        return CreateMulticastGroupRequest.builder()
                .name(model.getName())
                .description(model.getDescription())
                .clientRequestToken(clientRequestToken)
                .loRaWAN(loRaWANMulticast)
                .tags(translateTag(tags))
                .build();
    }

    static GetMulticastGroupRequest translateToReadRequest(final ResourceModel model) {
        return GetMulticastGroupRequest.builder()
                .id(model.getId())
                .build();
    }

    static DeleteMulticastGroupRequest translateToDeleteRequest(final ResourceModel model) {
        return DeleteMulticastGroupRequest.builder()
                .id(model.getId())
                .build();
    }

    static UpdateMulticastGroupRequest translateToUpdateRequest(final ResourceModel model) {
        LoRaWANMulticast loRaWANMulticast = null;
        if (model.getLoRaWAN() != null) {
            LoRaWAN modelLoRaWan = model.getLoRaWAN();
            loRaWANMulticast = LoRaWANMulticast.builder()
                    .rfRegion(modelLoRaWan.getRfRegion())
                    .dlClass(modelLoRaWan.getDlClass())
                    .build();
        }

        return UpdateMulticastGroupRequest.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .loRaWAN(loRaWANMulticast)
                .build();
    }

    static AssociateWirelessDeviceWithMulticastGroupRequest translateToAssociateWirelessDeviceRequest(final ResourceModel model) {
        return AssociateWirelessDeviceWithMulticastGroupRequest.builder()
                .id(model.getId())
                .wirelessDeviceId(model.getAssociateWirelessDevice())
                .build();
    }

    static DisassociateWirelessDeviceFromMulticastGroupRequest translateToDisassociateWirelessDeviceRequest(final ResourceModel model) {
        return DisassociateWirelessDeviceFromMulticastGroupRequest.builder()
                .id(model.getId())
                .wirelessDeviceId(model.getDisassociateWirelessDevice())
                .build();
    }

    static ListMulticastGroupsRequest translateToListRequest(final String nextToken) {
        return ListMulticastGroupsRequest.builder()
                .nextToken(nextToken)
                .build();
    }

    //Returned at the end of Create, Read, Update handlers to make sure they all return the same thing
    static ResourceModel setModel(final ResourceModel model) {
        return ResourceModel.builder()
                .name(model.getName())
                .description(model.getDescription())
                .loRaWAN(model.getLoRaWAN())
                .arn(model.getArn())
                .id(model.getId())
                .tags(model.getTags())
                .status(model.getStatus())
                .associateWirelessDevice(model.getAssociateWirelessDevice())
                .disassociateWirelessDevice(model.getDisassociateWirelessDevice())
                .build();
    }
}