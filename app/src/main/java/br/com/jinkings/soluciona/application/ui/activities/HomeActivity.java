package br.com.jinkings.soluciona.application.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.fragment.AboutUsFragment;
import br.com.jinkings.soluciona.application.ui.fragment.ProfileFragment;
import br.com.jinkings.soluciona.application.ui.fragment.SimulationsFragment;
import butterknife.InjectView;
import butterknife.OnClick;

public class HomeActivity extends MainActivity {
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.nav_header_username)
    TextView textViewNavHeaderUsername;

    @InjectView(R.id.home_nav_view)
    NavigationView navigationView;

    ParseUser user;

    @Override
    protected int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        rootView = (ViewGroup) findViewById(R.id.home_frame);

        setupDrawerContent(navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        openSimulationsFragment();
                        return true;
                    // For rest of the options we just show a toast on click
                    case R.id.nav_profile:
                        openProfileFragment();
                        return true;
                    case R.id.nav_about_us:
                        openAboutUsFragment();
                        return true;
                }

                return false;
            }
        });

        user = ParseUser.getCurrentUser();

        String username = user.get("nome").toString();

        textViewNavHeaderUsername.setText(username);

        openSimulationsFragment();
    }

    private void openProfileFragment() {
        ProfileFragment profileFragment = new ProfileFragment();
        openFragment(profileFragment, R.string.title_profile);
    }

    private void openAboutUsFragment() {
        AboutUsFragment aboutUsFragment = AboutUsFragment.newInstance("", "");
        openFragment(aboutUsFragment, R.string.title_about_us_fragment);
    }

    private void openSimulationsFragment() {
        SimulationsFragment simulationsFragment = new SimulationsFragment();
        openFragment(simulationsFragment, R.string.title_simulations_fragment);
    }

    private void openFragment(Fragment fragment, int titleId) {
        setTitle(titleId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.home_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @OnClick(R.id.nav_header_logout)
    public void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
