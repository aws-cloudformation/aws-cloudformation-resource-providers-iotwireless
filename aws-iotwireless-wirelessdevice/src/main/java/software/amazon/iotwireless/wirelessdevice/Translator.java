package software.amazon.iotwireless.wirelessdevice;

import software.amazon.awssdk.services.iotwireless.model.CreateWirelessDeviceRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteWirelessDeviceRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateWirelessDeviceFromThingRequest;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessDeviceRequest;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessDeviceWithThingRequest;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessDevicesRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateWirelessDeviceRequest;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * This class is a centralized placeholder for
 * - api request construction
 * - object translation to/from aws sdk
 * - resource model construction for read/list handlers
 */

public class Translator {

    //Translate from ResourceModel Tag to SDK Tag
    static Collection<software.amazon.awssdk.services.iotwireless.model.Tag>
    translateTag(final Collection<software.amazon.iotwireless.wirelessdevice.Tag> tags) {
        Collection<software.amazon.awssdk.services.iotwireless.model.Tag> newTagCollection =
                new HashSet<software.amazon.awssdk.services.iotwireless.model.Tag>();
        if (tags == null) {
            return newTagCollection;
        }
        for (software.amazon.iotwireless.wirelessdevice.Tag tag : tags) {
            software.amazon.awssdk.services.iotwireless.model.Tag newTag = software.amazon.awssdk.services.iotwireless.model.Tag.builder()
                    .key(tag.getKey())
                    .value(tag.getValue())
                    .build();
            newTagCollection.add(newTag);
        }
        return newTagCollection;
    }

    //Translate from LoRaWANListDevice to LoRaWAN
    static software.amazon.iotwireless.wirelessdevice.LoRaWANDevice
    translateToLoRaWAN(software.amazon.awssdk.services.iotwireless.model.LoRaWANListDevice device) {
        if (device == null) {
            return null;
        }
        return software.amazon.iotwireless.wirelessdevice.LoRaWANDevice.builder()
                .devEui(device.devEui())
                .build();
    }

    //Translate from ResourceModel OtaaV11 to SDK OtaaV1_1
    static software.amazon.awssdk.services.iotwireless.model.OtaaV1_1
    translateOtaaV11(software.amazon.iotwireless.wirelessdevice.LoRaWANDevice model) {
        if (model.getOtaaV11() == null) {
            return null;
        }
        software.amazon.iotwireless.wirelessdevice.OtaaV11 Otaa =
                model.getOtaaV11();
        return software.amazon.awssdk.services.iotwireless.model.OtaaV1_1.builder()
                .appKey(Otaa.getAppKey())
                .nwkKey(Otaa.getNwkKey())
                .joinEui(Otaa.getJoinEui())
                .build();
    }

    //Translate from ResourceModel OtaaV10 to SDK OtaaV1_0_x
    static software.amazon.awssdk.services.iotwireless.model.OtaaV1_0_x
    translateOtaaV10x(software.amazon.iotwireless.wirelessdevice.LoRaWANDevice model) {
        if (model.getOtaaV10x() == null) {
            return null;
        }
        software.amazon.iotwireless.wirelessdevice.OtaaV10x Otaa =
                model.getOtaaV10x();
        return software.amazon.awssdk.services.iotwireless.model.OtaaV1_0_x.builder()
                .appKey(Otaa.getAppKey())
                .appEui(Otaa.getAppEui())
                .build();
    }

    //Translate from ResourceModel AbpV10x to SDK AbpV1_0_x
    static software.amazon.awssdk.services.iotwireless.model.AbpV1_0_x
    translateAbpV10x(final software.amazon.iotwireless.wirelessdevice.LoRaWANDevice model) {
        if (model.getAbpV10x() == null) {
            return null;
        }
        software.amazon.iotwireless.wirelessdevice.AbpV10x abp =
                model.getAbpV10x();
        return software.amazon.awssdk.services.iotwireless.model.AbpV1_0_x.builder()
                .devAddr(abp.getDevAddr())
                .sessionKeys(translateSessionKeysAbpV10x(abp))
                .build();
    }

    //Translate from ResourceModel AbpV11 to SDK AbpV1_1
    static software.amazon.awssdk.services.iotwireless.model.AbpV1_1
    translateAbpV11x(final software.amazon.iotwireless.wirelessdevice.LoRaWANDevice model) {
        if (model.getAbpV11() == null) {
            return null;
        }
        software.amazon.iotwireless.wirelessdevice.AbpV11 abp =
                model.getAbpV11();
        return software.amazon.awssdk.services.iotwireless.model.AbpV1_1.builder()
                .devAddr(abp.getDevAddr())
                .sessionKeys(translateSessionKeysAbpV11(abp))
                .build();
    }

    //Translate from ResourceModel AbpV10x to SDK AbpV1_0_x
    static software.amazon.awssdk.services.iotwireless.model.SessionKeysAbpV1_0_x
    translateSessionKeysAbpV10x(software.amazon.iotwireless.wirelessdevice.AbpV10x model) {
        if (model.getSessionKeys() == null) {
            return null;
        }
        software.amazon.iotwireless.wirelessdevice.SessionKeysAbpV10x sessionKeys =
                model.getSessionKeys();
        return software.amazon.awssdk.services.iotwireless.model.SessionKeysAbpV1_0_x.builder()
                .nwkSKey(sessionKeys.getNwkSKey())
                .appSKey(sessionKeys.getAppSKey())
                .build();
    }

    //Translate from ResourceModel AbpV11 to SDK AbpV1_1
    static software.amazon.awssdk.services.iotwireless.model.SessionKeysAbpV1_1
    translateSessionKeysAbpV11(software.amazon.iotwireless.wirelessdevice.AbpV11 model) {
        if (model.getSessionKeys() == null) {
            return null;
        }
        software.amazon.iotwireless.wirelessdevice.SessionKeysAbpV11 sessionKeys =
                model.getSessionKeys();
        return software.amazon.awssdk.services.iotwireless.model.SessionKeysAbpV1_1.builder()
                .fNwkSIntKey(sessionKeys.getFNwkSIntKey())
                .nwkSEncKey(sessionKeys.getNwkSEncKey())
                .sNwkSIntKey(sessionKeys.getSNwkSIntKey())
                .appSKey(sessionKeys.getAppSKey())
                .build();
    }

    //Translate from ResourceModel LoRaDevice to SDK LoRaWANDevice
    static software.amazon.awssdk.services.iotwireless.model.LoRaWANDevice
    translateToLoRaDevice(final ResourceModel model) {
        if (model.getLoRaWAN() == null) {
            return null;
        }
        software.amazon.iotwireless.wirelessdevice.LoRaWANDevice device = model.getLoRaWAN();
        if (device.getOtaaV10x() != null) {
            return software.amazon.awssdk.services.iotwireless.model.LoRaWANDevice.builder()
                    .devEui(device.getDevEui())
                    .deviceProfileId(device.getDeviceProfileId())
                    .serviceProfileId(device.getServiceProfileId())
                    .otaaV1_0_x((translateOtaaV10x(device)))
                    .build();
        }
        if (device.getOtaaV11() != null) {
            return software.amazon.awssdk.services.iotwireless.model.LoRaWANDevice.builder()
                    .devEui(device.getDevEui())
                    .deviceProfileId(device.getDeviceProfileId())
                    .serviceProfileId(device.getServiceProfileId())
                    .otaaV1_1((translateOtaaV11(device)))
                    .build();
        }
        if (device.getAbpV10x() != null) {
            return software.amazon.awssdk.services.iotwireless.model.LoRaWANDevice.builder()
                    .devEui(device.getDevEui())
                    .deviceProfileId(device.getDeviceProfileId())
                    .serviceProfileId(device.getServiceProfileId())
                    .abpV1_0_x((translateAbpV10x(device)))
                    .build();
        }
        if (device.getAbpV11() != null) {
            return software.amazon.awssdk.services.iotwireless.model.LoRaWANDevice.builder()
                    .devEui(device.getDevEui())
                    .deviceProfileId(device.getDeviceProfileId())
                    .serviceProfileId(device.getServiceProfileId())
                    .abpV1_1((translateAbpV11x(device)))
                    .build();
        } else {
            return software.amazon.awssdk.services.iotwireless.model.LoRaWANDevice.builder()
                    .devEui(device.getDevEui())
                    .deviceProfileId(device.getDeviceProfileId())
                    .serviceProfileId(device.getServiceProfileId())
                    .build();
        }
    }
    static software.amazon.iotwireless.wirelessdevice.OtaaV11
    translateOtaaV11SDK(software.amazon.awssdk.services.iotwireless.model.LoRaWANDevice model) {
        software.amazon.awssdk.services.iotwireless.model.OtaaV1_1 Otaa =
                model.otaaV1_1();
        software.amazon.iotwireless.wirelessdevice.OtaaV11 newOtaa =
                new software.amazon.iotwireless.wirelessdevice.OtaaV11();
        newOtaa.setAppKey(Otaa.appKey());
        newOtaa.setNwkKey(Otaa.nwkKey());
        newOtaa.setJoinEui(Otaa.joinEui());
        return newOtaa;
    }

    static software.amazon.iotwireless.wirelessdevice.OtaaV10x
    translateOtaaV10xSDK(software.amazon.awssdk.services.iotwireless.model.LoRaWANDevice model) {
        software.amazon.awssdk.services.iotwireless.model.OtaaV1_0_x Otaa =
                model.otaaV1_0_x();
        software.amazon.iotwireless.wirelessdevice.OtaaV10x newOtaa =
                new software.amazon.iotwireless.wirelessdevice.OtaaV10x();
        newOtaa.setAppKey(Otaa.appKey());
        newOtaa.setAppEui(Otaa.appEui());
        return newOtaa;
    }

    static software.amazon.iotwireless.wirelessdevice.SessionKeysAbpV10x
    translateSessionKeysAbpV10xSDK(software.amazon.awssdk.services.iotwireless.model.AbpV1_0_x model) {
        software.amazon.awssdk.services.iotwireless.model.SessionKeysAbpV1_0_x sessionKeys =
                model.sessionKeys();
        software.amazon.iotwireless.wirelessdevice.SessionKeysAbpV10x newSessionKeys =
                new software.amazon.iotwireless.wirelessdevice.SessionKeysAbpV10x();
        newSessionKeys.setNwkSKey(sessionKeys.nwkSKey());
        newSessionKeys.setAppSKey(sessionKeys.appSKey());
        return newSessionKeys;
    }

    static software.amazon.iotwireless.wirelessdevice.SessionKeysAbpV11
    translateSessionKeysAbpV11SDK(software.amazon.awssdk.services.iotwireless.model.AbpV1_1 model) {
        software.amazon.awssdk.services.iotwireless.model.SessionKeysAbpV1_1 sessionKeys =
                model.sessionKeys();
        software.amazon.iotwireless.wirelessdevice.SessionKeysAbpV11 newSessionKeys =
                new software.amazon.iotwireless.wirelessdevice.SessionKeysAbpV11();
        newSessionKeys.setFNwkSIntKey(sessionKeys.fNwkSIntKey());
        newSessionKeys.setNwkSEncKey(sessionKeys.nwkSEncKey());
        newSessionKeys.setSNwkSIntKey(sessionKeys.sNwkSIntKey());
        newSessionKeys.setAppSKey(sessionKeys.appSKey());
        return newSessionKeys;
    }

    static software.amazon.iotwireless.wirelessdevice.AbpV10x
    translateAbpV10xSDK(software.amazon.awssdk.services.iotwireless.model.LoRaWANDevice model) {
        software.amazon.awssdk.services.iotwireless.model.AbpV1_0_x abp =
                model.abpV1_0_x();
        software.amazon.iotwireless.wirelessdevice.AbpV10x newAbp =
                new software.amazon.iotwireless.wirelessdevice.AbpV10x();
        newAbp.setDevAddr(abp.devAddr());
        newAbp.setSessionKeys(translateSessionKeysAbpV10xSDK(abp));
        return newAbp;
    }

    static software.amazon.iotwireless.wirelessdevice.AbpV11
    translateAbpV11xSDK(software.amazon.awssdk.services.iotwireless.model.LoRaWANDevice model) {
        software.amazon.awssdk.services.iotwireless.model.AbpV1_1 abp =
                model.abpV1_1();
        software.amazon.iotwireless.wirelessdevice.AbpV11 newAbp =
                new software.amazon.iotwireless.wirelessdevice.AbpV11();
        newAbp.setDevAddr(abp.devAddr());
        newAbp.setSessionKeys(translateSessionKeysAbpV11SDK(abp));
        return newAbp;
    }

    static software.amazon.iotwireless.wirelessdevice.LoRaWANDevice
    translateToLoRaWANDeviceSDK(final software.amazon.awssdk.services.iotwireless.model.LoRaWANDevice device) {
        software.amazon.iotwireless.wirelessdevice.LoRaWANDevice newDevice =
                new software.amazon.iotwireless.wirelessdevice.LoRaWANDevice();
        newDevice.setDevEui(device.devEui());
        newDevice.setDeviceProfileId(device.deviceProfileId());
        newDevice.setServiceProfileId(device.serviceProfileId());
        if (device.otaaV1_0_x() != null) {
            newDevice.setOtaaV10x(translateOtaaV10xSDK(device));
        }
        if (device.otaaV1_1() != null) {
            newDevice.setOtaaV11(translateOtaaV11SDK(device));
        }
        if (device.abpV1_0_x() != null) {
            newDevice.setAbpV10x(translateAbpV10xSDK(device));
        }
        if (device.abpV1_1() != null) {
            newDevice.setAbpV11(translateAbpV11xSDK(device));
        }
        return newDevice;
    }

    static CreateWirelessDeviceRequest translateToCreateRequest(final ResourceModel model, String clientRequestToken) {
        Collection<software.amazon.iotwireless.wirelessdevice.Tag> tags = null;
        if (model.getTags() != null) {
            tags = model.getTags().stream()
                    .collect(Collectors.toSet());
        }
        return CreateWirelessDeviceRequest.builder()
                .type(model.getType())
                .name(model.getName())
                .description(model.getDescription())
                .destinationName(model.getDestinationName())
                .clientRequestToken(clientRequestToken)
                .loRaWAN(translateToLoRaDevice(model))
                .tags(translateTag(tags))
                .build();
    }

    static GetWirelessDeviceRequest translateToReadRequest(final ResourceModel model) {
        return GetWirelessDeviceRequest.builder()
                .identifier(model.getId())
                .identifierType(BaseHandlerStd.IdType.WirelessDeviceId.toString())
                .build();
    }

    static DeleteWirelessDeviceRequest translateToDeleteRequest(final ResourceModel model) {
        return DeleteWirelessDeviceRequest.builder()
                .id(model.getId())
                .build();
    }

    static DisassociateWirelessDeviceFromThingRequest translateToDisassociateRequest(final ResourceModel model) {
        return DisassociateWirelessDeviceFromThingRequest.builder()
                .id(model.getId())
                .build();
    }

    static UpdateWirelessDeviceRequest translateToFirstUpdateRequest(final ResourceModel model) {
        return UpdateWirelessDeviceRequest.builder()
                .name(model.getName())
                .description(model.getDescription())
                .id(model.getId())
                .build();
    }

    static AssociateWirelessDeviceWithThingRequest associateWirelessDeviceWithThing(final ResourceModel model) {
        return AssociateWirelessDeviceWithThingRequest.builder()
                .thingArn(model.getThingArn())
                .id(model.getId())
                .build();
    }

    static ListWirelessDevicesRequest translateToListRequest(final String nextToken) {
        return ListWirelessDevicesRequest.builder()
                .nextToken(nextToken)
                .build();
    }

    //Returned at the end of Create, Read, Update handlers to make sure they all return the same thing
    static ResourceModel setModel(final ResourceModel model) {
        return ResourceModel.builder()
                .type(model.getType())
                .name(model.getName())
                .id(model.getId())
                .description(model.getDescription())
                .destinationName(model.getDestinationName())
                .loRaWAN(model.getLoRaWAN())
                .arn(model.getArn())
                .thingArn(model.getThingArn())
                .tags(model.getTags())
                .build();
    }

}
