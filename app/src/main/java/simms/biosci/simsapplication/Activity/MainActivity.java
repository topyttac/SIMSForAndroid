package simms.biosci.simsapplication.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import simms.biosci.simsapplication.Fragment.GermplasmFragment;
import simms.biosci.simsapplication.Fragment.LocationFragment;
import simms.biosci.simsapplication.Fragment.MainFragment;
import simms.biosci.simsapplication.Fragment.SourceFragment;
import simms.biosci.simsapplication.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LinearLayout ll_search, ll_germplasm, ll_location, ll_resource;
    private Typeface montserrat_regular, montserrat_bold;
    private TextView tv_search, tv_germplasm, tv_location, tv_resource;
    private static final int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer,
                            MainFragment.newInstance())
                    .commit();
        }
    }

    private void initInstances() {
        montserrat_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        montserrat_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ll_germplasm = (LinearLayout) findViewById(R.id.ll_germplasm);
        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        ll_resource = (LinearLayout) findViewById(R.id.ll_resource);
        tv_search = (TextView) findViewById(R.id.tv_title);
        tv_germplasm = (TextView) findViewById(R.id.tv_germplasm);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_resource = (TextView) findViewById(R.id.tv_resource);

        tv_search.setTypeface(montserrat_bold);
        tv_germplasm.setTypeface(montserrat_bold);
        tv_location.setTypeface(montserrat_bold);
        tv_resource.setTypeface(montserrat_bold);

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

        ll_search.setOnClickListener(ll_search_click);
        ll_germplasm.setOnClickListener(ll_germplasm_click);
        ll_location.setOnClickListener(ll_location_click);
        ll_resource.setOnClickListener(ll_resource_click);
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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer,
                            MainFragment.newInstance())
                    .commit();
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
    View.OnClickListener ll_search_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
//            BounceInterpolator interpolator = new BounceInterpolator();
//            anim.setInterpolator(interpolator);
//            ll_search.startAnimation(anim);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer,
                            MainFragment.newInstance())
                    .commit();
            drawerLayout.closeDrawers();
        }
    };

    View.OnClickListener ll_germplasm_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer,
                            SourceFragment.newInstance())
                    .commit();
            drawerLayout.closeDrawers();
        }
    };
    /*############ LISTENER #############*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {

        }
    }
}
