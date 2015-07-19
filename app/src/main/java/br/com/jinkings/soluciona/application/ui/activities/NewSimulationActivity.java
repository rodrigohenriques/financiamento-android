package br.com.jinkings.soluciona.application.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.fragment.BankAccountInfoFragment;
import br.com.jinkings.soluciona.application.ui.fragment.FinancingInfoFragment;
import br.com.jinkings.soluciona.application.ui.fragment.NewSimulationFragment;
import br.com.jinkings.soluciona.application.ui.fragment.PropertyInfoFragment;
import br.com.jinkings.soluciona.domain.model.Simulation;
import butterknife.InjectView;
import butterknife.OnClick;

public class NewSimulationActivity extends MainActivity {

    @InjectView(R.id.button_previous)
    Button buttonPrevious;
    @InjectView(R.id.button_next)
    Button buttonNext;

    private int fragmentIndex = 0;
    private ArrayList<NewSimulationFragment> fragments = new ArrayList<>();

    private final Simulation simulation = Simulation.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragments.add(new PropertyInfoFragment());
        fragments.add(new FinancingInfoFragment());
        fragments.add(new BankAccountInfoFragment());

        buttonPrevious.setVisibility(View.INVISIBLE);

        showFragment(currentFragment());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_new_simulation;
    }

    @OnClick(R.id.button_previous)
    public void previous() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.remove(currentFragment());
        fragmentTransaction.commit();

        fragmentIndex--;

        updateButtons();
    }

    @OnClick(R.id.button_next)
    public void next() {

        final NewSimulationFragment currentFragment = currentFragment();

        currentFragment.validate(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {

                currentFragment.populateData(simulation);

                if (isLastFragment()) {
                    sendNewSimulation();
                } else {
                    Fragment fragment = getNextFragment();
                    showFragment(fragment);
                }
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError v : errors) {
                    EditText editText = (EditText) v.getView();
                    editText.setError(v.getCollatedErrorMessage(NewSimulationActivity.this));
                }

                justSnackIt(R.string.invalid_form_message);
            }
        });
    }

    private NewSimulationFragment currentFragment() {
        return fragments.get(fragmentIndex);
    }

    private NewSimulationFragment getNextFragment() {
        return fragments.get(++fragmentIndex);
    }

    private void sendNewSimulation() {
        startProgress();

        simulation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                finishProgress();

                AlertDialog.Builder builder = new AlertDialog.Builder(NewSimulationActivity.this);

                if (e != null) {

                    Log.e(NewSimulationActivity.class.getName(), e.getMessage(), e);
                    builder.setTitle(e.getMessage());
                    builder.setMessage(R.string.new_simulation_store_data_failed);
                    builder.setNegativeButton(R.string.dialog_close_button, null);
                    builder.setPositiveButton(R.string.dialog_try_Again, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sendNewSimulation();
                        }
                    });
                } else {
                    builder.setTitle(R.string.dialog_title_success);
                    builder.setMessage(R.string.new_simulation_store_data_succeed);
                    builder.setPositiveButton(R.string.dialog_close_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                }

                builder.create().show();
            }
        });
    }


    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.new_simulation_frame, fragment);
        fragmentTransaction.commit();

        updateButtons();
    }

    private void updateButtons() {
        if (isFirstFragment()) {
            buttonPrevious.setVisibility(View.INVISIBLE);
        } else {
            buttonPrevious.setVisibility(View.VISIBLE);
        }

        if (isLastFragment()) {
            buttonNext.setText(R.string.action_send_new_simulation);
        } else {
            buttonNext.setText(R.string.action_next);
        }
    }

    private boolean isFirstFragment() {
        return fragmentIndex == 0;
    }

    private boolean isLastFragment() {
        return fragmentIndex == fragments.size() - 1;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_warning);
        builder.setMessage(R.string.dialog_new_simulation_on_back_pressed_message);
        builder.setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.dialog_negative_button, null);
        builder.create().show();
    }
}
