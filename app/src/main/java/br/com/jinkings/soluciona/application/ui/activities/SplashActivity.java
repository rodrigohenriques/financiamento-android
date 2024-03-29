package br.com.jinkings.soluciona.application.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.parse.ParseUser;

import br.com.jinkings.financing.R;

public class SplashActivity extends Activity {

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        if (ParseUser.getCurrentUser() != null) {

            startLoginActivity();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(750);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                startLoginActivity();
                            }
                        });
                    } catch (InterruptedException e) {
                        Log.e(getString(R.string.log_tag), e.getMessage(), e);
                    }
                }
            }).start();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
