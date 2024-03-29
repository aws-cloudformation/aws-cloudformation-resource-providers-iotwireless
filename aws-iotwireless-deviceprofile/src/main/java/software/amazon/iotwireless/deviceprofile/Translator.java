package software.amazon.iotwireless.deviceprofile;

import software.amazon.awssdk.services.iotwireless.model.CreateDeviceProfileRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteDeviceProfileRequest;
import software.amazon.awssdk.services.iotwireless.model.GetDeviceProfileRequest;
import software.amazon.awssdk.services.iotwireless.model.ListDeviceProfilesRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Translator {

    //Translate from ResourceModel Tag to SDK Tag
    static Collection<software.amazon.awssdk.services.iotwireless.model.Tag>
    translateTag(final Collection<software.amazon.iotwireless.deviceprofile.Tag> tags) {
        Collection<software.amazon.awssdk.services.iotwireless.model.Tag> newTagCollection =
                new HashSet<software.amazon.awssdk.services.iotwireless.model.Tag>();
        if (tags == null) {
            return newTagCollection;
        }
        for (software.amazon.iotwireless.deviceprofile.Tag tag : tags) {
            software.amazon.awssdk.services.iotwireless.model.Tag newTag =
                    software.amazon.awssdk.services.iotwireless.model.Tag.builder()
                            .key(tag.getKey())
                            .value(tag.getValue())
                            .build();
            newTagCollection.add(newTag);
        }
        return newTagCollection;
    }

    //Translate from SDK LoRaWANDeviceProfile to ResourceModel LoRaWANDeviceProfile
    static software.amazon.iotwireless.deviceprofile.LoRaWANDeviceProfile
    translateFromLoRaSDK(final software.amazon.awssdk.services.iotwireless.model.LoRaWANDeviceProfile profile) {
        software.amazon.iotwireless.deviceprofile.LoRaWANDeviceProfile newProfile
                = new software.amazon.iotwireless.deviceprofile.LoRaWANDeviceProfile();
        newProfile.setSupportsClassB(profile.supportsClassB());
        newProfile.setClassBTimeout(profile.classBTimeout());
        newProfile.setPingSlotPeriod(profile.pingSlotPeriod());
        newProfile.setPingSlotDr(profile.pingSlotDr());
        newProfile.setPingSlotFreq(profile.pingSlotFreq());
        newProfile.setSupportsClassC(profile.supportsClassC());
        newProfile.setMacVersion(profile.macVersion());
        newProfile.setRegParamsRevision(profile.regParamsRevision());
        newProfile.setMaxEirp(profile.maxEirp());
        newProfile.setMaxDutyCycle(profile.maxDutyCycle());
        newProfile.setSupportsJoin(profile.supportsJoin());
        newProfile.setRfRegion(profile.rfRegion());
        newProfile.setSupports32BitFCnt(profile.supports32BitFCnt());
        newProfile.setRxDelay1(profile.rxDelay1());
        newProfile.setRxDrOffset1(profile.rxDrOffset1());
        newProfile.setRxDataRate2(profile.rxDataRate2());
        newProfile.setRxFreq2(profile.rxFreq2());
        newProfile.setFactoryPresetFreqsList(profile.factoryPresetFreqsList());
        return newProfile;
    }

    //Translate from Resource Model LoRaWANDeviceProfile to SDK LoRaWANDeviceProfile
    static software.amazon.awssdk.services.iotwireless.model.LoRaWANDeviceProfile
    translateToLoRa(final ResourceModel model) {
        if (model.getLoRaWAN() == null) {
            return null;
        }
        software.amazon.iotwireless.deviceprofile.LoRaWANDeviceProfile gateway = model.getLoRaWAN();
        return software.amazon.awssdk.services.iotwireless.model.LoRaWANDeviceProfile.builder()
                .supportsClassB(gateway.getSupportsClassB())
                .classBTimeout(gateway.getClassBTimeout())
                .pingSlotPeriod(gateway.getPingSlotPeriod())
                .pingSlotDr(gateway.getPingSlotDr())
                .pingSlotFreq(gateway.getPingSlotFreq())
                .supportsClassC(gateway.getSupportsClassC())
                .macVersion(gateway.getMacVersion())
                .regParamsRevision(gateway.getRegParamsRevision())
                .maxEirp(gateway.getMaxEirp())
                .maxDutyCycle(gateway.getMaxDutyCycle())
                .supportsJoin(gateway.getSupportsJoin())
                .rfRegion(gateway.getRfRegion())
                .supports32BitFCnt(gateway.getSupports32BitFCnt())
                .rxDelay1(gateway.getRxDelay1())
                .rxDrOffset1(gateway.getRxDrOffset1())
                .rxFreq2(gateway.getRxFreq2())
                .rxDataRate2(gateway.getRxDataRate2())
                .factoryPresetFreqsList(gateway.getFactoryPresetFreqsList())
                .build();
    }

    static CreateDeviceProfileRequest translateToCreateRequest(final ResourceModel model, String clientRequestToken) {
        Collection<software.amazon.iotwireless.deviceprofile.Tag> tags = null;
        if (model.getTags() != null) {
            tags = model.getTags().stream()
                    .collect(Collectors.toSet());
        }
        return CreateDeviceProfileRequest.builder()
                .name(model.getName())
                .loRaWAN(translateToLoRa(model))
                .tags(translateTag(tags))
                .clientRequestToken(clientRequestToken)
                .build();
    }

    static GetDeviceProfileRequest translateToReadRequest(final ResourceModel model) {
        return GetDeviceProfileRequest.builder()
                .id(model.getId())
                .build();
    }

    static DeleteDeviceProfileRequest translateToDeleteRequest(final ResourceModel model) {
        return DeleteDeviceProfileRequest.builder()
                .id(model.getId())
                .build();
    }

    static ListDeviceProfilesRequest translateToListRequest(final String nextToken) {
        return ListDeviceProfilesRequest.builder()
                .nextToken(nextToken)
                .build();
    }

    //Returned at the end of Create, Read, Update handlers to make sure they all return the same thing
    static ResourceModel setModel(final ResourceModel model) {
        return ResourceModel.builder()
                .arn(model.getArn())
                .id(model.getId())
                .name(model.getName())
                .loRaWAN(model.getLoRaWAN())
                .tags(model.getTags())
                .build();
    }
}
