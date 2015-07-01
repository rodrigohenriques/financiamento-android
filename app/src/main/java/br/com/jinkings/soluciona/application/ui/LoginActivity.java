package br.com.jinkings.soluciona.application.ui;

import android.content.Intent;

import br.com.jinkings.financing.R;
import butterknife.OnClick;


public class LoginActivity extends MainActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.textview_signup)
    public void signUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
