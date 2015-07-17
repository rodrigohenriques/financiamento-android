package br.com.jinkings.soluciona.application.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.activities.NewSimulationActivity;
import br.com.jinkings.soluciona.application.ui.customview.ClickToSelectEditText;
import br.com.jinkings.soluciona.domain.model.PropertyStatus;
import br.com.jinkings.soluciona.domain.model.PropertyType;
import br.com.jinkings.soluciona.domain.model.Simulation;
import butterknife.InjectView;

public class PropertyInfoFragment extends NewSimulationFragment {

    @InjectView(R.id.property_info_text_input_property_type)
    ClickToSelectEditText<PropertyType> clickToSelectEditTextPropertyType;

    @InjectView(R.id.property_info_text_input_property_status)
    ClickToSelectEditText<PropertyStatus> clickToSelectEditTextPropertyStatus;

    NewSimulationActivity activity;

    PropertyType selectedPropertyType;
    PropertyStatus selectedPropertyStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        activity = (NewSimulationActivity) getActivity();

        View viewPropertyInfo = inflater.inflate(R.layout.fragment_property_info, container, false);

        clickToSelectEditTextPropertyType.setItems(activity.getPropertyTypes());
        clickToSelectEditTextPropertyType.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<PropertyType>() {
            @Override
            public void onItemSelectedListener(PropertyType item, int selectedIndex) {
                selectedPropertyType = item;
            }
        });

        clickToSelectEditTextPropertyStatus.setItems(activity.getPropertyStatuses());
        clickToSelectEditTextPropertyStatus.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<PropertyStatus>() {
            @Override
            public void onItemSelectedListener(PropertyStatus item, int selectedIndex) {
                selectedPropertyStatus = item;
            }
        });

        return viewPropertyInfo;
    }

    @Override
    public ViewGroup getRootView() {
        return (ViewGroup) getView().getRootView().findViewById(R.id.new_simulation_frame);
    }

    @Override
    public void populate(Simulation simulation) {

    }
}
