package software.amazon.iotwireless.wirelessdeviceimporttask;

import com.google.common.collect.Lists;
import software.amazon.awssdk.awscore.AwsRequest;
import software.amazon.awssdk.awscore.AwsResponse;
import software.amazon.awssdk.services.iotwireless.model.SidewalkStartImportInfo;
import software.amazon.awssdk.services.iotwireless.model.SidewalkUpdateImportInfo;
import software.amazon.awssdk.services.iotwireless.model.SidewalkGetStartImportInfo;
import software.amazon.awssdk.services.iotwireless.model.ListTagsForResourceRequest;
import software.amazon.awssdk.services.iotwireless.model.SidewalkSingleStartImportInfo;
import software.amazon.awssdk.services.iotwireless.model.GetWirelessDeviceImportTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.ListWirelessDeviceImportTasksRequest;
import software.amazon.awssdk.services.iotwireless.model.StartWirelessDeviceImportTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.DeleteWirelessDeviceImportTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateWirelessDeviceImportTaskRequest;
import software.amazon.awssdk.services.iotwireless.model.StartSingleWirelessDeviceImportTaskRequest;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is a centralized placeholder for
 *  - api request construction
 *  - object translation to/from aws sdk
 *  - resource model construction for read/list handlers
 */

public class Translator {

    static final String UNSET_VALUE = "UNSET_VALUE";
    /**
     * Returned at the end of Create, Read, Update handlers to make sure they all return the same thing
     * @param model for all resources
     * @return ResourceModel
     */
    static ResourceModel setModel(final ResourceModel model) {
        return ResourceModel.builder()
                .id(model.getId())
                .arn(model.getArn())
                .destinationName(model.getDestinationName())
                .creationDate(model.getCreationDate())
                .sidewalk(model.getSidewalk())
                .status(model.getStatus())
                .statusReason(model.getStatusReason() == null? UNSET_VALUE : model.getStatusReason())
                .initializedImportedDevicesCount(model.getInitializedImportedDevicesCount())
                .pendingImportedDevicesCount(model.getPendingImportedDevicesCount())
                .onboardedImportedDevicesCount(model.getOnboardedImportedDevicesCount())
                .failedImportedDevicesCount(model.getFailedImportedDevicesCount())
                .tags(model.getTags())
                .build();
    }

    /**
     * Translate IoTWireless sdk translateToSidewalkResourceModel model to CFN resource model
     * @param source from list and read
     * @return Sidewalk
     */
    static Sidewalk translateToSidewalkResourceModel (final SidewalkGetStartImportInfo source){
        if(source == null) {
            return null;
        }
        Sidewalk sidewalk = new Sidewalk();
        sidewalk.setDeviceCreationFileList(source.deviceCreationFileList());
        sidewalk.setRole(source.role());
        return sidewalk;
    }

    /**
     * Translate CFN Resource Sidewalk model to IoTWireless sdk model
     * @param sidewalk from CreateHandler
     * @return SidewalkStartImportInfo
     */
    static SidewalkStartImportInfo translateFromSidewalkResourceModel(final Sidewalk sidewalk) {
        if(sidewalk == null) {
            return null;
        }
        return SidewalkStartImportInfo.builder()
                .deviceCreationFile(sidewalk.getDeviceCreationFile())
                .role(sidewalk.getRole())
                .build();
    }

    /**
     * Request to create a resource
     * @param request from create handler
     * @return StartWirelessDeviceImportTaskRequest
     */
    static StartWirelessDeviceImportTaskRequest translateToCreateRequest(final ResourceHandlerRequest<ResourceModel> request) {
        final ResourceModel model = request.getDesiredResourceState();
        return StartWirelessDeviceImportTaskRequest.builder()
                .destinationName(model.getDestinationName())
                .clientRequestToken(request.getClientRequestToken())
                .sidewalk(translateFromSidewalkResourceModel(model.getSidewalk()))
                .tags(TagHelper.generateTagsForCreate(request)
                        .entrySet().stream()
                        .map(tag -> software.amazon.awssdk.services.iotwireless.model.Tag.builder()
                                .key(tag.getKey())
                                .value(tag.getValue())
                                .build())
                        .collect(Collectors.toCollection(ArrayList::new)))
                .build();
    }

    /**
     * Translate CFN Resource Sidewalk model to IoTWireless sdk model
     * @param sidewalk is from create handler
     * @return SidewalkSingleStartImportInfo
     */
    static SidewalkSingleStartImportInfo translateFromSingleSidewalkResourceModel(final Sidewalk sidewalk) {
        if(sidewalk == null) {
            return null;
        }
        return SidewalkSingleStartImportInfo.builder()
                .sidewalkManufacturingSn(sidewalk.getSidewalkManufacturingSn())
                .build();
    }

    /**
     * Request to create a resource
     * @param request is from create handler
     * @return StartSingleWirelessDeviceImportTaskRequest
     */
    static StartSingleWirelessDeviceImportTaskRequest translateToSingleCreateRequest(final ResourceHandlerRequest<ResourceModel> request) {
        final ResourceModel model = request.getDesiredResourceState();
        return StartSingleWirelessDeviceImportTaskRequest.builder()
                .destinationName(model.getDestinationName())
                .clientRequestToken(request.getClientRequestToken())
                .sidewalk(translateFromSingleSidewalkResourceModel(model.getSidewalk()))
                .tags(TagHelper.generateTagsForCreate(request)
                        .entrySet().stream()
                        .map(tag -> software.amazon.awssdk.services.iotwireless.model.Tag.builder()
                                .key(tag.getKey())
                                .value(tag.getValue())
                                .build())
                        .collect(Collectors.toCollection(ArrayList::new)))
                .build();
    }

    /**
     * Request to read a resource
     * @param model from read handler
     * @return GetWirelessDeviceImportTaskRequest
     */
    static GetWirelessDeviceImportTaskRequest translateToReadRequest(final ResourceModel model) {
        return GetWirelessDeviceImportTaskRequest.builder().id(model.getId()).build();
    }

    /**
     * Request to delete a resource
     * @param model from delete handler
     * @return DeleteWirelessDeviceImportTaskRequest
     */
    static DeleteWirelessDeviceImportTaskRequest translateToDeleteRequest(final ResourceModel model) {
        if(model == null) {
            return null;
        }
        return DeleteWirelessDeviceImportTaskRequest.builder().id(model.getId()).build();
    }

    /**
     * Translate CFN Resource Sidewalk model to *** sdk model
     * @param sidewalk from Update handler
     * @return SidewalkUpdateImportInfo
     */
    static SidewalkUpdateImportInfo translateFromUpdateSidewalkResourceModel(final Sidewalk sidewalk) {
        if(sidewalk == null) {
            return null;
        }
        return SidewalkUpdateImportInfo.builder()
                .deviceCreationFile(sidewalk.getDeviceCreationFile())
                .build();
    }

    /**
     * help to update properties of a previously created resource
     * @param request from update handler
     * @return UpdateWirelessDeviceImportTaskRequest
     */
    static UpdateWirelessDeviceImportTaskRequest translateToUpdateRequest(ResourceHandlerRequest<ResourceModel> request) {
        final ResourceModel model = request.getDesiredResourceState();
        return UpdateWirelessDeviceImportTaskRequest.builder()
                .id(model.getId())
                .sidewalk(translateFromUpdateSidewalkResourceModel(model.getSidewalk()))
                .build();
    }

    /**
     * Request to list resources
     * @param nextToken token passed to the aws service list resources request
     * @return ListWirelessDeviceImportTasksRequest
     */
    static ListWirelessDeviceImportTasksRequest translateToListRequest(final String nextToken) {
        return ListWirelessDeviceImportTasksRequest.builder().nextToken(nextToken).build();
    }

    /**
     * Translates resource objects from sdk into a resource model (primary identifier only)
     * @param awsResponse the aws service describe resource response
     * @return list of resource models
     */
    static List<ResourceModel> translateFromListRequest(final AwsResponse awsResponse) {
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/2077c92299aeb9a68ae8f4418b5e932b12a8b186/aws-logs-loggroup/src/main/java/com/aws/logs/loggroup/Translator.java#L75-L82
        return streamOfOrEmpty(Lists.newArrayList())
                .map(resource -> ResourceModel.builder()
                        // include only primary identifier
                        .build())
                .collect(Collectors.toList());
    }

    private static <T> Stream<T> streamOfOrEmpty(final Collection<T> collection) {
        return Optional.ofNullable(collection)
                .map(Collection::stream)
                .orElseGet(Stream::empty);
    }

    /**
     * Request to add tags to a resource
     * @param model resource model
     * @return awsRequest the aws service request to create a resource
     */
    static AwsRequest tagResourceRequest(final ResourceModel model, final Map<String, String> addedTags) {
        final AwsRequest awsRequest = null;
        // TODO: construct a request
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/2077c92299aeb9a68ae8f4418b5e932b12a8b186/aws-logs-loggroup/src/main/java/com/aws/logs/loggroup/Translator.java#L39-L43
        return awsRequest;
    }

    /**
     * Request to add tags to a resource
     * @param model resource model
     * @return awsRequest the aws service request to create a resource
     */
    static AwsRequest untagResourceRequest(final ResourceModel model, final Set<String> removedTags) {
        final AwsRequest awsRequest = null;
        // TODO: construct a request
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/2077c92299aeb9a68ae8f4418b5e932b12a8b186/aws-logs-loggroup/src/main/java/com/aws/logs/loggroup/Translator.java#L39-L43
        return awsRequest;
    }

    /**
     * Request list tags for resource
     * @param model resource model
     * @return awsRequest the aws service request to list tag for a resource
     */
    static ListTagsForResourceRequest translateToListTagForResourceRequest(final ResourceModel model) {
        return ListTagsForResourceRequest.builder().resourceArn(model.getArn()).build();
    }
}