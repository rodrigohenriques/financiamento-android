package br.com.jinkings.soluciona.application.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.fragment.BankAccountInfoFragment;
import br.com.jinkings.soluciona.application.ui.fragment.FinancingInfoFragment;
import br.com.jinkings.soluciona.application.ui.fragment.PropertyInfoFragment;
import butterknife.InjectView;
import butterknife.OnClick;

public class NewSimulationActivity extends MainActivity {

    @InjectView(R.id.button_previous)
    Button buttonPrevious;
    @InjectView(R.id.button_next)
    Button buttonNext;

    private int fragmentIndex = 0;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragments.add(new PropertyInfoFragment());
        fragments.add(new FinancingInfoFragment());
        fragments.add(new BankAccountInfoFragment());

        Fragment fragment = fragments.get(fragmentIndex);

        showFragment(fragment);

        buttonPrevious.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_new_simulation;
    }

    @OnClick(R.id.button_previous)
    public void previous() {
        fragmentIndex--;
        Fragment fragment = fragments.get(fragmentIndex);
        showFragment(fragment);
    }

    @OnClick(R.id.button_next)
    public void next() {
        if (isLastFragment()) {
            sendNewSimulation();
        } else {
            fragmentIndex++;
            Fragment fragment = fragments.get(fragmentIndex);
            showFragment(fragment);
        }
    }

    private void sendNewSimulation() {
        justSnackIt(R.string.invalid_form_message);
    }


    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, 0, 0, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.new_simulation_frame, fragment);
        fragmentTransaction.commit();

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
