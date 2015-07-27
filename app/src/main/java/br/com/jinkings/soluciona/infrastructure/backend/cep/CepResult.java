package br.com.jinkings.soluciona.infrastructure.backend.cep;

import com.google.gson.annotations.SerializedName;

public class CepResult {
    @SerializedName("bairro")
    public String neighbourhood;
    @SerializedName("logradouro")
    public String location;
    @SerializedName("cep")
    public String cep;
    @SerializedName("uf")
    public String uf;
    @SerializedName("localidade")
    public String county;
}
