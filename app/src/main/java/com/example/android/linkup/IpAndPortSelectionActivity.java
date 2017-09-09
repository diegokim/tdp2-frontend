package com.example.android.linkup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.android.linkup.network.NetworkConfiguration;

public class IpAndPortSelectionActivity extends Activity {

    private static final String SERVER_PROTOCOL = "http://" ;
    private String IP_AND_PORT_SEPARATOR = ":";
    private EditText ipInput;
    private EditText portInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_and_port_selection);
        ipInput = (EditText) findViewById(R.id.ip_input);
        portInput = (EditText) findViewById(R.id.port_input);
    }

    public void setServerAddress() {
        String port = portInput.getText().toString();
        String ip = ipInput.getText().toString();
        String serverAddr = SERVER_PROTOCOL + ip + IP_AND_PORT_SEPARATOR + port;
        Log.i("SERVER ADDR" , serverAddr);
        NetworkConfiguration.getInstance().serverAddr = serverAddr;
    }

    public void startApplication(View view) {
        setServerAddress();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}