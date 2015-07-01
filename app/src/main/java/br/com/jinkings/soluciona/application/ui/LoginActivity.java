package br.com.jinkings.soluciona.application.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import br.com.jinkings.financing.R;
import br.com.m4u.commons.brazilian.library.validator.BrazilianValidator;
import butterknife.InjectView;
import butterknife.OnClick;


public class LoginActivity extends MainActivity implements Validator.ValidationListener {

    @InjectView(R.id.login_text_input_email)
    @Email(sequence = 1, messageResId = R.string.invalid_email)
    EditText editTextEmail;

    @InjectView(R.id.login_text_input_password)
    @Password(sequence = 2, messageResId = R.string.invalid_password)
    EditText editTextPassword;

    BrazilianValidator validator;

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validator = new BrazilianValidator(this);
        validator.setValidationListener(this);

        if (ParseUser.getCurrentUser() != null) {
            loggedIn();
        }
    }

    @OnClick(R.id.login_button_signin)
    public void signIn() {
        validator.validate();
    }

    @OnClick(R.id.login_textview_signup)
    public void signUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onValidationSucceeded() {
        startProgress();

        String user = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        ParseUser.logInInBackground(user, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {

                if (e != null) {

                    int errorMsgId = R.string.login_failed_default_message;

                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        errorMsgId = R.string.login_user_not_found_message;
                    }

                    justSnackIt(errorMsgId);
                } else {
                    loggedIn();
                }

                finishProgress();
            }
        });
    }

    private void loggedIn() {
        Intent intent = new Intent(LoginActivity.this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError v : errors) {
            EditText editText = (EditText) v.getView();
            editText.setError(v.getCollatedErrorMessage(this));
        }

        justSnackIt(R.string.invalid_form_message);
    }
}
