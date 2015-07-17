package br.com.jinkings.soluciona.application.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.customview.ClickToSelectEditText;
import br.com.jinkings.soluciona.domain.model.BasicListable;
import br.com.jinkings.soluciona.domain.model.Simulation;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class BankAccountInfoFragment extends NewSimulationFragment {

    @InjectView(R.id.bank_account_info_text_input_has_account)
    ClickToSelectEditText<BasicListable> clickToSelectEditTextHasAccount;

    @InjectView(R.id.bank_account_info_text_input_agency)
    EditText editTextAgency;

    @InjectView(R.id.bank_account_info_text_input_account)
    EditText editTextAccount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewBankAccountInfo = inflater.inflate(R.layout.fragment_bank_account_info, container, false);

        ButterKnife.inject(this, viewBankAccountInfo);

        String[] stringArrayYesNo = getResources().getStringArray(R.array.yes_no);

        clickToSelectEditTextHasAccount.setItems(BasicListable.with(stringArrayYesNo));
        clickToSelectEditTextHasAccount.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<BasicListable>() {
            public final int YES = 0;

            @Override
            public void onItemSelectedListener(BasicListable item, int selectedIndex) {
                editTextAgency.setEnabled(selectedIndex == YES);
                editTextAccount.setEnabled(selectedIndex == YES);
            }
        });

        return viewBankAccountInfo;
    }

    @Override
    public ViewGroup getRootView() {
        return (ViewGroup) getView().getRootView().findViewById(R.id.new_simulation_frame);
    }

    public String getAgency() {
        return editTextAgency.getText().toString();
    }

    public String getAccount() {
        return editTextAccount.getText().toString();
    }

    @Override
    public void populate(Simulation simulation) {

    }
}
