package br.com.jinkings.soluciona.domain.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by rodrigohenriques on7/4/15.
 */
@ParseClassName("Simulacao")
public class Simulation extends ParseObject {

    public Simulation() {

    }

    public String getAgency() {
        return getString(SimulationFields.AGENCIA);
    }

    public void setAgency(String agency) {
        put(SimulationFields.AGENCIA, agency);
    }

    public String getNeighbourhood() {
        return getString(SimulationFields.BAIRRO);
    }

    public void setNeighbourhood(String neighbourhood) {
        put(SimulationFields.BAIRRO, neighbourhood);
    }

    public String getCep() {
        return getString(SimulationFields.CEP);
    }

    public void setCep(String cep) {
        put(SimulationFields.CEP, cep);
    }

    public String getAccountNumber() {
        return getString(SimulationFields.CONTA);
    }

    public void setAccountNumber(String accountNumber) {
        put(SimulationFields.CONTA, accountNumber);
    }

    public boolean hasAccount() {
        return getBoolean(SimulationFields.CORRENTISTA);
    }

    public void haveAnAccount() {
        put(SimulationFields.CORRENTISTA, true);
    }

    public void haveNoAccount() {
        put(SimulationFields.CORRENTISTA, false);
    }

    public String getDataEnvio() {
        return getString(SimulationFields.DATA_ENVIO);
    }

    public void setDataEnvio(String dataEnvio) {
        put(SimulationFields.DATA_ENVIO, dataEnvio);
    }

    public String getBirthday() {
        return getString(SimulationFields.DATA_NASCIMENTO);
    }

    public void setBirthday(String birthday) {
        put(SimulationFields.DATA_NASCIMENTO, birthday);
    }

    public boolean hasPropertyInFinancingLocal() {
        return getBoolean(SimulationFields.IMOVEL_MUNICIPIO_FINANCIAMENTO);
    }

    public void havePropertyInFinancingLocal() {
        put(SimulationFields.IMOVEL_MUNICIPIO_FINANCIAMENTO, true);
    }

    public void hasNoPropertyInFinancingLocal() {
        put(SimulationFields.IMOVEL_MUNICIPIO_FINANCIAMENTO, false);
    }

    public boolean hasPropertyWhereLives() {
        return getBoolean(SimulationFields.IMOVEL_MUNICIPIO_RESIDE);
    }

    public void havePropertyWhereLives() {
        put(SimulationFields.IMOVEL_MUNICIPIO_RESIDE, true);
    }

    public void haveNoPropertyWhereLives() {
        put(SimulationFields.IMOVEL_MUNICIPIO_RESIDE, false);
    }

    public String getLocation() {
        return getString(SimulationFields.LOGRADOURO);
    }

    public void setLocation(String location) {
        put(SimulationFields.LOGRADOURO, location);
    }

    public String getCounty() {
        return getString(SimulationFields.MUNICIPIO);
    }

    public void setCounty(String county) {
        put(SimulationFields.MUNICIPIO, county);
    }

    public boolean hasFinancing() {
        return getBoolean(SimulationFields.POSSUI_FINANCIAMENTO);
    }

    public void haveFinancing(boolean haveFinancing) {
        put(SimulationFields.POSSUI_FINANCIAMENTO, true);
    }

    public void haveNoFinancing(boolean haveNoFinancing) {
        put(SimulationFields.POSSUI_FINANCIAMENTO, false);
    }

    public int getDeadline() {
        return getInt(SimulationFields.PRAZO_DESEJADO);
    }

    public void setDeadline(int deadline) {
        put(SimulationFields.PRAZO_DESEJADO, deadline);
    }

    public SimulationStatus getStatus() {
        return SimulationStatus.find(SimulationFields.STATUS);
    }

    public void setStatus(SimulationStatus status) {
        put(SimulationFields.STATUS, status.value);
    }

    public PropertyType getPropertyType() {
        return PropertyType.find(getPropertyTypeString());
    }

    public String getPropertyTypeString() {
        return getString(SimulationFields.TIPO_IMOVEL);
    }

    public void setPropertyType(PropertyType propertyType) {
        put(SimulationFields.TIPO_IMOVEL, propertyType.value);
    }

    public String getUf() {
        return getString(SimulationFields.UF);
    }

    public void setUf(String uf) {
        put(SimulationFields.UF, uf);
    }

    public ParseUser getUser() {
        return getParseUser(SimulationFields.USER);
    }

    public void setUser(ParseUser parseUser) {
        put(SimulationFields.USER, parseUser);
    }

    public String getFinancingAmount() {
        return getString(SimulationFields.VALOR_FINANCIAMENTO);
    }

    public void setFinancingAmount(String valorFinanciamento) {
        put(SimulationFields.VALOR_FINANCIAMENTO, valorFinanciamento);
    }

    public String getPropertyPrice() {
        return getString(SimulationFields.VALOR_IMOVEL);
    }

    public void setPropertyPrice(String propertyPrice) {
        put(SimulationFields.VALOR_IMOVEL, propertyPrice);
    }

    public String getPriceAndDate() {
        return String.format("R$ %s - %s", getPropertyPrice(), getDataEnvio());
    }

    public String getStatusString() {
        return getStatus().getValue();
    }

    public String getTypeAndLocation() {
        return String.format("%s - %s", getPropertyType(), getLocation());
    }

    public static ParseQuery<Simulation> getQuery() {
        return ParseQuery.getQuery(Simulation.class);
    }
}
