package br.com.jinkings.soluciona.domain.model;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {

    public User() {
        super();
    }

    public void setFullName(String fullName) {
        put(UserFields.NOME, fullName);
    }

    public String getFullName() {
        return getString(UserFields.NOME);
    }

    public void setPhone(String phone) {
        put(UserFields.TELEFONE, phone);
    }

    public String getPhone() {
        return getString(UserFields.TELEFONE);
    }

    public void setCellPhone(String cellPhone) {
        put(UserFields.CELULAR, cellPhone);
    }

    public String getCellPhone() {
        return getString(UserFields.CELULAR);
    }

    public void setCpf(String cpf) {
        put(UserFields.CPF, cpf);
    }

    public String getCpf() {
        return getString(UserFields.CPF);
    }

    public void setJobCategory(JobCategory jobCategory) {
        put(UserFields.CATEGORIA_PROFISSIONAL, jobCategory);
    }

    public JobCategory getJobCategory() {
        return (JobCategory) getParseObject(UserFields.CATEGORIA_PROFISSIONAL);
    }
}
