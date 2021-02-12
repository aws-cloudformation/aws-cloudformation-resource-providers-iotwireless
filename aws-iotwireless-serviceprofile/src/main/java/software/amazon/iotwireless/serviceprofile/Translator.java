package software.amazon.iotwireless.serviceprofile;

import software.amazon.awssdk.services.iotwireless.model.CreateServiceProfileRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteServiceProfileRequest;
import software.amazon.awssdk.services.iotwireless.model.GetServiceProfileRequest;
import software.amazon.awssdk.services.iotwireless.model.ListServiceProfilesRequest;
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
    translateTag(final Collection<software.amazon.iotwireless.serviceprofile.Tag> tags) {
        Collection<software.amazon.awssdk.services.iotwireless.model.Tag> newTagCollection =
                new HashSet<software.amazon.awssdk.services.iotwireless.model.Tag>();
        if (tags == null) {
            return newTagCollection;
        }
        for (software.amazon.iotwireless.serviceprofile.Tag tag : tags) {
            software.amazon.awssdk.services.iotwireless.model.Tag newTag = software.amazon.awssdk.services.iotwireless.model.Tag.builder()
                    .key(tag.getKey())
                    .value(tag.getValue())
                    .build();
            newTagCollection.add(newTag);
        }
        return newTagCollection;
    }

    //Translate from SDK LoRaWANServiceProfile to ResourceModel LoRaWANServiceProfile
    static software.amazon.iotwireless.serviceprofile.LoRaWANGetServiceProfileInfo
    translateFromLoRaSDK(final software.amazon.awssdk.services.iotwireless.model.LoRaWANGetServiceProfileInfo profile) {
        software.amazon.iotwireless.serviceprofile.LoRaWANGetServiceProfileInfo newProfile
                = new software.amazon.iotwireless.serviceprofile.LoRaWANGetServiceProfileInfo();
        newProfile.setUlRate(profile.ulRate());
        newProfile.setUlBucketSize(profile.ulBucketSize());
        newProfile.setUlRatePolicy(profile.ulRatePolicy());
        newProfile.setDlRate(profile.dlRate());
        newProfile.setDlBucketSize(profile.dlBucketSize());
        newProfile.setDlRatePolicy(profile.dlRatePolicy());
        newProfile.setAddGwMetadata(profile.addGwMetadata());
        newProfile.setDevStatusReqFreq(profile.devStatusReqFreq());
        newProfile.setReportDevStatusBattery(profile.reportDevStatusBattery());
        newProfile.setReportDevStatusMargin(profile.reportDevStatusMargin());
        newProfile.setDrMin(profile.drMin());
        newProfile.setDrMax(profile.drMax());
        newProfile.setChannelMask(profile.channelMask());
        newProfile.setPrAllowed(profile.prAllowed());
        newProfile.setHrAllowed(profile.hrAllowed());
        newProfile.setRaAllowed(profile.raAllowed());
        newProfile.setNwkGeoLoc(profile.nwkGeoLoc());
        newProfile.setTargetPer(profile.targetPer());
        newProfile.setMinGwDiversity(profile.minGwDiversity());
        return newProfile;
    }

    //Translate from ResourceModel LoRaWANServiceProfile to SDK LoRaWANServiceProfile
    static software.amazon.awssdk.services.iotwireless.model.LoRaWANServiceProfile
    translateFromLoRa(final ResourceModel model) {
        if ( model.getLoRaWAN() == null ) {
            return null;
        }
        software.amazon.iotwireless.serviceprofile.LoRaWANServiceProfile gateway =
                model.getLoRaWAN();
        return software.amazon.awssdk.services.iotwireless.model.LoRaWANServiceProfile.builder()
                .addGwMetadata(gateway.getAddGwMetadata())
                .build();
    }

    static CreateServiceProfileRequest translateToCreateRequest(final ResourceModel model, String clientRequestToken) {
        Collection<software.amazon.iotwireless.serviceprofile.Tag> tags = null;
        if (model.getTags() != null) {
            tags = model.getTags().stream()
                    .collect(Collectors.toSet());
        }
        return CreateServiceProfileRequest.builder()
                .name(model.getName())
                .loRaWAN(translateFromLoRa(model))
                .tags(translateTag(tags))
                .clientRequestToken(clientRequestToken)
                .build();
    }

    static GetServiceProfileRequest translateToReadRequest(final ResourceModel model) {
        return GetServiceProfileRequest.builder()
                .id(model.getId())
                .build();
    }

    static DeleteServiceProfileRequest translateToDeleteRequest(final ResourceModel model) {
        return DeleteServiceProfileRequest.builder()
                .id(model.getId())
                .build();
    }

    static ListServiceProfilesRequest translateToListRequest(final String nextToken) {
        return ListServiceProfilesRequest.builder()
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
