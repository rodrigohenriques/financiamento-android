package br.com.jinkings.soluciona.domain.model;

import android.graphics.Color;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

@ParseClassName("StatusSimulacao")
public class SimulationStatus extends ParseObject {

    public static final String DEFAULT_INITIAL_ID = "KMok5LBiIC";

    public String getDescription() {

        try {
            fetchIfNeeded();
        } catch (ParseException e) {
            Log.e(SimulationStatus.class.getName(), e.getMessage(), e);
        }

        return getString(SimulationStatusFields.DESCRICAO);
    }

    public String getColorHex() {

        try {
            fetchIfNeeded();
        } catch (ParseException e) {
            Log.e(SimulationStatus.class.getName(), e.getMessage(), e);
        }

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
