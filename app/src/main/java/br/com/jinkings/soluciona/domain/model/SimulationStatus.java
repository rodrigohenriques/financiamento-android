package br.com.jinkings.soluciona.domain.model;

import br.com.jinkings.financing.R;

/**
 * Created by rodrigohenriques on 7/4/15.
 */
public enum SimulationStatus {
    APPROVED("Aprovada", R.color.status_approved),
    CANCELED("Cancelada", R.color.status_canceled),
    IN_REVIEW("Em análise", R.color.status_in_review),
    WAITING_FOR_DOCUMENTATION("Aguardando documentação", R.color.status_wating_for_documentation),
    UNAVAILABLE("Consulte a central de atendimento", R.color.status_unavailable);

    protected String value;
    protected int colorId;

    SimulationStatus(String value, int colorId) {
        this.value = value;
        this.colorId = colorId;
    }

    public String getValue() {
        return value;
    }

    public int getColorId() {
        return colorId;
    }

    public static SimulationStatus find(String status) {
        for (SimulationStatus s : values()) {
            if (s.value.equalsIgnoreCase(status)) {
                return s;
            }
        }

        return UNAVAILABLE;
    }
}
