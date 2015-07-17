package br.com.jinkings.soluciona.domain.model;

import java.util.ArrayList;
import java.util.List;

import br.com.jinkings.soluciona.application.ui.customview.Listable;

public class BasicListable implements Listable {

    private String value;
    private String label;

    public BasicListable(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public BasicListable(String value) {
        this.value = value;
        this.label = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public static List<BasicListable> with(Object[] objects) {
        List<BasicListable> items = new ArrayList<>();

        for (Object s : objects) {
            items.add(new BasicListable(s.toString()));
        }

        return items;
    }
}
