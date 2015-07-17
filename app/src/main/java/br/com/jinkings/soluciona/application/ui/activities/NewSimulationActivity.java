package br.com.jinkings.soluciona.application.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.fragment.BankAccountInfoFragment;
import br.com.jinkings.soluciona.application.ui.fragment.FinancingInfoFragment;
import br.com.jinkings.soluciona.application.ui.fragment.NewSimulationFragment;
import br.com.jinkings.soluciona.application.ui.fragment.PropertyInfoFragment;
import br.com.jinkings.soluciona.domain.model.PropertyStatus;
import br.com.jinkings.soluciona.domain.model.PropertyType;
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

    private List<PropertyType> propertyTypes;
    private List<PropertyStatus> propertyStatuses;

    private Handler handler = new Handler();

    private Simulation simulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simulation = new Simulation();

        fragments.add(new PropertyInfoFragment());
        fragments.add(new FinancingInfoFragment());
        fragments.add(new BankAccountInfoFragment());

        Fragment fragment = fragments.get(fragmentIndex);

        showFragment(fragment);

        buttonPrevious.setVisibility(View.INVISIBLE);

        loadData();
    }

    private void loadData() {

        startProgress();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ParseQuery<PropertyType> propertyTypeParseQuery = ParseQuery.getQuery(PropertyType.class);
                    ParseQuery<PropertyStatus> propertyStatusParseQuery = ParseQuery.getQuery(PropertyStatus.class);

                    propertyTypeParseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                    propertyStatusParseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);

                    propertyTypes = propertyTypeParseQuery.find();
                    propertyStatuses = propertyStatusParseQuery.find();
                } catch (ParseException e) {
                    Log.e(NewSimulationActivity.class.getName(), e.getMessage(), e);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewSimulationActivity.this);

                            builder.setMessage(R.string.new_simulation_load_data_failed);
                            builder.setPositiveButton(R.string.dialog_close_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });

                            builder.create().show();
                        }
                    });
                } finally {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            finishProgress();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_new_simulation;
    }

    @OnClick(R.id.button_previous)
    public void previous() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.remove(fragments.get(fragmentIndex--));
        fragmentTransaction.commit();
        updateButtons();
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



        for (NewSimulationFragment fragment : fragments) {
            fragment.populate(simulation);
        }

        if (simulation.validate()) {
            simulation.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(NewSimulationActivity.this);

                    if (e != null) {

                        builder.setMessage(R.string.new_simulation_store_data_failed);
                        builder.setNegativeButton(R.string.dialog_close_button, null);
                        builder.setPositiveButton(R.string.dialog_try_Again, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sendNewSimulation();
                            }
                        });
                    } else {

                    }

                    builder.create().show();
                 }
            });
        } else {
            justSnackIt(R.string.invalid_form_message);
        }
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

    public List<PropertyType> getPropertyTypes() {
        return propertyTypes;
    }

    public List<PropertyStatus> getPropertyStatuses() {
        return propertyStatuses;
    }
}
