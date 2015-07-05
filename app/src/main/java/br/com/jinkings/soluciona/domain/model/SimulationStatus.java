package br.com.jinkings.soluciona.domain.model;

/**
 * Created by rodrigohenriques on 7/4/15.
 */
public enum SimulationStatus {
    APPROVED("Aprovada"),
    CANCELED("Cancelada"),
    IN_REVIEW("Em análise"),
    WAITING_FOR_DOCUMENTATION("Aguardando documentação"),
    UNAVAILABLE("Consulte a central de atendimento");

    protected String value;

    SimulationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
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
