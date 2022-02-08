package com.example.sw0b_001.Models.Platforms;

import android.content.Context;
import android.content.Intent;

import androidx.room.Room;

import com.example.sw0b_001.Database.Datastore;
import com.example.sw0b_001.EmailThreadsActivity;
import com.example.sw0b_001.PublisherTemplates.Emails.EmailMessageDao;
import com.example.sw0b_001.R;
import com.example.sw0b_001.TextThreadActivity;

import java.util.List;
import java.util.Map;

public class PlatformsHandler {
    static public Intent getIntent(Context context, String platform, String type) {
        Intent intent = new Intent();
        switch(type) {
            case "email": {
                intent = new Intent(context, EmailThreadsActivity.class);
                intent.putExtra("platform", platform);
                break;
            }

            case "text": {
                intent = new Intent(context, TextThreadActivity.class);
                intent.putExtra("platform", platform);
                break;
            }
            // TODO: put a default here
        }
        return intent;
    }

    public void addMultiple() throws InterruptedException {
        Thread storeProviders = new Thread(new Runnable() {
            @Override
            public void run() {
                Datastore dbConnector = Room.databaseBuilder(getApplicationContext(),
                        Datastore.class, Datastore.DBName).build();
                PlatformDao providerDao = dbConnector.platformDao();
                EmailMessageDao emailMessageDao = dbConnector.emailDao();
                providerDao.deleteAll();
                emailMessageDao.deleteAll();
                for(int i=0;i<providers.size();++i) {
                    Platform provider = new Platform()
                            .setName(platforms.get(i).get(0))
                            .setDescription(providers.get(i).get(1))
                            .setProvider(providers.get(i).get(0))
                            .setType(platforms.get(i).get(1));
                    if(provider.getName().toLowerCase().equals("gmail") && provider.getProvider().toLowerCase().equals("google"))
                        provider.setImage(R.drawable.roundgmail);
                    else if(provider.getName().toLowerCase().equals("twitter") && provider.getProvider().toLowerCase().equals("twitter"))
                        provider.setImage(R.drawable.roundtwitter);
                    providerDao.insert(provider);
                }
            }
        });
        storeProviders.start();
        storeProviders.join();
    }
}
