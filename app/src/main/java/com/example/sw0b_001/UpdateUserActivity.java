package com.example.sw0b_001;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sw0b_001.Models.Gateway.GatewayClient;
import com.example.sw0b_001.Models.Platforms.Platform;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class UpdateUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
    }

    private downloadUserInformation(String userID) {
        try {
            String gatewayServerPublicKey = response.getString("public_key");
            String generatedSharedKey = response.getString("generated_shared_key");
            String UID = response.getString("UID");
            JSONArray platformsJSONArray = response.getJSONArray("platforms");
            JSONArray gatewayClientsJSONArray = response.getJSONArray("gatewayClients");

            List<GatewayClient> gatewayClients = GatewayClient.parseGatewayFromJson(gatewayClientsJSONArray);
            List<Platform> platforms = GatewayClient.parseGatewayFromJson(platformsJSONArray);

        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}