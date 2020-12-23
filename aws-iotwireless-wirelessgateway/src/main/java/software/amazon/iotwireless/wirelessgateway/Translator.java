package software.amazon.iotwireless.wirelessgateway;

import software.amazon.awssdk.services.iotwireless.model.*;
import jdk.jfr.internal.Logger;
import software.amazon.awssdk.awscore.AwsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;


import java.io.IOException;
import java.util.*;


import javax.annotation.Resource;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is a centralized placeholder for
 * - api request construction
 * - object translation to/from aws sdk
 * - resource model construction for read/list handlers
 */

public class Translator {

    //Translate from ResourceModel LoRaWANGateway to SDK LoRaWANGateway
    static software.amazon.awssdk.services.iotwireless.model.LoRaWANGateway
    translateToLoRaWANGateway(final ResourceModel model) {
        if (model.getLoRaWANGateway() == null) {
            return null;
        }
        software.amazon.iotwireless.wirelessgateway.LoRaWANGateway gateway = model.getLoRaWANGateway();
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
    translateTag(final Collection<Tag> tags) {
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
        Collection<Tag> tags = null;
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
    static ResourceModel unsetWriteOnly(final ResourceModel model) {
        return ResourceModel.builder()
                .name(model.getName())
                .id(model.getId())
                .description(model.getDescription())
                .loRaWANGateway(model.getLoRaWANGateway())
                .arn(model.getArn())
                .thingArn(model.getThingArn())
                .tags(model.getTags())
                .build();
    }
}
