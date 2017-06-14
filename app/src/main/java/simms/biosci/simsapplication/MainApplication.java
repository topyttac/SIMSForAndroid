package simms.biosci.simsapplication;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by User 2 on 6/5/2017.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // init herer
        Contextor.getInstance().init(getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
