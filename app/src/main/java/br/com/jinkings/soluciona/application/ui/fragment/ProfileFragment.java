package br.com.jinkings.soluciona.application.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.domain.model.User;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileFragment extends MainFragment {
    @InjectView(R.id.profile_fullname)
    TextView textViewName;
    @InjectView(R.id.profile_cellphone)
    TextView textViewCellPhone;
    @InjectView(R.id.profile_email)
    TextView textViewEmail;
    @InjectView(R.id.profile_job_category)
    TextView textViewJobCategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.inject(this, view);

        User user = (User) ParseUser.getCurrentUser();

        textViewName.setText(user.getFullName());
        textViewCellPhone.setText(user.getCellPhone());
        textViewEmail.setText(user.getEmail());
        textViewJobCategory.setText(user.getJobCategory().getDescription());

        return view;
    }

    @Override
    public ViewGroup getRootView() {
        return (ViewGroup) getView().getRootView().findViewById(R.id.home_frame);
    }
}
