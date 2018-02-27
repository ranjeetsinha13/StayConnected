package com.rs.stayconnected.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.rs.stayconnected.BuildConfig;
import com.rs.stayconnected.R;
import com.rs.stayconnected.di.Injectable;
import com.rs.stayconnected.di.UserViewModelFactory;
import com.rs.stayconnected.entity.User;
import com.rs.stayconnected.ui.details.ProfileFragment;
import com.rs.stayconnected.viewmodel.UserListViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

import static com.rs.stayconnected.utils.Constants.EMAIL_ID;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        Injectable, HasSupportFragmentInjector {

    @Inject
    UserViewModelFactory userViewModelFactory;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setHomeFragment();
        initViews();

    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        Timber.d("fragment count %s", fragments);
        if (fragments <= 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }


    private void setHomeFragment() {
        UserListViewModel userListViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserListViewModel.class);
        userListViewModel.getUsersListByType(false).observe(this, (List<User> users) -> {
            Timber.i("Users changed" + users + users.size());
            if (users.size() == 0) {
                initToolBar(true);
                //Start Edit Profile Fragment
                Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(ProfileFragment.class.getName());
                if (fragmentByTag == null) {
                    fragmentByTag = new ProfileFragment();
                }
                setFragment(fragmentByTag);
            } else {
                user = users.get(0);
                initToolBar(false);
                // Start Contacts List Fragment with list of Contacts;
                Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(ContactsListFragment.class.getName());
                if (fragmentByTag == null) {
                    fragmentByTag = new ContactsListFragment();
                }
                setFragment(fragmentByTag);
            }

        });

    }


    private void setFragment(Fragment fragment) {
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate
                (fragment.getClass().getName(), 0);
        if (!fragmentPopped && getSupportFragmentManager().findFragmentByTag(fragment.getClass().getName()) == null) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content_main, fragment, fragment.getClass().getName()).
                    addToBackStack(fragment.getClass().getName()).commit();
        }

    }

    private void initViews() {

        // Set Version Name in the Navigation Bar
        TextView versionTV = findViewById(R.id.version);
        versionTV.setText(BuildConfig.VERSION_NAME);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initToolBar(boolean isToolBarDisabled) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (isToolBarDisabled) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_about:
                break;
            case R.id.nav_feedback:
                break;
            case R.id.nav_home:
                setHomeFragment();
                break;
            case R.id.nav_profile:
                Fragment f = getSupportFragmentManager().findFragmentByTag(ProfileFragment.class.getName());
                if (f == null) {
                    f = new ProfileFragment();
                }
                Bundle args = new Bundle();
                args.putString(EMAIL_ID, user.getEmailId());
                f.setArguments(args);
                setFragment(f);
                break;
            case R.id.nav_share:
                shareApp();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the fragment, which will
        // then pass the result to the login button.
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(ProfileFragment.class.getName());
        if (fragment != null && fragment instanceof ProfileFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void shareApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String sAux = "\n" + String.format(getString(R.string.share_subject), getString(R.string.app_name)) + "\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";

            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }
}
