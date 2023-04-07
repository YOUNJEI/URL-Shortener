package com.url.urlshortener.utility;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.InetAddress;

@Component
public class Geoip {
    private final DatabaseReader reader;

    public Geoip() throws Exception {
        final String filePath = "/Users/younjei/dev/GeoLite2-City.mmdb";

        File database = new File(filePath);
        reader = new DatabaseReader.Builder(database).build();
    }

    public String getLocation(String ipaddr) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipaddr);
            CityResponse cityResponse = reader.city(inetAddress);
            return cityResponse.getSubdivisions().get(0).getName();
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
