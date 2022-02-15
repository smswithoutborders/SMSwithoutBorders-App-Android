package com.example.sw0b_001.Test;

import androidx.room.Room;

import com.example.sw0b_001.Database.Datastore;
import com.example.sw0b_001.Models.Platforms.Platform;
import com.example.sw0b_001.Models.Platforms.PlatformDao;
import com.example.sw0b_001.Models.PublisherTemplates.Emails.EmailMessage;
import com.example.sw0b_001.Models.PublisherTemplates.Emails.EmailMessageDao;
import com.example.sw0b_001.Models.PublisherTemplates.Emails.EmailThreads;
import com.example.sw0b_001.Models.PublisherTemplates.Emails.EmailThreadsDao;
import com.example.sw0b_001.R;

public class TestPlatformsActivity {
    public void populateDB() throws InterruptedException {
        Platform gmail = new Platform()
                .setName("Gmail")
                .setProvider("google")
                .setDescription("Made By Google")
                .setImage(R.drawable.roundgmail)
                .setType("email");

        EmailThreads emailThreads = new EmailThreads()
//                        .setImage(CustomHelpers.getLetterImage('i'))
                .setRecipient("info@smswithoutborders.com")
                .setSubject("Initial test")
                .setMdate("2021-01-01");

        EmailMessage emailMessage = new EmailMessage()
                .setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. ")
                .setDatetime("2020-01-01")
//                        .setImage(CustomHelpers.getLetterImage('i'))
                .setStatus("delivered");

        Datastore platformDb = Room.databaseBuilder(getApplicationContext(),
                Datastore.class, Datastore.DBName).build();

        PlatformDao platformsDao = platformDb.platformDao();
        EmailThreadsDao emailThreadsDao = platformDb.emailThreadDao();
        EmailMessageDao emailMessageDao = platformDb.emailDao();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                platformsDao.deleteAll();
                emailThreadsDao.deleteAll();
                emailMessageDao.deleteAll();

                long platformId = platformsDao.insert(gmail);
                emailThreads.setPlatformId(platformId);
                long threadId = emailThreadsDao.insert(emailThreads);

                emailThreads.setSubject("Second Initial Message");
                emailThreads.setRecipient("sherlock@gmail.com");
                long threadId2 = emailThreadsDao.insert(emailThreads);

                emailMessage.setThreadId(threadId);
                emailMessageDao.insertAll(emailMessage);
                emailMessage.setThreadId(threadId2);
                emailMessageDao.insertAll(emailMessage);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
    }
}
