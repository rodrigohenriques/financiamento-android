package br.com.jinkings.soluciona.application.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.adapter.SimulationsFragment;
import br.com.jinkings.soluciona.domain.model.Simulation;
import butterknife.InjectView;

/**
 * Created by rodrigohenriques on 6/30/15.
 */
public class HomeActivity extends MainActivity {
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.nav_header_username)
    TextView textViewNavHeaderUsername;

    @InjectView(R.id.home_nav_view)
    NavigationView navigationView;

    ParseUser user;
    List<Simulation> simulations;

    ActionBar supportActionBar;

    @Override
    protected int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        rootView = (ViewGroup) findViewById(R.id.home_frame);

        supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        setupDrawerContent(navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        openSimulationList();
                        return true;
                    // For rest of the options we just show a toast on click
                    case R.id.nav_profile:
                        Toast.makeText(getApplicationContext(), "Profile Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_help:
                        Toast.makeText(getApplicationContext(), "Help Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_about_us:
                        Toast.makeText(getApplicationContext(), "About us Selected", Toast.LENGTH_SHORT).show();
                        return true;
                }

                return false;
            }
        });

        user = ParseUser.getCurrentUser();

        String username = user.get("nome").toString();

        textViewNavHeaderUsername.setText(username);

        startProgress();

        ParseQuery<Simulation> query = Simulation.getQuery();

        query.findInBackground(new FindCallback<Simulation>() {
            @Override
            public void done(List<Simulation> list, ParseException e) {

                if (e != null) {
                    int errorMsgId = R.string.default_loading_simulations_error_message;

                    justSnackIt(errorMsgId);
                } else {
                    simulations = list;
                    openSimulationList();
                }

                finishProgress();
            }
        });
    }

    private void openSimulationList() {
        setTitle(R.string.title_list_activity);
        SimulationsFragment simulationsFragment = new SimulationsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_frame, simulationsFragment);
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

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);

        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);

        if (supportActionBar != null) {
            supportActionBar.setTitle(titleId);
        }
    }

    public List<Simulation> getSimulations() {
        return simulations;
    }
}
