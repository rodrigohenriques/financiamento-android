package br.com.jinkings.soluciona.domain.model;

/**
 * Created by rodrigohenriques on 7/4/15.
 */
public enum PropertyType {
    RESIDENTIAL("Residencial"), COMMERCIAL("Comercial");

    protected String value;

    PropertyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PropertyType find(String status) {
        for (PropertyType propertyType : values()) {
            if (propertyType.value.equalsIgnoreCase(status)) {
                return propertyType;
            }
        }

        return RESIDENTIAL;
    }
}
