package com.example.android.linkup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class IpAndPortSelectionActivity extends AppCompatActivity {

    public static final String SERVER_ADDR_EXTRA = "SERVER ADDRESS EXTRA";
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


    public void startApplication(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        String port = portInput.getText().toString();
        String ip = ipInput.getText().toString();
        String serverAddr = SERVER_PROTOCOL + ip + IP_AND_PORT_SEPARATOR + port;
        Log.i("SERVER ADDR" , serverAddr);
        intent.putExtra(SERVER_ADDR_EXTRA,serverAddr);
        startActivity(intent);
    }





}