package br.com.jinkings.soluciona.domain.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import br.com.jinkings.soluciona.application.ui.customview.Listable;

@ParseClassName("CondicaoImovel")
public class PropertyStatus extends ParseObject implements Listable {
    public String getDescription() {
        return getString(PropertyStatusFields.DESCRICAO);
    }

    @Override
    public String getLabel() {
        return getDescription();
    }
}
