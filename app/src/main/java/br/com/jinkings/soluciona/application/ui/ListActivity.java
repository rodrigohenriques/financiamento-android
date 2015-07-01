package br.com.jinkings.soluciona.application.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseUser;

import br.com.jinkings.financing.R;
import butterknife.InjectView;

/**
 * Created by rodrigohenriques on 6/30/15.
 */
public class ListActivity extends MainActivity {
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.nav_header_username)
    TextView textViewNavHeaderUsername;

    @InjectView(R.id.nav_view)
    NavigationView navigationView;

    ParseUser user;

    @Override
    protected int getContentView() {
        return R.layout.activity_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        final ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        user = ParseUser.getCurrentUser();

        String username =  user.get("nome").toString();

        textViewNavHeaderUsername.setText(username);
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
}
