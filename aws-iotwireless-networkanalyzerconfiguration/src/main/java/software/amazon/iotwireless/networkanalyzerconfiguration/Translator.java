package software.amazon.iotwireless.networkanalyzerconfiguration;

import software.amazon.awssdk.services.iotwireless.model.CreateNetworkAnalyzerConfigurationRequest;
import software.amazon.awssdk.services.iotwireless.model.CreateNetworkAnalyzerConfigurationResponse;
import software.amazon.awssdk.services.iotwireless.model.DeleteNetworkAnalyzerConfigurationRequest;
import software.amazon.awssdk.services.iotwireless.model.ListTagsForResourceRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateNetworkAnalyzerConfigurationRequest;
import software.amazon.awssdk.services.iotwireless.model.UpdateNetworkAnalyzerConfigurationResponse;
import software.amazon.awssdk.services.iotwireless.model.GetNetworkAnalyzerConfigurationRequest;
import software.amazon.awssdk.services.iotwireless.model.GetNetworkAnalyzerConfigurationResponse;
import software.amazon.awssdk.services.iotwireless.model.ListNetworkAnalyzerConfigurationsRequest;
import com.google.common.collect.Lists;
import software.amazon.awssdk.awscore.AwsRequest;
import software.amazon.awssdk.awscore.AwsResponse;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

  /**
   * Request to create a resource
   * @param request ResourceHander request
   * @return CreateNetworkAnalyzerConfigurationRequest the aws service request to create a resource
   */
  static CreateNetworkAnalyzerConfigurationRequest translateToCreateRequest(final ResourceHandlerRequest<ResourceModel> request) {
    final ResourceModel model = request.getDesiredResourceState();
    return CreateNetworkAnalyzerConfigurationRequest.builder()
           .name(model.getName())
           .description(model.getDescription())
           .clientRequestToken(request.getClientRequestToken())
           .traceContent(translateFromTraceContentResourceModel(model.getTraceContent()))
           .wirelessDevices(model.getWirelessDevices())
           .wirelessGateways(model.getWirelessGateways())
           .tags(TagHelper.generateTagsForCreate(request)
                    .entrySet().stream()
                    .map(tag ->
                              software.amazon.awssdk.services.iotwireless.model.Tag.builder()
                                    .key(tag.getKey())
                                    .value(tag.getValue()).build())
                   .collect(Collectors.toCollection(ArrayList::new)))
            .build();
  }

  /**
   * Translate CFN Resource TraceContent model to IoTWireless sdk model
   * @param traceContent
   * @return
   */
  static software.amazon.awssdk.services.iotwireless.model.TraceContent
  translateFromTraceContentResourceModel( final TraceContent traceContent) {
    if(traceContent == null) {
      return null;
    }
    return software.amazon.awssdk.services.iotwireless.model.TraceContent.builder()
            .wirelessDeviceFrameInfo(traceContent.getWirelessDeviceFrameInfo())
            .logLevel(traceContent.getLogLevel()).build();
  }

  /**
   * Translate IoTWireless sdk TraceContent model to CFN resource model
   * @param source
   * @return
   */
  static TraceContent translateToTraceContentResourceModel (
          final software.amazon.awssdk.services.iotwireless.model.TraceContent source){
    if(source == null) {
      return null;
    }
    TraceContent traceContent = new TraceContent() ;
    traceContent.setWirelessDeviceFrameInfo(source.wirelessDeviceFrameInfoAsString());
    traceContent.setLogLevel(source.logLevelAsString());
    return traceContent;
  }

  /**
   * Request to read a resource
   * @param model resource model
   * @return awsRequest the aws service request to describe a resource
   */
  static GetNetworkAnalyzerConfigurationRequest translateToReadRequest(final ResourceModel model) {
    return GetNetworkAnalyzerConfigurationRequest.builder().configurationName(model.getName()).build();
  }

  /**
   * Request to delete a resource
   * @param model resource model
   * @return awsRequest the aws service request to delete a resource
   */
  static DeleteNetworkAnalyzerConfigurationRequest translateToDeleteRequest(final ResourceModel model) {
    if(model == null) {
      return null;
    }
    return DeleteNetworkAnalyzerConfigurationRequest.builder().configurationName(model.getName()).build();
  }

  /**
   * Request to update properties of a previously created resource
   * @param request
   * @return
   */
  static UpdateNetworkAnalyzerConfigurationRequest translateToUpdateRequest(ResourceHandlerRequest<ResourceModel> request) {
    final ResourceModel model = request.getDesiredResourceState();
    if(model==null) {
      return null;
    }

    final List<String> baseWirelessDevices = request.getPreviousResourceState().getWirelessDevices();
    final List<String> targetWirelessDevices = model.getWirelessDevices();

    final List<String> baseWirelessGateways = request.getPreviousResourceState().getWirelessGateways();
    final List<String> targetWirelessGateways= model.getWirelessGateways();

    return UpdateNetworkAnalyzerConfigurationRequest.builder()
            .configurationName(model.getName())
            .description(model.getDescription())
            .traceContent(translateFromTraceContentResourceModel(model.getTraceContent()))
            .wirelessDevicesToAdd(getDifferentElements(targetWirelessDevices,baseWirelessDevices))
            .wirelessDevicesToRemove(getDifferentElements(baseWirelessDevices,targetWirelessDevices))
            .wirelessGatewaysToAdd(getDifferentElements(targetWirelessGateways,baseWirelessGateways))
            .wirelessGatewaysToRemove(getDifferentElements(baseWirelessGateways,targetWirelessGateways))
            .build();
  }

  /**
   * @param base
   * @param target
   * @return elements that are in base but not in target
   */
   static List<String> getDifferentElements(List<String> base, List<String> target) {
    return base.stream().filter(e -> !target.contains(e)).collect(Collectors.toList());
  }

  /**
   * Returned at the end of Create, Read, Update handlers to make sure they all return the same thing
    * @param model
   * @return ResourceModel
   */
  static ResourceModel setModel(final ResourceModel model) {
    return ResourceModel.builder()
            .name(model.getName())
            .description(model.getDescription())
            .traceContent(model.getTraceContent())
            .wirelessDevices(model.getWirelessDevices())
            .wirelessGateways(model.getWirelessGateways())
            .tags(model.getTags())
            .arn(model.getArn())
            .build();
  }

  /**
   * Request list tags for resource
   * @param model resource model
   * @return awsRequest the aws service request to list tag for a resource
   */
  static ListTagsForResourceRequest translateToListTagForResourceRequest(final ResourceModel model) {
    return ListTagsForResourceRequest.builder().resourceArn(model.getArn()).build();
  }

  /**
   * Request to list resources
   * @param nextToken token passed to the aws service list resources request
   * @return awsRequest the aws service request to list resources within aws account
   */
  static ListNetworkAnalyzerConfigurationsRequest translateToListRequest(final String nextToken) {
    return ListNetworkAnalyzerConfigurationsRequest.builder().nextToken(nextToken).build();
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
}
