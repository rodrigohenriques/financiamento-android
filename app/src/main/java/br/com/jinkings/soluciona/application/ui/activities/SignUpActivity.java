package br.com.jinkings.soluciona.application.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

import br.com.jinkings.financing.R;
import br.com.m4u.commons.brazilian.library.validator.BrazilianValidator;
import br.com.m4u.commons.brazilian.library.validator.saripaar.annotations.Cpf;
import br.com.m4u.commons.brazilian.library.validator.saripaar.annotations.MobilePhone;
import butterknife.InjectView;
import butterknife.OnClick;


@SuppressWarnings("WeakerAccess")
public class SignUpActivity extends MainActivity implements Validator.ValidationListener {
    @InjectView(R.id.signup_text_input_fullname)
    @NotEmpty(sequence = 0, messageResId = R.string.invalid_fullname)
    EditText editTextFullName;
    @InjectView(R.id.signup_text_input_cpf)
    @Cpf(sequence = 1, messageResId = R.string.invalid_cpf)
    EditText editTextCPF;
    @InjectView(R.id.signup_text_input_phone)
    @Pattern(sequence = 2, regex = "(10)|([1-9][1-9])[2-9][0-9]{3}[0-9]{4}", messageResId = R.string.invalid_phone)
    EditText editTextPhone;
    @InjectView(R.id.signup_text_input_cellphone)
    @MobilePhone(sequence = 3, messageResId = R.string.invalid_mobile_phone)
    EditText editTextCellPhone;
    @InjectView(R.id.signup_text_input_job_category)
    @NotEmpty(sequence = 4, messageResId = R.string.invalid_job_category)
    EditText editTextJobCategory;
    @InjectView(R.id.signup_text_input_email)
    @Email(sequence = 5, messageResId = R.string.invalid_email)
    EditText editTextEmail;
    @InjectView(R.id.signup_text_input_password)
    @Password(sequence = 6, messageResId = R.string.invalid_password)
    EditText editTextPassword;
    @InjectView(R.id.signup_text_input_confirm_password)
    @ConfirmPassword(sequence = 7, messageResId = R.string.invalid_password_confirmation)
    EditText editTextConfirmPassword;

    BrazilianValidator validator;

    String[] stringArrayJobCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validator = new BrazilianValidator(this);
        validator.setValidationListener(this);

        stringArrayJobCategories = getResources().getStringArray(R.array.job_categories);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_signup;
    }

    @OnClick(R.id.button_confirm)
    public void signUp() {
        validator.validate();
    }

    @OnClick(R.id.signup_text_input_job_category)
    public void selectJobType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.hint_job_category);
        builder.setItems(stringArrayJobCategories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                editTextJobCategory.setText(stringArrayJobCategories[selectedIndex]);
            }
        });
        builder.setNegativeButton(R.string.signup_job_type_dialog_cancel_button, null);
        builder.create().show();
    }

    @Override
    public void onValidationSucceeded() {
        startProgress();

        ParseUser parseUser = new ParseUser();

        parseUser.setUsername(editTextEmail.getText().toString());
        parseUser.setEmail(editTextEmail.getText().toString());
        parseUser.setPassword(editTextPassword.getText().toString());

        parseUser.put("nome", editTextFullName.getText().toString());
        parseUser.put("telefone", editTextPhone.getText().toString());
        parseUser.put("celular", editTextCellPhone.getText().toString());
        parseUser.put("cpf", editTextCPF.getText().toString());
        parseUser.put("categoriaProfissional", editTextJobCategory.getText().toString());

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                finishProgress();

                if (e != null) {

                    logError(e);

                    Snackbar snackbar = Snackbar.make(editTextFullName, R.string.default_signup_error_message, Snackbar.LENGTH_LONG);

                    snackbar.setAction(R.string.signup_snackbar_action_retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signUp();
                        }
                    });

                    snackbar.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);

                    builder.setMessage(R.string.signup_success);
                    builder.setPositiveButton(R.string.signup_success_dialog_positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });

                    builder.create().show();
                }
            }
        });
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
