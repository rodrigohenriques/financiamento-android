package br.com.jinkings.soluciona.application.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.customview.ClickToSelectEditText;
import br.com.jinkings.soluciona.application.ui.customview.BasicListable;
import br.com.jinkings.soluciona.domain.model.Simulation;
import butterknife.ButterKnife;
import butterknife.InjectView;
import ui.mine.maskedit.CurrencyTextWatcher;

public class FinancingInfoFragment extends NewSimulationFragment {

    @InjectView(R.id.financing_info_text_input_has_financing)
    @NotEmpty(sequence = 0, messageResId = R.string.invalid_option)
    ClickToSelectEditText<BasicListable> clickToSelectEditTextHasFinancing;

    @InjectView(R.id.financing_info_text_input_amount)
    @NotEmpty(sequence = 1, messageResId = R.string.invalid_financing_amount)
    EditText editTextAmount;

    @InjectView(R.id.financing_info_text_input_deadline)
    @NotEmpty(sequence = 2, messageResId = R.string.invalid_financing_deadline)
    EditText editTextDeadline;

    @InjectView(R.id.financing_info_text_input_has_property_near_by)
    @NotEmpty(sequence = 3, messageResId = R.string.invalid_option)
    ClickToSelectEditText<BasicListable> clickToSelectEditTextHasPropertyNearBy;

    @InjectView(R.id.financing_info_text_input_has_property_where_lives)
    @NotEmpty(sequence = 4, messageResId = R.string.invalid_option)
    ClickToSelectEditText<BasicListable> clickToSelectEditTextHasPropertyWhereLives;

    @InjectView(R.id.financing_info_text_input_birthday)
    @NotEmpty(sequence = 5, messageResId = R.string.invalid_birthday)
    EditText editTextBirthday;

    boolean hasFinancing;
    boolean hasPropertyNearBy;
    boolean hasPropertyWhereLives;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewFinancingInfo = inflater.inflate(R.layout.fragment_financing_info, container, false);

        ButterKnife.inject(this, viewFinancingInfo);

        String[] stringArrayYesNo = getResources().getStringArray(R.array.yes_no);

        clickToSelectEditTextHasFinancing.setItems(BasicListable.with(stringArrayYesNo));
        clickToSelectEditTextHasFinancing.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<BasicListable>() {
            final int YES = 0;

            @Override
            public void onItemSelectedListener(BasicListable item, int selectedIndex) {
                hasFinancing = selectedIndex == YES;
            }
        });
        clickToSelectEditTextHasPropertyNearBy.setItems(BasicListable.with(stringArrayYesNo));
        clickToSelectEditTextHasPropertyNearBy.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<BasicListable>() {
            final int YES = 0;

            @Override
            public void onItemSelectedListener(BasicListable item, int selectedIndex) {
                hasPropertyNearBy = selectedIndex == YES;
            }
        });
        clickToSelectEditTextHasPropertyWhereLives.setItems(BasicListable.with(stringArrayYesNo));
        clickToSelectEditTextHasPropertyWhereLives.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<BasicListable>() {
            final int YES = 0;

            @Override
            public void onItemSelectedListener(BasicListable item, int selectedIndex) {
                hasPropertyWhereLives = selectedIndex == YES;
            }
        });

        editTextAmount.addTextChangedListener(new CurrencyTextWatcher(editTextAmount));

        return viewFinancingInfo;
    }

    @Override
    public ViewGroup getRootView() {
        return (ViewGroup) getView().getRootView().findViewById(R.id.new_simulation_frame);
    }

    @Override
    public void populateData(Simulation simulation) {

        if (hasFinancing) {
            simulation.haveFinancing();
        } else {
            simulation.haveNoFinancing();
        }

        simulation.setFinancingAmount(getFinancingAmount());
        simulation.setDeadline(getDeadline());

        if (hasPropertyNearBy) {
            simulation.havePropertyNearBy();
        } else {
            simulation.haveNoPropertyNearBy();
        }

        if (hasPropertyWhereLives) {
            simulation.havePropertyWhereLives();
        } else {
            simulation.haveNoPropertyWhereLives();
        }

        simulation.setBirthday(getBirthday());
    }

    private String getBirthday() {
        return editTextBirthday.getText().toString();
    }

    private int getDeadline() {
        return Integer.valueOf(editTextDeadline.getText().toString());
    }

    private String getFinancingAmount() {
        return editTextAmount.getText().toString();
    }
}
