package com.example.android.linkup.network;

public class NetworkConfiguration {
    public static final String SERVER_REQUEST_ERROR = "No hemos podido comunicarnos con el servidor, por favor intenta nuevamente mas tarde" ;
    public String serverAddr;
    public String accessToken;
    private static NetworkConfiguration instance;


    private NetworkConfiguration() {
    }

    public static NetworkConfiguration getInstance() {
        if ( instance == null ) {
            instance = new NetworkConfiguration();
            instance.accessToken = "-1";
        }
        return instance;
    }
}
