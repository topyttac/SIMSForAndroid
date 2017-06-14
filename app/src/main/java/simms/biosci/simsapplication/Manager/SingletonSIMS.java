package simms.biosci.simsapplication.Manager;

import android.content.Context;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class SingletonSIMS {

    private static SingletonSIMS instance;
    private int selectedType;
    private String selectedTypeName;
    public static SingletonSIMS getInstance() {
        if (instance == null)
            instance = new SingletonSIMS();
        return instance;
    }

    private Context mContext;

    private SingletonSIMS() {
        mContext = Contextor.getInstance().getContext();
    }

    public int getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(int selectedType) {
        this.selectedType = selectedType;
    }

    public String getSelectedTypeName() {
        return selectedTypeName;
    }

    public void setSelectedTypeName(String selectedTypeName) {
        this.selectedTypeName = selectedTypeName;
    }
}
