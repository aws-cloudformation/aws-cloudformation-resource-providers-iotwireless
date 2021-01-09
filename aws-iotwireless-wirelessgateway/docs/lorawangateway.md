# AWS::IoTWireless::WirelessGateway LoRaWANGateway

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "<a href="#gatewayeui" title="GatewayEui">GatewayEui</a>" : <i>String</i>,
    "<a href="#rfregion" title="RfRegion">RfRegion</a>" : <i>String</i>
}
</pre>

### YAML

<pre>
<a href="#gatewayeui" title="GatewayEui">GatewayEui</a>: <i>String</i>
<a href="#rfregion" title="RfRegion">RfRegion</a>: <i>String</i>
</pre>

## Properties

#### GatewayEui

_Required_: Yes

_Type_: String

_Pattern_: <code>^(([0-9a-f]{2}-){7}|([0-9a-f]{2}:){7}|([0-9a-f]{2}\s){7}|([0-9a-f]{2}){7})([0-9a-f]{2})$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### RfRegion

_Required_: Yes

_Type_: String

_Maximum_: <code>64</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

