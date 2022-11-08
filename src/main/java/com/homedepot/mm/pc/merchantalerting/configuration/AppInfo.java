package com.homedepot.mm.pc.merchantalerting.configuration;

import lombok.Value;

@Value(staticConstructor = "of")
public class AppInfo {
    String name;
    String version;

    public static AppInfo of() {
        String version = System.getenv("VERSION");
        if (version == null) {
            version = "local";
        }

        return new AppInfo("Merchant Alerting Service", version);
    }
}
