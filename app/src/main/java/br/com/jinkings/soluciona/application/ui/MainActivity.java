package br.com.jinkings.soluciona.application.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        setContentView(getContentView());

        super.onCreate(savedInstanceState, persistentState);

        ButterKnife.inject(this);
    }

    protected abstract int getContentView();
}
