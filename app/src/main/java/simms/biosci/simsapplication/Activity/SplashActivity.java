package simms.biosci.simsapplication.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import simms.biosci.simsapplication.R;

public class SplashActivity extends AppCompatActivity {

    private TextView tv_title, tv_power_by;
    private Typeface montserrat_regular, montserrat_bold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        montserrat_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        montserrat_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_power_by = (TextView) findViewById(R.id.tv_power_by);
        tv_title.setTypeface(montserrat_bold);
        tv_power_by.setTypeface(montserrat_bold);

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }

        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
