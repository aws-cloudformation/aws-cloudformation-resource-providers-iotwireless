package software.amazon.iotwireless.wirelessgateway;

import software.amazon.awssdk.services.iotwireless.model.CreateWirelessGatewayRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteWirelessGatewayRequest;
import software.amazon.awssdk.services.iotwireless.model.DisassociateWirelessGatewayFromThingRequest;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessGatewayRequest;
import software.amazon.awssdk.services.iotwireless.model.AssociateWirelessGatewayWithThingRequest;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessGatewaysRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateWirelessGatewayRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Translator {

    //Translate from ResourceModel LoRaWANGateway to SDK LoRaWANGateway
    static software.amazon.awssdk.services.iotwireless.model.LoRaWANGateway
    translateToLoRaWANGateway(final ResourceModel model) {
        if (model.getLoRaWAN() == null) {
            return null;
        }
        software.amazon.iotwireless.wirelessgateway.LoRaWANGateway gateway = model.getLoRaWAN();
        return software.amazon.awssdk.services.iotwireless.model.LoRaWANGateway.builder()
                .rfRegion(gateway.getRfRegion())
                .gatewayEui(gateway.getGatewayEui())
                .build();
    }

    //Translate from SDK LoRaWANGateway to ResourceModel LoRaWANGateway
    static software.amazon.iotwireless.wirelessgateway.LoRaWANGateway
    translateToLoRaWAN(final software.amazon.awssdk.services.iotwireless.model.LoRaWANGateway model) {
        software.amazon.iotwireless.wirelessgateway.LoRaWANGateway newGateway =
                new software.amazon.iotwireless.wirelessgateway.LoRaWANGateway();
        newGateway.setRfRegion(model.rfRegion());
        newGateway.setGatewayEui(model.gatewayEui());
        return newGateway;
    }

    //Translate from ResourceModel Tag to SDK Tag
    static Collection<software.amazon.awssdk.services.iotwireless.model.Tag>
    translateTag(final Collection<software.amazon.iotwireless.wirelessgateway.Tag> tags) {
        Collection<software.amazon.awssdk.services.iotwireless.model.Tag> newTagCollection =
                new HashSet<software.amazon.awssdk.services.iotwireless.model.Tag>();
        if (tags == null) {
            return newTagCollection;
        }
        for (software.amazon.iotwireless.wirelessgateway.Tag tag : tags) {
            software.amazon.awssdk.services.iotwireless.model.Tag newTag =
                    software.amazon.awssdk.services.iotwireless.model.Tag.builder()
                            .key(tag.getKey())
                            .value(tag.getValue())
                            .build();
            newTagCollection.add(newTag);
        }

        return newTagCollection;
    }

    static CreateWirelessGatewayRequest translateToCreateRequest(final ResourceModel model, String clientRequestToken) {
        Collection<software.amazon.iotwireless.wirelessgateway.Tag> tags = null;
        if (model.getTags() != null) {
            tags = model.getTags().stream()
                    .collect(Collectors.toSet());
        }
        return CreateWirelessGatewayRequest.builder()
                .name(model.getName())
                .description(model.getDescription())
                .loRaWAN(translateToLoRaWANGateway(model))
                .clientRequestToken(clientRequestToken)
                .tags(translateTag(tags))
                .build();
    }

    static GetWirelessGatewayRequest translateToReadRequest(final ResourceModel model) {
        return GetWirelessGatewayRequest.builder()
                .identifier(model.getId())
                .identifierType(BaseHandlerStd.IdType.WirelessGatewayId.toString())
                .build();
    }

    static DeleteWirelessGatewayRequest translateToDeleteRequest(final ResourceModel model) {
        return DeleteWirelessGatewayRequest.builder()
                .id(model.getId())
                .build();
    }

    static DisassociateWirelessGatewayFromThingRequest translateToDisassociateRequest(final ResourceModel model) {
        return DisassociateWirelessGatewayFromThingRequest.builder()
                .id(model.getId())
                .build();
    }

    static UpdateWirelessGatewayRequest translateToFirstUpdateRequest(final ResourceModel model) {
        return UpdateWirelessGatewayRequest.builder()
                .name(model.getName())
                .description(model.getDescription())
                .id(model.getId())
                .build();
    }

    static AssociateWirelessGatewayWithThingRequest associateWirelessGatewayWithThing(final ResourceModel model) {
        return AssociateWirelessGatewayWithThingRequest.builder()
                .thingArn(model.getThingArn())
                .id(model.getId())
                .build();
    }

    // List handler
    static ListWirelessGatewaysRequest translateToListRequest(final String token) {
        return ListWirelessGatewaysRequest.builder()
                .nextToken(token)
                .build();
    }

    //Returned at the end of Create, Read, Update handlers to make sure they all return the same thing
    static ResourceModel setModel(final ResourceModel model) {
        return ResourceModel.builder()
                .name(model.getName())
                .id(model.getId())
                .description(model.getDescription())
                .loRaWAN(model.getLoRaWAN())
                .arn(model.getArn())
                .thingArn(model.getThingArn())
                .tags(model.getTags())
                .build();
    }
}
