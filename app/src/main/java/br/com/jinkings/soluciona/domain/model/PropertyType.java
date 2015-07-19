package br.com.jinkings.soluciona.domain.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import br.com.jinkings.soluciona.application.ui.customview.Listable;

@ParseClassName("TipoImovel")
public class PropertyType extends ParseObject implements Listable {
    public String getDescription() {
        try {
            fetchIfNeeded();
        } catch (ParseException e) {
            Log.e(PropertyType.class.getName(), e.getMessage(), e);
        }

        return getString(PropertyTypeFields.DESCRICAO);
    }

    @Override
    public String getLabel() {
        return getDescription();
    }
}
