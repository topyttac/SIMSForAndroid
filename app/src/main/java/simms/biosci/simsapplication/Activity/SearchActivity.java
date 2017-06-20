package simms.biosci.simsapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import simms.biosci.simsapplication.Fragment.MainFragment;
import simms.biosci.simsapplication.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer,
                            MainFragment.newInstance())
                    .commit();
        }
    }
}
