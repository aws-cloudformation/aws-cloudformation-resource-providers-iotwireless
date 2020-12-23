package software.amazon.iotwireless.destination;

import software.amazon.awssdk.services.iotwireless.model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Translator {
    //Translate from Resource Model Tags to SDK Tags
    static Collection<software.amazon.awssdk.services.iotwireless.model.Tag>
    translateTag(final Collection<Tag> tags) {
        Collection<software.amazon.awssdk.services.iotwireless.model.Tag> newTagCollection =
                new HashSet<software.amazon.awssdk.services.iotwireless.model.Tag>();
        if (tags == null) {
            return newTagCollection;
        }
        for (software.amazon.iotwireless.destination.Tag tag : tags) {
            software.amazon.awssdk.services.iotwireless.model.Tag newTag = software.amazon.awssdk.services.iotwireless.model.Tag.builder()
                    .key(tag.getKey())
                    .value(tag.getValue())
                    .build();
            newTagCollection.add(newTag);
        }
        return newTagCollection;
    }

    static CreateDestinationRequest translateToCreateRequest(final ResourceModel model, String clientRequestToken) {
        Collection<Tag> tags = null;
        if (model.getTags() != null) {
            tags = model.getTags().stream()
                    .collect(Collectors.toSet());
        }
        return CreateDestinationRequest.builder()
                .name(model.getName())
                .expressionType(model.getExpressionType())
                .expression(model.getExpression())
                .description(model.getDescription())
                .tags(translateTag(tags))
                .roleArn(model.getRoleArn())
                .clientRequestToken(clientRequestToken)
                .build();
    }

    static GetDestinationRequest translateToReadRequest(final ResourceModel model) {
        return GetDestinationRequest.builder()
                .name(model.getName())
                .build();

    }

    static DeleteDestinationRequest translateToDeleteRequest(final ResourceModel model) {
        return DeleteDestinationRequest.builder()
                .name(model.getName())
                .build();
    }

    static UpdateDestinationRequest translateToFirstUpdateRequest(final ResourceModel model) {
        return UpdateDestinationRequest.builder()
                .name(model.getName())
                .expressionType(model.getExpressionType())
                .expression(model.getExpression())
                .description(model.getDescription())
                .roleArn(model.getRoleArn())
                .build();
    }

    static ListDestinationsRequest translateToListRequest(ResourceModel model) {
        return ListDestinationsRequest.builder()
                .nextToken(model.getNextToken())
                .build();
    }

    //Returned at the end of Create, Read, Update handlers to make sure they all return the same thing
    static ResourceModel unsetWriteOnly(final ResourceModel model) {
        return ResourceModel.builder()
                .name(model.getName())
                .expressionType(model.getExpressionType())
                .expression(model.getExpression())
                .description(model.getDescription())
                .roleArn(model.getRoleArn())
                .tags(model.getTags())
                .build();
    }

}
