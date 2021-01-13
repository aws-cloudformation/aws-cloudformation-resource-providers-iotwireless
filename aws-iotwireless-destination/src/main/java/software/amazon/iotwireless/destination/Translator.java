package software.amazon.iotwireless.destination;

import software.amazon.awssdk.services.iotwireless.model.CreateDestinationRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteDestinationRequest;
import software.amazon.awssdk.services.iotwireless.model.GetDestinationRequest;
import software.amazon.awssdk.services.iotwireless.model.ListDestinationsRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateDestinationRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Translator {
    //Translate from Resource Model Tags to SDK Tags
    static Collection<software.amazon.awssdk.services.iotwireless.model.Tag>
    translateTag(final Collection<software.amazon.iotwireless.destination.Tag> tags) {
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
        Collection<software.amazon.iotwireless.destination.Tag> tags = null;
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

    static ListDestinationsRequest translateToListRequest(final String nextToken) {
        return ListDestinationsRequest.builder()
                .nextToken(nextToken)
                .build();
    }

}
