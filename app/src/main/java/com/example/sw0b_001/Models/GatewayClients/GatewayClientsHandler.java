package com.example.sw0b_001.Models.GatewayClients;

import android.content.Context;
import android.telephony.TelephonyManager;

import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sw0b_001.Database.Datastore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GatewayClientsHandler {

    public static long add(Context context, GatewayClient gatewayClient) throws InterruptedException {

        final long[] gatewayClientsInsertId = {-1};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Datastore databaseConnector = Room.databaseBuilder(context, Datastore.class,
                        Datastore.DatabaseName).build();
                GatewayClientsDao gatewayClientsDao = databaseConnector.gatewayClientsDao();
                gatewayClientsInsertId[0] = gatewayClientsDao.insert(gatewayClient);
            }
        });
        thread.start();
        thread.join();
        return gatewayClientsInsertId[0];
    }

    public static void toggleDefault(Context context, GatewayClient gatewayClient) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Datastore databaseConnector = Room.databaseBuilder(context, Datastore.class,
                        Datastore.DatabaseName).build();
                GatewayClientsDao gatewayClientsDao = databaseConnector.gatewayClientsDao();
                gatewayClientsDao.resetAllDefaults();
                gatewayClientsDao.updateDefault(gatewayClient.isDefault(), gatewayClient.getId());
            }
        });
        thread.start();
        thread.join();
    }

    public static String getOperatorId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String operatorId = telephonyManager.getSimOperator();

        return operatorId;
    }

    public static boolean containsDefaultProperties(Context context, String gatewayClientOperatorId) {
        String operatorId = getOperatorId(context);
        return operatorId.equals(gatewayClientOperatorId);
    }

    public static void remoteFetchAndStoreGatewayClients(Context context, String gatewayServerSeedsUrl, Runnable callbackFunction) throws InterruptedException {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest remoteSeedsRequest = new JsonArrayRequest(Request.Method.GET, gatewayServerSeedsUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray responses) {
                int defaultCounters = 0;
                boolean randomSelectDefault = false;

                for(int i=0;i<responses.length();++i) {
                    try {
                        JSONObject response = responses.getJSONObject(i);
                        String operatorId = response.getString("operator_id");
                        if(containsDefaultProperties(context, operatorId)) {
                            ++defaultCounters;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // If no Gateway client that matches the required ISP
                // choose from any of the multiples and make default
                if(defaultCounters < 1 && responses.length() > 0) {
                    defaultCounters = responses.length();
                    randomSelectDefault = true;
                }

                try {
                    defaultCounters = new Random().nextInt(defaultCounters);
                }
                catch(Exception e ) {
                    e.printStackTrace();
                }
                for(int i=0, findDefaultCounter=0;i<responses.length();++i, ++findDefaultCounter) {
                    try {
                        // TODO: Add algorithm for default Gateway Client
                        JSONObject response = responses.getJSONObject(i);
                        String IMSI = response.getString("IMSI");
                        String MSISDN = response.getString("MSISDN");
                        String country = response.getString("country");
                        String operatorName = response.getString("operator_name");
                        String operatorId = response.getString("operator_id");
                        double LPS = response.getDouble("LPS");
                        String seedType = response.getString("seed_type");

                        GatewayClient gatewayClient = new GatewayClient();
                        gatewayClient.setType(seedType);
                        gatewayClient.setMSISDN(MSISDN);
                        gatewayClient.setLastPingSession(LPS);
                        gatewayClient.setCountry(country);
                        gatewayClient.setOperatorName(operatorName);
                        gatewayClient.setOperatorId(operatorId);

                        // Random Gateway client selector
                        if(randomSelectDefault) {
                            if(i == defaultCounters)
                                gatewayClient.setDefault(true);
                        }
                        else if(containsDefaultProperties(context, operatorId)) {
                            if(findDefaultCounter == defaultCounters)
                                gatewayClient.setDefault(true);
                        }

                        GatewayClientsHandler.add(context, gatewayClient);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                if(callbackFunction != null)
                    callbackFunction.run();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callbackFunction.run();
            }
        });
        queue.add(remoteSeedsRequest);
    }

    public static List<GatewayClient> getAllGatewayClients(Context context) {
        final List<GatewayClient>[] gatewayClients = new List[]{new ArrayList<>()};

        Thread fetchGatewayClientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Datastore databaseConnection = Room.databaseBuilder(context,
                        Datastore.class, Datastore.DatabaseName)
                        .fallbackToDestructiveMigration()
                        .build();

                GatewayClientsDao gatewayClientsDao = databaseConnection.gatewayClientsDao();
                gatewayClients[0] = gatewayClientsDao.getAll();
            }
        });
        fetchGatewayClientThread.start();
        try {
            fetchGatewayClientThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return gatewayClients[0];
    }

    public static void clearStoredGatewayClients(Context context) {

        Thread clearGatewayClientsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Datastore databaseConnection = Room.databaseBuilder(context,
                        Datastore.class, Datastore.DatabaseName)
                        .fallbackToDestructiveMigration()
                        .build();

                GatewayClientsDao gatewayClientsDao = databaseConnection.gatewayClientsDao();
                gatewayClientsDao.deleteAll();
            }
        });
        clearGatewayClientsThread.start();
        try {
            clearGatewayClientsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static GatewayClient getGatewayClientMSISDN(Context context) throws Throwable {

        GatewayClient defaultGatewayClient = new GatewayClient();

        List<GatewayClient> gatewayClients = GatewayClientsHandler.getAllGatewayClients(context);
        for(GatewayClient gatewayClient : gatewayClients) {
            if(gatewayClient.isDefault()) {
                defaultGatewayClient = gatewayClient;
                break;
            }
        }

        return defaultGatewayClient;
    }

    public static String getDefaultGatewayClientMSISDN(Context context) throws Throwable {
        GatewayClient gatewayClient = getGatewayClientMSISDN(context);
        if(gatewayClient.getMSISDN() == null || gatewayClient.getMSISDN().isEmpty()) {
            // TODO should have fallback GatewayClients that can be used in the code
            String defaultSeedFallbackGatewayClientMSISDN = "+237672451860";
            gatewayClient.setMSISDN(defaultSeedFallbackGatewayClientMSISDN);
        }

        return gatewayClient.getMSISDN();
    }
}
