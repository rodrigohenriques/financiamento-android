package br.com.jinkings.soluciona.application.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import br.com.jinkings.financing.R;
import butterknife.ButterKnife;

public abstract class MainActivity extends AppCompatActivity {

    protected String logTag;
    protected ViewGroup rootView;
    protected View viewComponentProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentView());

        ButterKnife.inject(this);

        logTag = getString(R.string.log_tag);

        rootView = (ViewGroup) getWindow().getDecorView().getRootView();
        viewComponentProgressBar = getLayoutInflater().inflate(R.layout.component_progressbar, null);
    }

    protected abstract int getContentView();

    protected void logError(String message, Exception e) {
        Log.e(logTag, message, e);
    }

    protected void logError(Exception e) {
        Log.e(logTag, e.getMessage(), e);
    }

    protected void startProgress() {
        rootView.addView(viewComponentProgressBar);
    }

    protected void finishProgress() {
        rootView.removeView(viewComponentProgressBar);
    }

    protected void justSnackIt(int messageResId) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), messageResId, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.default_snackbar_action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }
}
