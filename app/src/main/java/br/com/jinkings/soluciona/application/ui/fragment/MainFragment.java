package br.com.jinkings.soluciona.application.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.jinkings.financing.R;

/**
 * Created by rodrigohenriques on 7/4/15.
 */
public abstract class MainFragment extends Fragment {

    private View viewComponentProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewComponentProgressBar = inflater.inflate(R.layout.component_progressbar, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected ViewGroup getRootView() {
        return ((ViewGroup) getView());
    }

    public void startProgress() {
        getRootView().addView(viewComponentProgressBar);
    }

    public void finishProgress() {
        getRootView().removeView(viewComponentProgressBar);
    }

    public void justSnackIt(int messageResId) {
        justSnackIt(getActivity().getString(messageResId));
    }

    public void justSnackIt(String message) {
        final Snackbar snackbar = Snackbar.make(getRootView(), message, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.default_snackbar_action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }
}
