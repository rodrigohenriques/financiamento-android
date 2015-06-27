package br.com.jinkings.soluciona.application;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by rodrigohenriques on 6/27/15.
 */
public class JinkingsSoluciona extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "tcPa2fsP1bULHFjD6ZG0qHxmAILBYku4TosH8uX2", "jXAtMw4hIthlWgjlL9LasZQ03UJv4igOw2bHPH3R");
    }
}
