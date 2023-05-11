package software.amazon.iotwireless.wirelessdeviceimporttask;

import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.cloudformation.LambdaWrapper;

public class ClientBuilder {
    public static IotWirelessClient getClient() {
        return IotWirelessClient.builder()
                .httpClient(LambdaWrapper.HTTP_CLIENT)
                .build();
    }
}