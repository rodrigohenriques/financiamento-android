package br.com.jinkings.soluciona.infrastructure.backend.cep;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface CepService {
    @GET("/{cep}.json")
    void find(@Path("cep") String cep, Callback<CepResult> callback);
}
