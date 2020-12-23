package software.amazon.iotwireless.deviceprofile;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iotwireless.IotWirelessClient;
import software.amazon.cloudformation.LambdaWrapper;
import com.amazonaws.regions.Regions;

import java.net.URI;

public class ClientBuilder {

  public static IotWirelessClient getClient() {
    return IotWirelessClient.builder()
            .httpClient(LambdaWrapper.HTTP_CLIENT)
            .build();
  }
}
