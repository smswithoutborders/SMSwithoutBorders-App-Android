package com.example.sw0b_001;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import com.example.sw0b_001.Database.Datastore;
import com.example.sw0b_001.Helpers.GatewayValues;
import com.example.sw0b_001.Models.Platforms.Platform;
import com.example.sw0b_001.Security.SecureProtocol;
import com.example.sw0b_001.PublisherTemplates.Emails.EmailMessage;
import com.example.sw0b_001.PublisherTemplates.Emails.EmailThreads;
import com.example.sw0b_001.PublisherTemplates.Emails.EmailMessageDao;
import com.example.sw0b_001.PublisherTemplates.Emails.EmailThreadsDao;
import com.example.sw0b_001.Models.Platforms.PlatformDao;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class PasswordPromptActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}