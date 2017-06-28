package simms.biosci.simsapplication.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;

import simms.biosci.simsapplication.Fragment.AddCrossFragment;
import simms.biosci.simsapplication.Fragment.ShowCrossFragment;
import simms.biosci.simsapplication.Manager.TypefaceSpan;
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
                SpannableString s = new SpannableString("Add Cross");
                s.setSpan(new TypefaceSpan(AddCrossActivity.this, "Montserrat-SemiBold.ttf"), 0, s.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                getSupportActionBar().setTitle(s);
            } else if (REQUEST_CODE == 8) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.contentContainer,
                                ShowCrossFragment.newInstance(key))
                        .commit();
                SpannableString s = new SpannableString("Detail");
                s.setSpan(new TypefaceSpan(AddCrossActivity.this, "Montserrat-SemiBold.ttf"), 0, s.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                getSupportActionBar().setTitle(s);
            }

        }
    }

    private void initInstance() {
        montserrat_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        montserrat_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
