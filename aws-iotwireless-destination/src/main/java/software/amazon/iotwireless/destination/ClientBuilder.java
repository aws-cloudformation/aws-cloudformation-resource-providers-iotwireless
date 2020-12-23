package software.amazon.iotwireless.destination;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.cloudformation.LambdaWrapper;

import java.net.URI;

public class ClientBuilder {

    public static IotWirelessClient getClient() {
        return IotWirelessClient.builder()
                .httpClient(LambdaWrapper.HTTP_CLIENT)
                .build();
    }
}
