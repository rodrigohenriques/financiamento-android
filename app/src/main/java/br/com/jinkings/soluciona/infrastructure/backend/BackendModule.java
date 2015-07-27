package br.com.jinkings.soluciona.infrastructure.backend;

import android.app.Application;

import com.google.inject.AbstractModule;

import br.com.jinkings.soluciona.infrastructure.backend.cep.CepService;
import retrofit.RestAdapter;

/**
 * Created by rodrigohenriques on 7/26/15.
 */
public class BackendModule extends AbstractModule{

    Application application;

    public BackendModule(Application application) {
        this.application = application;
    }

    @Override
    protected void configure() {
        bind(CepService.class).toInstance(new RestAdapter.Builder()
                .setEndpoint("http://cep.correiocontrol.com.br/")
                .build()
                .create(CepService.class));
    }
}
