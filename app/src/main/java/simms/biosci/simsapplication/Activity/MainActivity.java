package simms.biosci.simsapplication.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import simms.biosci.simsapplication.Fragment.CrossFragment;
import simms.biosci.simsapplication.Fragment.GermplasmFragment;
import simms.biosci.simsapplication.Fragment.LocationFragment;
import simms.biosci.simsapplication.Fragment.SourceFragment;
import simms.biosci.simsapplication.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LinearLayout ll_germplasm, ll_location, ll_resource, ll_cross, ll_setting;
    private Typeface montserrat_regular, montserrat_bold;
    private TextView tv_sims, tv_germplasm, tv_location, tv_resource, tv_cross, tv_setting, tv_released;
    private static final int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer,
                            GermplasmFragment.newInstance())
                    .commit();
        }
    }

    private void initInstances() {
        montserrat_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        montserrat_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ll_germplasm = (LinearLayout) findViewById(R.id.ll_germplasm);
        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        ll_resource = (LinearLayout) findViewById(R.id.ll_resource);
        ll_cross = (LinearLayout) findViewById(R.id.ll_cross);
        ll_setting = (LinearLayout) findViewById(R.id.ll_setting);
        tv_sims = (TextView) findViewById(R.id.tv_sims);
        tv_germplasm = (TextView) findViewById(R.id.tv_germplasm);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_resource = (TextView) findViewById(R.id.tv_resource);
        tv_cross = (TextView) findViewById(R.id.tv_cross);
        tv_setting = (TextView) findViewById(R.id.tv_setting);
        tv_released = (TextView) findViewById(R.id.tv_released);

        tv_sims.setTypeface(montserrat_bold);
        tv_germplasm.setTypeface(montserrat_bold);
        tv_location.setTypeface(montserrat_bold);
        tv_resource.setTypeface(montserrat_bold);
        tv_cross.setTypeface(montserrat_bold);
        tv_setting.setTypeface(montserrat_bold);
        tv_released.setTypeface(montserrat_regular);

        drawerLayout.openDrawer(Gravity.LEFT);
        ll_germplasm.setBackgroundColor(Color.parseColor("#EEEEEE"));
        setSupportActionBar(toolbar); // set toolbar

        // start hamburger
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //end hamburger

        ll_germplasm.setOnClickListener(ll_germplasm_click);
        ll_location.setOnClickListener(ll_location_click);
        ll_resource.setOnClickListener(ll_resource_click);
        ll_cross.setOnClickListener(ll_cross_click);
        ll_setting.setOnClickListener(ll_setting_click);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState(); // hamburger
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig); // hamburger
        actionBarDrawerToggle.onConfigurationChanged(newConfig); // hamburger
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) // when press hamburger
            return true;
        else if (item.getItemId() == R.id.menu_home) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            return true;
        } else if (item.getItemId() == R.id.menu_add) {
            Intent intent = new Intent(MainActivity.this, AddGermplasmActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*############ LISTENER #############*/

    View.OnClickListener ll_germplasm_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ll_germplasm.setBackgroundColor(Color.parseColor("#EEEEEE"));
            ll_location.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_resource.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_cross.setBackgroundColor(Color.parseColor("#FFFFFF"));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer,
                            GermplasmFragment.newInstance())
                    .commit();
            drawerLayout.closeDrawers();
        }
    };

    View.OnClickListener ll_location_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ll_germplasm.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_location.setBackgroundColor(Color.parseColor("#EEEEEE"));
            ll_resource.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_cross.setBackgroundColor(Color.parseColor("#FFFFFF"));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer,
                            LocationFragment.newInstance())
                    .commit();
            drawerLayout.closeDrawers();
        }
    };

    View.OnClickListener ll_resource_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ll_germplasm.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_location.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_resource.setBackgroundColor(Color.parseColor("#EEEEEE"));
            ll_cross.setBackgroundColor(Color.parseColor("#FFFFFF"));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer,
                            SourceFragment.newInstance())
                    .commit();
            drawerLayout.closeDrawers();
        }
    };

    View.OnClickListener ll_cross_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ll_germplasm.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_location.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_resource.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_cross.setBackgroundColor(Color.parseColor("#EEEEEE"));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer,
                            CrossFragment.newInstance())
                    .commit();
            drawerLayout.closeDrawers();
        }
    };

    View.OnClickListener ll_setting_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    };
    /*############ LISTENER #############*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {

        }
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
}
