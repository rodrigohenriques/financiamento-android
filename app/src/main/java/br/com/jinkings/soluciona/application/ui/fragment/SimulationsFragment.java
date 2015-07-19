package br.com.jinkings.soluciona.application.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.activities.NewSimulationActivity;
import br.com.jinkings.soluciona.application.ui.adapter.SimulationRecyclerViewAdapter;
import br.com.jinkings.soluciona.application.ui.recyclerview.DividerItemDecoration;
import br.com.jinkings.soluciona.domain.model.Simulation;
import br.com.jinkings.soluciona.domain.model.SimulationFields;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rodrigohenriques on 7/4/15.
 */
public class SimulationsFragment extends MainFragment implements SimulationRecyclerViewAdapter.OnItemClickListener {

    @InjectView(R.id.simulation_textview_empty_list)
    View emptyList;
    @InjectView(R.id.simulation_recyclerview)
    RecyclerView recyclerView;

    List<Simulation> simulations;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_simulations, container, false);

        ButterKnife.inject(this, rootView);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        return rootView;
    }

    @Override
    public ViewGroup getRootView() {
        return (ViewGroup) getView().getRootView().findViewById(R.id.home_frame);
    }

    @Override
    public void onStart() {
        super.onStart();

        startProgress();

        ParseQuery<Simulation> query = Simulation.getQuery();

        query.whereEqualTo(SimulationFields.USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Simulation>() {
            @Override
            public void done(List<Simulation> list, ParseException e) {

                if (e != null) {
                    int errorMsgId = R.string.default_loading_simulations_error_message;

                    justSnackIt(errorMsgId);

                    Log.e(SimulationsFragment.class.getName(), e.getMessage(), e);
                } else {
                    simulations = list;

                    if (list.isEmpty()) {
                        emptyList.setVisibility(View.VISIBLE);
                    } else {
                        emptyList.setVisibility(View.GONE);
                        SimulationRecyclerViewAdapter adapter = new SimulationRecyclerViewAdapter(getActivity(), simulations);
                        adapter.setOnItemClickListener(SimulationsFragment.this);
                        recyclerView.setAdapter(adapter);
                    }
                }

                finishProgress();
            }
        });
    }

    @Override
    public void onItemClick(Simulation simulation) {
        justSnackIt(simulation.getPriceAndDate());
    }

    @OnClick(R.id.simulation_button_new)
    public void newSimulation() {
        Intent intent = new Intent(getActivity(), NewSimulationActivity.class);
        startActivity(intent);
    }
}
