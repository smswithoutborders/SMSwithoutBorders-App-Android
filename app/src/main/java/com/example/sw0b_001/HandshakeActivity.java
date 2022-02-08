package com.example.sw0b_001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sw0b_001.Models.Gateway.GatewayClient;
import com.example.sw0b_001.Models.Platforms.Platform;
import com.example.sw0b_001.Security.AsymmetricSecurity;
import com.example.sw0b_001.Security.RSAProtocol;
import com.example.sw0b_001.Security.SecureProtocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class HandshakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_processing);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        String QRText = getIntent().getStringExtra("QRText");
        processQR(QRText);
    }

    public void processQR(String QRText) {
        try {
            // TODO provide default keystoreProvider and keystoreAlias values
            String keyStoreProvider = "";
            String keyStoreAlias = "";

            // TODO check if public key already exist
            // if exist use it
            // else create new one and store

            RSAProtocol asymmetricSecurityRSA = new RSAProtocol(keyStoreProvider, keyStoreAlias);
            byte[] publicKeyEncoded = asymmetricSecurityRSA.getPublicKeyEncoded();

            String jsonRequestString = "{\"public_key\": \"" + publicKeyEncoded + "\"}";
            JSONObject jsonBody = new JSONObject(jsonRequestString);

            // Second part Handshake => Transmission of public key
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(QRText, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Third part Handshake
                    // TODO request for password
                    Intent passwordPromptIntent = new Intent(getApplicationContext(), PasswordPromptActivity.class);
                    startActivity(passwordPromptIntent);

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
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    System.out.println("Failed: " + error);
                    Log.i(this.getClass().getSimpleName(), error.toString());
                }
            });
            queue.add(jsonObjectRequest);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }


    public void


    public Map<Integer, List<String>>[] extractPlatformFromGateway(JSONArray gatewayData) throws JSONException {
        Map<Integer, List<String>> providers = new HashMap<>();
        Map<Integer, List<String>> platforms = new HashMap<>();
        for(int i=0;i<gatewayData.length(); ++i) {
            JSONObject provider = (JSONObject) gatewayData.get(i);
//            Log.i(this.getClass().getSimpleName(), "Providers: " + provider.get("provider").toString());

            List<String> providerDetails = new ArrayList<>();
            providerDetails.add(provider.get("provider").toString());
            providerDetails.add(provider.get("description").toString());
            providers.put(i, providerDetails);

            JSONArray provider_platforms = (JSONArray) provider.get("platforms");
            for(int j=0;j<provider_platforms.length();++j) {
                JSONObject platform = (JSONObject) provider_platforms.get(j);
//                Log.i(this.getClass().getSimpleName(), "\tPlatforms: " + platform.get("name").toString());

                List<String> platformDetails = new ArrayList<>();
                platformDetails.add(platform.get("name").toString());
                platformDetails.add(platform.get("type").toString());
                platforms.put(i, platformDetails);
            }
        }

        Map<Integer, List<String>>[] extractedInformation= new Map[]{providers, platforms};
        return extractedInformation;
    }


    private void logout(Intent intent) {
        startActivity(intent);
        finish();
    }
}