package com.example.sw0b_001;

import android.annotation.SuppressLint;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.sw0b_001.databinding.ActivitySplashBinding;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private final Handler hideHandler = new Handler();

    private View fullscreenContentView;
    private ActivitySplashBinding splashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(splashBinding.getRoot());

        // TODO check if keypairs are present
        // if not present show splash and move to QR
        // if present continue to platforms
        fullscreenContentView = splashBinding.fullscreenContent;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        /**
         * Schedules a call to hide() in delay milliseconds, canceling any
         * previously scheduled calls.
         */
        int delayHideMillisecs = 0;
        hideHandler.removeCallbacks(hideCallbackRunnable);
        hideHandler.postDelayed(hideCallbackRunnable, delayHideMillisecs);
    }

    private void navigateToPermissionsActivity() {
        Intent intent = new Intent(this, PermissionsActivity.class);
        startActivity(intent);
        return;
    }

    private void navigateToDashboardActivity() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    private final Runnable hideScreenContentRunnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.

            fullscreenContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable showScreenContentRunnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };

    private final Runnable hideCallbackRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                hideScreenContents();
            } catch (InterruptedException | CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void hideScreenContents() throws InterruptedException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        final int UI_ANIMATION_DELAY = 0;
        hideHandler.removeCallbacks(showScreenContentRunnable);
        hideHandler.postDelayed(hideScreenContentRunnable, UI_ANIMATION_DELAY);

        int delayHideMillisecs = 3000;
        fullscreenContentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToPermissionsActivity();
                finish();
            }
        }, delayHideMillisecs);
    }
}