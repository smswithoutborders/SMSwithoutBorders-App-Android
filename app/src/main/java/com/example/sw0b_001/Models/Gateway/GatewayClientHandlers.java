package com.example.sw0b_001.Models.Gateway;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.room.Room;

import com.example.sw0b_001.Database.Datastore;
import com.example.sw0b_001.Helpers.GatewayValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GatewayClientHandlers {
    public static List<GatewayClient> parseGatewayFromJson(JSONArray gatewayClientsJson) throws JSONException {
        /*
        ----- GatewayClient Object

        [{
        "MSISDN":"",
        "default":"",
        "operatorName":""
        }]
         */

        List<GatewayClient> gatewayClients = new ArrayList<>();
        for(int i=0; i<gatewayClientsJson.length(); ++i ) {
            JSONObject gatewayClientJson = gatewayClientsJson.getJSONObject(i);
            GatewayClient gatewayClient = new GatewayClient()
                    .setMSISDN(gatewayClientJson.getString("MSISDN"))
                    .setOperatorName(gatewayClientJson.getString("operatorName"))
                    .setDefault(gatewayClientJson.getBoolean("default"));

            gatewayClients.add(gatewayClient);
        }
        return gatewayClients;
    }

    private static void addMultiple(Context context, List<GatewayClient> gatewayClients) throws InterruptedException {
        Thread storeProviders = new Thread(new Runnable() {
            @Override
            public void run() {
                Datastore dbConnector = Room.databaseBuilder(context,
                        Datastore.class, Datastore.DatabaseName)
                        .fallbackToDestructiveMigration()
                        .build();

                GatewayClientDao gatewayClientDao = dbConnector.gatewayClientDao();
                gatewayClientDao.deleteAll();

                for(int i=0;i<gatewayClients.size();++i)
                    gatewayClientDao.insert(gatewayClients.get(i));
            }
        });
        storeProviders.start();
        storeProviders.join();
    }

    private void storeGatewayInformation(Map<Integer, List<String>>[] extractedInformation, String passwdHash, String publicKey, String sharedKey) throws InterruptedException {
        Map<Integer, List<String>> providers = extractedInformation[0];
        Map<Integer, List<String>> provider_platforms_map = extractedInformation[1];

        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = app_preferences.edit();
        editor.putString(GatewayValues.VAR_PUBLICKEY, publicKey);
        editor.putString(GatewayValues.SHARED_KEY, sharedKey);
//                editor.putString(GatewayValues.VAR_PASSWDHASH, passwdHash);
        storePlatformFromGateway(providers, provider_platforms_map);
        editor.putString(GatewayValues.VAR_PASSWDHASH, passwdHash);
        editor.commit();
    }

}
