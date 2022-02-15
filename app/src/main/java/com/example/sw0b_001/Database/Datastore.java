package com.example.sw0b_001.Database;


import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.sw0b_001.Models.PublisherTemplates.Emails.EmailMessage;
import com.example.sw0b_001.Models.PublisherTemplates.Emails.EmailThreads;
import com.example.sw0b_001.Models.PublisherTemplates.Emails.EmailMessageDao;
import com.example.sw0b_001.Models.PublisherTemplates.Emails.EmailThreadsDao;
import com.example.sw0b_001.Models.Gateway.GatewayClient;
import com.example.sw0b_001.Models.Gateway.GatewayDao;
import com.example.sw0b_001.Models.Platforms.PlatformDao;
import com.example.sw0b_001.Models.Platforms.Platform;
import com.example.sw0b_001.Models.PublisherTemplates.Text.TextMessage;
import com.example.sw0b_001.Models.PublisherTemplates.Text.TextMessageDao;

import org.jetbrains.annotations.NotNull;

// @Database(entities = {EmailMessage.class, EmailThreads.class, Platforms.class, GatewayPhonenumber.class, TextMessage.class}, autoMigrations = {@AutoMigration(from=3,to=4)}, version = 4)
@Database(entities = {EmailMessage.class, EmailThreads.class, Platform.class, GatewayClient.class, TextMessage.class}, version = 4)
public abstract class Datastore extends RoomDatabase {
    public static String DBName = "SWOBDb";

    public abstract EmailMessageDao emailDao();
    public abstract TextMessageDao textMessageDao();
    public abstract EmailThreadsDao emailThreadDao();
    public abstract PlatformDao platformDao();
    public abstract GatewayDao gatewayDao();

    @NonNull
    @NotNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @NotNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
