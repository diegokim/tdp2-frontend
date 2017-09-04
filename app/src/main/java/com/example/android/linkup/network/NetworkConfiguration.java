package com.example.android.linkup.network;

/**
 * Created by diegokim on 9/4/17.
 */

public class NetworkConfiguration {
    public String serverAddr;
    private static NetworkConfiguration instance;

    private NetworkConfiguration() {
    }

    public static NetworkConfiguration getInstance() {
        if ( instance == null ) {
            instance = new NetworkConfiguration();
        }
        return instance;
    }
}
