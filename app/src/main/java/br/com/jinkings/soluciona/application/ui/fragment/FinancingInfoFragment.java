package br.com.jinkings.soluciona.application.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.jinkings.financing.R;

public class FinancingInfoFragment extends MainFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewPropertyInfo = inflater.inflate(R.layout.fragment_financing_info, container, false);
        return viewPropertyInfo;
    }

    @Override
    public ViewGroup getRootView() {
        return (ViewGroup) getView().getRootView().findViewById(R.id.new_simulation_frame);
    }
}
