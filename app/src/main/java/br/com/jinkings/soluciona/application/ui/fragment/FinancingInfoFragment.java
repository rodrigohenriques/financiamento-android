package br.com.jinkings.soluciona.application.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.customview.ClickToSelectEditText;
import br.com.jinkings.soluciona.domain.model.BasicListable;
import br.com.jinkings.soluciona.domain.model.Simulation;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class FinancingInfoFragment extends NewSimulationFragment {

    @InjectView(R.id.financing_info_text_input_has_financing)
    ClickToSelectEditText<BasicListable> clickToSelectEditTextHasFinancing;

    @InjectView(R.id.financing_info_text_input_has_property_near_by)
    ClickToSelectEditText<BasicListable> clickToSelectEditTextHasPropertyNearBy;

    @InjectView(R.id.financing_info_text_input_has_property_where_lives)
    ClickToSelectEditText<BasicListable> clickToSelectEditTextHasPropertyWhereLives;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewFinancingInfo = inflater.inflate(R.layout.fragment_financing_info, container, false);

        ButterKnife.inject(this, viewFinancingInfo);

        String[] stringArrayYesNo = getResources().getStringArray(R.array.yes_no);

        clickToSelectEditTextHasFinancing.setItems(BasicListable.with(stringArrayYesNo));
        clickToSelectEditTextHasPropertyNearBy.setItems(BasicListable.with(stringArrayYesNo));
        clickToSelectEditTextHasPropertyWhereLives.setItems(BasicListable.with(stringArrayYesNo));

        return viewFinancingInfo;
    }

    @Override
    public ViewGroup getRootView() {
        return (ViewGroup) getView().getRootView().findViewById(R.id.new_simulation_frame);
    }

    @Override
    public void populate(Simulation simulation) {

    }
}
