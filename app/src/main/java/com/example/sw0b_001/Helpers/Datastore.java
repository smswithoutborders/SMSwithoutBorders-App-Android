package com.example.sw0b_001.Helpers;


import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.sw0b_001.Providers.Emails.EmailMessage;
import com.example.sw0b_001.Providers.Emails.EmailThreads;
import com.example.sw0b_001.Providers.Emails.EmailMessageDao;
import com.example.sw0b_001.Providers.Emails.EmailThreadsDao;
import com.example.sw0b_001.Providers.Gateway.GatewayPhonenumber;
import com.example.sw0b_001.Providers.Gateway.GatewayDao;
import com.example.sw0b_001.Providers.Platforms.PlatformDao;
import com.example.sw0b_001.Providers.Platforms.Platforms;
import com.example.sw0b_001.Providers.Text.TextMessage;
import com.example.sw0b_001.Providers.Text.TextMessageDao;

import org.jetbrains.annotations.NotNull;

// @Database(entities = {EmailMessage.class, EmailThreads.class, Platforms.class, GatewayPhonenumber.class, TextMessage.class}, autoMigrations = {@AutoMigration(from=3,to=4)}, version = 4)
@Database(entities = {EmailMessage.class, EmailThreads.class, Platforms.class, GatewayPhonenumber.class, TextMessage.class}, version = 4)
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
