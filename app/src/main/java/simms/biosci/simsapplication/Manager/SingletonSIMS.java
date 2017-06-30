package simms.biosci.simsapplication.Manager;

import android.content.Context;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class SingletonSIMS {

    private String user;

    private static SingletonSIMS instance;
    public static SingletonSIMS getInstance() {
        if (instance == null)
            instance = new SingletonSIMS();
        return instance;
    }

    private Context mContext;

    private SingletonSIMS() {
        mContext = Contextor.getInstance().getContext();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
