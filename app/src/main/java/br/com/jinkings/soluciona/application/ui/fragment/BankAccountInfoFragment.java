package br.com.jinkings.soluciona.application.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.customview.ClickToSelectEditText;
import br.com.jinkings.soluciona.application.ui.customview.BasicListable;
import br.com.jinkings.soluciona.domain.model.Simulation;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class BankAccountInfoFragment extends NewSimulationFragment {

    @InjectView(R.id.bank_account_info_text_input_has_account)
    @NotEmpty(sequence = 0, messageResId = R.string.invalid_option)
    ClickToSelectEditText<BasicListable> clickToSelectEditTextHasAccount;

    @InjectView(R.id.bank_account_info_text_input_agency)
    EditText editTextAgency;

    @InjectView(R.id.bank_account_info_text_input_account)
    EditText editTextAccount;

    boolean hasAccount;

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

                hasAccount = selectedIndex == YES;

                editTextAgency.setEnabled(hasAccount);
                editTextAccount.setEnabled(hasAccount);

                clickToSelectEditTextHasAccount.setError(null);
                editTextAgency.setError(null);
                editTextAccount.setError(null);
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
    public void populateData(Simulation simulation) {

        if (hasAccount) {
            simulation.haveAnAccount();
            simulation.setAgency(getAgency());
            simulation.setAccountNumber(getAccount());
        } else {
            simulation.haveNoAccount();
        }
    }

    @Override
    public void validate(Validator.ValidationListener listener) {

        if (hasAccount) {

            boolean incompleteForm = false;

            if (getAgency().length() == 0) {
                editTextAgency.setError(getString(R.string.invalid_agency));
                incompleteForm = true;
            }

            if (getAccount().length() == 0) {
                editTextAccount.setError(getString(R.string.invalid_account));
                incompleteForm = true;
            }

            if (incompleteForm) {
                return;
            }
        }

        super.validate(listener);
    }
}
