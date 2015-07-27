package br.com.jinkings.soluciona.application.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.activities.NewSimulationActivity;
import br.com.jinkings.soluciona.application.ui.customview.ClickToSelectEditText;
import br.com.jinkings.soluciona.domain.model.PropertyStatus;
import br.com.jinkings.soluciona.domain.model.PropertyType;
import br.com.jinkings.soluciona.domain.model.Simulation;
import br.com.jinkings.soluciona.infrastructure.backend.cep.CepResult;
import br.com.jinkings.soluciona.infrastructure.backend.cep.CepService;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ui.mine.maskedit.CurrencyTextWatcher;
import ui.mine.maskedit.Mask;

public class PropertyInfoFragment extends NewSimulationFragment {

    @InjectView(R.id.property_info_text_input_cep)
    @NotEmpty(sequence = 1, messageResId = R.string.invalid_cep)
    EditText editTextCep;

    @InjectView(R.id.button_cep)
    Button buttonSearchCep;

    @InjectView(R.id.property_info_text_input_neighbourhood)
    @NotEmpty(sequence = 2, messageResId = R.string.invalid_neighbourhood)
    EditText editTextNeighbourhood;

    @InjectView(R.id.property_info_text_input_location)
    @NotEmpty(sequence = 3, messageResId = R.string.invalid_location)
    EditText editTextLocation;

    @InjectView(R.id.property_info_text_input_uf)
    @NotEmpty(sequence = 4, messageResId = R.string.invalid_uf)
    EditText editTextUF;

    @InjectView(R.id.property_info_text_input_county)
    @NotEmpty(sequence = 5, messageResId = R.string.invalid_county)
    EditText editTextCounty;

    @InjectView(R.id.property_info_text_input_property_type)
    @NotEmpty(sequence = 6, messageResId = R.string.invalid_property_type)
    ClickToSelectEditText<PropertyType> clickToSelectEditTextPropertyType;

    @InjectView(R.id.property_info_text_input_property_status)
    @NotEmpty(sequence = 7, messageResId = R.string.invalid_property_status)
    ClickToSelectEditText<PropertyStatus> clickToSelectEditTextPropertyStatus;

    @InjectView(R.id.property_info_text_input_price)
    @NotEmpty(sequence = 8, messageResId = R.string.invalid_price)
    EditText editTextPropertyPrice;

    NewSimulationActivity activity;

    PropertyType selectedPropertyType;
    PropertyStatus selectedPropertyStatus;

    List<PropertyType> propertyTypes;
    List<PropertyStatus> propertyStatuses;

    @Inject
    CepService cepService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View viewPropertyInfo = inflater.inflate(R.layout.fragment_property_info, container, false);

        ButterKnife.inject(this, viewPropertyInfo);

        editTextPropertyPrice.addTextChangedListener(new CurrencyTextWatcher(editTextPropertyPrice));
        editTextCep.addTextChangedListener(getEnableSearchCepButtonWatcher());

        return viewPropertyInfo;
    }


    @Override
    public void onStart() {
        super.onStart();

        new ParseDataLoader().execute();
    }

    @Override
    public ViewGroup getRootView() {
        return (ViewGroup) getView().getRootView().findViewById(R.id.new_simulation_frame);
    }

    @Override
    public void populateData(final Simulation simulation) {
        simulation.setCep(getCep());
        simulation.setNeighbourhood(getNeighbourhood());
        simulation.setLocation(getLocation());
        simulation.setUf(getUf());
        simulation.setCounty(getCounty());
        simulation.setPropertyType(selectedPropertyType);
        simulation.setPropertyStatus(selectedPropertyStatus);
        simulation.setPropertyPrice(getPropertyPrice());
    }

    @OnClick(R.id.button_cep)
    public void cepSearch() {
        startProgress();

        String cep = Mask.unmask(getCep());

        cepService.find(cep, new Callback<CepResult>() {
            @Override
            public void success(CepResult cepResult, Response response) {

                Log.i(getLogTag(), response.toString());

                bind(cepResult);

                finishProgress();
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e(getLogTag(), error.getMessage(), error);

                bind(new CepResult());

                justSnackIt(R.string.search_cep_default_failure);

                finishProgress();
            }

            private void bind(CepResult cepResult) {
                editTextNeighbourhood.setText(cepResult.neighbourhood);
                editTextLocation.setText(cepResult.location);
                editTextUF.setText(cepResult.uf);
                editTextCounty.setText(cepResult.county);
            }
        });
    }

    @NonNull
    private String getPropertyPrice() {
        return editTextPropertyPrice.getText().toString();
    }

    @NonNull
    private String getCounty() {
        return editTextCounty.getText().toString();
    }

    @NonNull
    private String getUf() {
        return editTextUF.getText().toString();
    }

    @NonNull
    private String getLocation() {
        return editTextLocation.getText().toString();
    }

    @NonNull
    private String getNeighbourhood() {
        return editTextNeighbourhood.getText().toString();
    }

    @NonNull
    private String getCep() {
        return editTextCep.getText().toString();
    }

    @NonNull
    private TextWatcher getEnableSearchCepButtonWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonSearchCep.setEnabled(s.length() >= 7);
            }
        };
    }

    private class ParseDataLoader extends AsyncTask<Void, Void, ParseDataLoader.Result> {

        class Result {
            boolean succeed;
            ParseException parseException;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            startProgress();
        }

        @Override
        protected Result doInBackground(Void... voids) {

            Result result = new Result();

            try {
                ParseQuery<PropertyType> propertyTypeParseQuery = ParseQuery.getQuery(PropertyType.class);
                ParseQuery<PropertyStatus> propertyStatusParseQuery = ParseQuery.getQuery(PropertyStatus.class);

                propertyTypes = propertyTypeParseQuery.find();
                propertyStatuses = propertyStatusParseQuery.find();

                result.succeed = true;
            } catch (ParseException e) {

                Log.e(getLogTag(), e.getMessage(), e);

                result.parseException = e;
                result.succeed = false;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);

            if (result.succeed) {
                clickToSelectEditTextPropertyType.setItems(propertyTypes);
                clickToSelectEditTextPropertyType.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<PropertyType>() {
                    @Override
                    public void onItemSelectedListener(PropertyType item, int selectedIndex) {
                        selectedPropertyType = item;
                    }
                });

                clickToSelectEditTextPropertyStatus.setItems(propertyStatuses);
                clickToSelectEditTextPropertyStatus.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<PropertyStatus>() {
                    @Override
                    public void onItemSelectedListener(PropertyStatus item, int selectedIndex) {
                        selectedPropertyStatus = item;
                    }
                });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage(R.string.new_simulation_load_data_failed);
                builder.setPositiveButton(R.string.dialog_close_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                });

                builder.create().show();
            }

            finishProgress();
        }
    }
}
