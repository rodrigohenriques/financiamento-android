package br.com.jinkings.soluciona.application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import br.com.jinkings.soluciona.domain.model.Simulation;

/**
 * Created by rodrigohenriques on 6/27/15.
 */
public class JinkingsSoluciona extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Simulation.class);
        Parse.initialize(this, "tcPa2fsP1bULHFjD6ZG0qHxmAILBYku4TosH8uX2", "jXAtMw4hIthlWgjlL9LasZQ03UJv4igOw2bHPH3R");
    }
}
