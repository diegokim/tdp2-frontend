package com.example.android.linkup.network;

public class NetworkConfiguration {
    public String serverAddr;
    private static NetworkConfiguration instance;
    public String accessToken;


    private NetworkConfiguration() {
    }

    public static NetworkConfiguration getInstance() {
        if ( instance == null ) {
            instance = new NetworkConfiguration();
        }
        return instance;
    }
}
