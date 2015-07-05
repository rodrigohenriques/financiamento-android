package br.com.jinkings.soluciona.application.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.HomeActivity;

/**
 * Created by rodrigohenriques on 7/4/15.
 */
public class SimulationsFragment extends Fragment {

    HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_smiulations_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimulationRecyclerViewAdapter(homeActivity.getSimulations()));
    }
}
