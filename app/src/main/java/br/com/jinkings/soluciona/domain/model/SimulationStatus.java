package br.com.jinkings.soluciona.domain.model;

import android.graphics.Color;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("StatusSimulacao")
public class SimulationStatus extends ParseObject {
    public String getDescription() {
        return getString(SimulationStatusFields.DESCRICAO);
    }

    public String getColorHex() {
        return getString(SimulationStatusFields.HEX_COR);
    }

    public int getColor() {
        String colorHex = getColorHex();

        if (colorHex != null) {
            return Color.parseColor(getColorHex());
        } else {
            return Color.parseColor("#607D8B");
        }
    }
}
