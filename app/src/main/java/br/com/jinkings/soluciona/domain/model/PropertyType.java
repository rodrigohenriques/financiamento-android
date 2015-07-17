package br.com.jinkings.soluciona.domain.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import br.com.jinkings.soluciona.application.ui.customview.Listable;

@ParseClassName("TipoImovel")
public class PropertyType extends ParseObject implements Listable {
    public String getDescription() {
        return getString(PropertyTypeFields.DESCRICAO);
    }

    @Override
    public String getLabel() {
        return getDescription();
    }
}