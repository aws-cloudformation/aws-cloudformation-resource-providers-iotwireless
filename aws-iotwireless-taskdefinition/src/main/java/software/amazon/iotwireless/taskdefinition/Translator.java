package software.amazon.iotwireless.taskdefinition;

import software.amazon.awssdk.services.iotwireless.model.CreateWirelessGatewayTaskDefinitionRequest;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessGatewayTaskDefinitionRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteWirelessGatewayTaskDefinitionRequest;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessGatewayTaskDefinitionsRequest;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Translator {
    //TODO : Add tagging translation if TaskDefinition support tagging

    //Translate from SDK LoRaWANUpdateGatewayTaskEntry to Resource Model LoRaWANUpdateGatewayTaskEntry
    static  software.amazon.iotwireless.taskdefinition.LoRaWANUpdateGatewayTaskEntry
    translateLoRaWANUpdateGatewayTaskEntry(final software.amazon.awssdk.services.iotwireless.model.LoRaWANUpdateGatewayTaskEntry sdkLoRaWAN) {
        software.amazon.iotwireless.taskdefinition.LoRaWANUpdateGatewayTaskEntry resourceLoRaWAN =
                new  software.amazon.iotwireless.taskdefinition.LoRaWANUpdateGatewayTaskEntry();
        resourceLoRaWAN.setCurrentVersion(translateToCurVersion(sdkLoRaWAN.currentVersion()));
        resourceLoRaWAN.setUpdateVersion(translateToUpVersion(sdkLoRaWAN.updateVersion()));
        return resourceLoRaWAN;
    }

    //Translate from SDK CurrentLoRaWANGatewayVersion to Resource Model CurrentLoRaWANGatewayVersion
    static software.amazon.iotwireless.taskdefinition.LoRaWANGatewayVersion
    translateToCurVersion(final software.amazon.awssdk.services.iotwireless.model.LoRaWANGatewayVersion sdkUpdate) {
        software.amazon.iotwireless.taskdefinition.LoRaWANGatewayVersion resourceUpdate =
                new software.amazon.iotwireless.taskdefinition.LoRaWANGatewayVersion();
        resourceUpdate.setPackageVersion(sdkUpdate.packageVersion());
        resourceUpdate.setModel(sdkUpdate.model());
        resourceUpdate.setStation(sdkUpdate.station());
        return resourceUpdate;
    }

    //Translate from SDK UpdateLoRaWANGatewayVersion to Resource Model UpdateLoRaWANGatewayVersion
    static software.amazon.iotwireless.taskdefinition.LoRaWANGatewayVersion
    translateToUpVersion(final software.amazon.awssdk.services.iotwireless.model.LoRaWANGatewayVersion sdkUpdate) {
        software.amazon.iotwireless.taskdefinition.LoRaWANGatewayVersion resourceUpdate =
                new software.amazon.iotwireless.taskdefinition.LoRaWANGatewayVersion();
        resourceUpdate.setPackageVersion(sdkUpdate.packageVersion());
        resourceUpdate.setModel(sdkUpdate.model());
        resourceUpdate.setStation(sdkUpdate.station());
        return resourceUpdate;
    }

    static software.amazon.iotwireless.taskdefinition.LoRaWANUpdateGatewayTaskCreate
    translateToLoRaWAN(final software.amazon.awssdk.services.iotwireless.model.UpdateWirelessGatewayTaskCreate model) {
        software.amazon.awssdk.services.iotwireless.model.LoRaWANUpdateGatewayTaskCreate sdkUpdate =
                model.loRaWAN();
        software.amazon.iotwireless.taskdefinition.LoRaWANUpdateGatewayTaskCreate resourceUpdate =
                new software.amazon.iotwireless.taskdefinition.LoRaWANUpdateGatewayTaskCreate();
        resourceUpdate.setUpdateSignature(sdkUpdate.updateSignature());
        resourceUpdate.setCurrentVersion(translateToCurVersion(sdkUpdate.currentVersion()));
        resourceUpdate.setUpdateVersion(translateToUpVersion(sdkUpdate.updateVersion()));
        return resourceUpdate;
    }

    static software.amazon.iotwireless.taskdefinition.UpdateWirelessGatewayTaskCreate
    translateToUpdate(final software.amazon.awssdk.services.iotwireless.model.UpdateWirelessGatewayTaskCreate model) {
        software.amazon.iotwireless.taskdefinition.UpdateWirelessGatewayTaskCreate newUpdate=
                new software.amazon.iotwireless.taskdefinition.UpdateWirelessGatewayTaskCreate();
        newUpdate.setUpdateDataSource(model.updateDataSource());
        newUpdate.setUpdateDataRole(model.updateDataRole());
        if(model.loRaWAN() != null){
            newUpdate.setLoRaWAN(translateToLoRaWAN(model));
        }
        return newUpdate;
    }

    //Translate from ResourceModel LoRaWANGatewayVersion to SDK LoRaWANGatewayVersion
    static software.amazon.awssdk.services.iotwireless.model.LoRaWANGatewayVersion
    translateVersion(software.amazon.iotwireless.taskdefinition.LoRaWANUpdateGatewayTaskCreate model, boolean curVersion) {
        if (curVersion && model.getCurrentVersion() == null) {
            return null;
        }
        if (curVersion == false && model.getUpdateVersion() == null) {
            return null;
        }

        if (curVersion) {
            software.amazon.iotwireless.taskdefinition.LoRaWANGatewayVersion version =
                    model.getCurrentVersion();

            return software.amazon.awssdk.services.iotwireless.model.LoRaWANGatewayVersion
                    .builder()
                    .packageVersion(version.getPackageVersion())
                    .model(version.getModel())
                    .station(version.getStation())
                    .build();
        } else {
            software.amazon.iotwireless.taskdefinition.LoRaWANGatewayVersion version =
                    model.getUpdateVersion();

            return software.amazon.awssdk.services.iotwireless.model.LoRaWANGatewayVersion
                    .builder()
                    .packageVersion(version.getPackageVersion())
                    .model(version.getModel())
                    .station(version.getStation())
                    .build();
        }
    }

    //Translate from ResourceModel LoRaWANUpdateGatewayTaskCreate to SDK LoRaWANUpdateGatewayTaskCreate
    static software.amazon.awssdk.services.iotwireless.model.LoRaWANUpdateGatewayTaskCreate
    translateLoRaWAN(software.amazon.iotwireless.taskdefinition.UpdateWirelessGatewayTaskCreate model) {
        if (model.getLoRaWAN() == null) {
            return null;
        }

        software.amazon.iotwireless.taskdefinition.LoRaWANUpdateGatewayTaskCreate loRaWANTaskCreate =
                model.getLoRaWAN();

        return software.amazon.awssdk.services.iotwireless.model.LoRaWANUpdateGatewayTaskCreate
                .builder()
                .updateSignature(loRaWANTaskCreate.getUpdateSignature())
                .currentVersion(translateVersion(loRaWANTaskCreate, true))
                .sigKeyCrc(loRaWANTaskCreate.getSigKeyCrc())
                .updateVersion(translateVersion(loRaWANTaskCreate, false))
                .build();
    }

    //Translate from ResourceModel UpdateWirelessGatewayTaskCreate to SDK UpdateWirelessGatewayTaskCreate
    static software.amazon.awssdk.services.iotwireless.model.UpdateWirelessGatewayTaskCreate
    translateUpdate(final ResourceModel model) {
        if (model.getUpdate() == null) {
            return null;
        }
        software.amazon.iotwireless.taskdefinition.UpdateWirelessGatewayTaskCreate updateTaskCreate =
                model.getUpdate();

        return software.amazon.awssdk.services.iotwireless.model.UpdateWirelessGatewayTaskCreate.builder()
                .updateDataSource(updateTaskCreate.getUpdateDataSource())
                .updateDataRole(updateTaskCreate.getUpdateDataRole())
                .loRaWAN(translateLoRaWAN(updateTaskCreate))
                .build();
    }

    //Translate from ResourceModel Tag to SDK Tag
    static Collection<software.amazon.awssdk.services.iotwireless.model.Tag>
    translateTag(final Collection<software.amazon.iotwireless.taskdefinition.Tag> tags) {
        Collection<software.amazon.awssdk.services.iotwireless.model.Tag> newTagCollection =
                new HashSet<software.amazon.awssdk.services.iotwireless.model.Tag>();
        if (tags == null) {
            return newTagCollection;
        }
        for (software.amazon.iotwireless.taskdefinition.Tag tag : tags) {
            software.amazon.awssdk.services.iotwireless.model.Tag newTag =
                    software.amazon.awssdk.services.iotwireless.model.Tag.builder()
                            .key(tag.getKey())
                            .value(tag.getValue())
                            .build();
            newTagCollection.add(newTag);
        }

        return newTagCollection;
    }

    static CreateWirelessGatewayTaskDefinitionRequest translateToCreateRequest(final ResourceModel model, String clientRequestToken) {
        Collection<software.amazon.iotwireless.taskdefinition.Tag> tags = null;
        if (model.getTags() != null) {
            tags = model.getTags().stream()
                    .collect(Collectors.toSet());
        }
        return CreateWirelessGatewayTaskDefinitionRequest.builder()
                .name(model.getName())
                .autoCreateTasks(model.getAutoCreateTasks())
                .update(translateUpdate(model))
                .clientRequestToken(clientRequestToken)
                .tags(translateTag(tags))
                .build();
    }

    static GetWirelessGatewayTaskDefinitionRequest translateToReadRequest(final ResourceModel model) {
        return GetWirelessGatewayTaskDefinitionRequest.builder()
                .id(model.getId())
                .build();
    }

    static DeleteWirelessGatewayTaskDefinitionRequest translateToDeleteRequest(final ResourceModel model) {
        return DeleteWirelessGatewayTaskDefinitionRequest.builder()
                .id(model.getId())
                .build();
    }

    static ListWirelessGatewayTaskDefinitionsRequest translateToListRequest(final ResourceModel model, final String nextToken) {
        return ListWirelessGatewayTaskDefinitionsRequest.builder()
                .nextToken(nextToken)
                .taskDefinitionType(model.getTaskDefinitionType())
                .build();
    }

    //Returned at the end of Create, Read, Update handlers to make sure they all return the same thing
    static ResourceModel setModel(final ResourceModel model) {
        return ResourceModel.builder()
                .autoCreateTasks(model.getAutoCreateTasks())
                .name(model.getName())
                .update(model.getUpdate())
                .id(model.getId())
                .arn(model.getArn())
                .tags(model.getTags())
                .build();
    }
}
