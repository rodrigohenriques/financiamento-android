package br.com.jinkings.soluciona.application.ui.fragment;

import com.mobsandgeeks.saripaar.Validator;

import br.com.jinkings.soluciona.domain.model.Simulation;
import br.com.m4u.commons.brazilian.library.validator.BrazilianValidator;

public abstract class NewSimulationFragment extends MainFragment{

    public void validate(final Validator.ValidationListener listener) {
        BrazilianValidator brazilianValidator = new BrazilianValidator(this);
        brazilianValidator.setValidationListener(listener);
        brazilianValidator.validate();
    }

    public abstract void populateData(final Simulation simulation);
}
