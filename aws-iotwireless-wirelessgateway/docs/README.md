# AWS::IoTWireless::WirelessGateway

Create and manage wireless gateways, including LoRa gateways.

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "Type" : "AWS::IoTWireless::WirelessGateway",
    "Properties" : {
        "<a href="#name" title="Name">Name</a>" : <i>String</i>,
        "<a href="#description" title="Description">Description</a>" : <i>String</i>,
        "<a href="#tags" title="Tags">Tags</a>" : <i>[ <a href="tag.md">Tag</a>, ... ]</i>,
        "<a href="#lorawan" title="LoRaWAN">LoRaWAN</a>" : <i><a href="lorawangateway.md">LoRaWANGateway</a></i>,
        "<a href="#thingname" title="ThingName">ThingName</a>" : <i>String</i>
    }
}
</pre>

### YAML

<pre>
Type: AWS::IoTWireless::WirelessGateway
Properties:
    <a href="#name" title="Name">Name</a>: <i>String</i>
    <a href="#description" title="Description">Description</a>: <i>String</i>
    <a href="#tags" title="Tags">Tags</a>: <i>
      - <a href="tag.md">Tag</a></i>
    <a href="#lorawan" title="LoRaWAN">LoRaWAN</a>: <i><a href="lorawangateway.md">LoRaWANGateway</a></i>
    <a href="#thingname" title="ThingName">ThingName</a>: <i>String</i>
</pre>

## Properties

#### Name

Name of Wireless Gateway.

_Required_: No

_Type_: String

_Maximum_: <code>256</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Description

Description of Wireless Gateway.

_Required_: No

_Type_: String

_Maximum_: <code>2048</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Tags

A list of key-value pairs that contain metadata for the gateway.

_Required_: No

_Type_: List of <a href="tag.md">Tag</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### LoRaWAN

_Required_: Yes

_Type_: <a href="lorawangateway.md">LoRaWANGateway</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### ThingName

Thing Arn. If there is a Thing created, this can be returned with a Get call.

_Required_: No

_Type_: String

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

## Return Values

### Ref

When you pass the logical ID of this resource to the intrinsic `Ref` function, Ref returns the Id.

### Fn::GetAtt

The `Fn::GetAtt` intrinsic function returns a value for a specified attribute of this type. The following are the available attributes and sample return values.

For more information about using the `Fn::GetAtt` intrinsic function, see [Fn::GetAtt](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference-getatt.html).

#### ThingArn

Thing Arn. Passed into Update to associate a Thing with the Wireless Gateway.

#### Id

Id for Wireless Gateway. Returned upon successful create.

#### Arn

Arn for Wireless Gateway. Returned upon successful create.

