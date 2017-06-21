package simms.biosci.simsapplication.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import simms.biosci.simsapplication.Fragment.AddCrossFragment;
import simms.biosci.simsapplication.Fragment.ShowCrossFragment;
import simms.biosci.simsapplication.R;

public class AddCrossActivity extends AppCompatActivity {

    private Typeface montserrat_regular, montserrat_bold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cross);

        Intent intent = getIntent();
        String key = intent.getStringExtra("KEY");
        int REQUEST_CODE = intent.getIntExtra("REQUEST_CODE", 7);
        initInstance();
        if (savedInstanceState == null) {
            if (REQUEST_CODE == 7) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.contentContainer,
                                AddCrossFragment.newInstance())
                        .commit();
            } else if (REQUEST_CODE == 8) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.contentContainer,
                                ShowCrossFragment.newInstance(key))
                        .commit();
            }

        }

        setTitle("Add");
    }

    private void initInstance() {
        montserrat_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        montserrat_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.menu_home) {
            finish();
            Intent intent = new Intent(AddCrossActivity.this, SearchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        }
        return super.onOptionsItemSelected(item);
    }
}
