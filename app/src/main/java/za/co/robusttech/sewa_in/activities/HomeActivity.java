package za.co.robusttech.sewa_in.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.fragments.Frag1;
import za.co.robusttech.sewa_in.fragments.Frag2;
import za.co.robusttech.sewa_in.fragments.Frag3;
import za.co.robusttech.sewa_in.fragments.Home;
import za.co.robusttech.sewa_in.fragments.Search;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NavigationView navigationView;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.authText);
        LinearLayout linearLayout = (LinearLayout) headerView.findViewById(R.id.authLaout);
        //  navUsername.setText("Your Text Here");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorblack));

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new Home(), "Home");
        viewPagerAdapter.addFragment(new Search(), "Search");
        viewPagerAdapter.addFragment(new Frag3(), "Frag");


        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);




    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        // Ctrl + O

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (menuItem.getItemId()) {
            case R.id.nav_My_store:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.nav_My_Orders:
                Toast.makeText(this, "My Orders", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.nav_My_Rewards:
                Toast.makeText(this, "My Rewards", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.nav_My_Cart:
                Toast.makeText(this, "My Cart", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.nav_My_Wishlist:
                Toast.makeText(this, "My Wishlist", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.nav_My_account:
                Intent profile = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(profile);
                return true;

            case R.id.nav_sign_out:

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;




    }
}