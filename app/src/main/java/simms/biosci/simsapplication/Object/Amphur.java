package simms.biosci.simsapplication.Object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by topyttac on 3/4/2017 AD.
 */

public class Amphur implements Parcelable {
    private int amphurID;
    private int amphurCode;
    private String amphurName;
    private int GEO_ID;
    private int provinceID;

    protected Amphur(Parcel in) {
        amphurID = in.readInt();
        amphurName = in.readString();
        amphurCode = in.readInt();
        GEO_ID = in.readInt();
        provinceID = in.readInt();
    }

    public static final Creator<Amphur> CREATOR = new Creator<Amphur>() {
        @Override
        public Amphur createFromParcel(Parcel in) {
            return new Amphur(in);
        }

        @Override
        public Amphur[] newArray(int size) {
            return new Amphur[size];
        }
    };

    public Amphur() {
        super();
    }

    public int getAmphurID() {
        return amphurID;
    }

    public void setAmphurID(int amphurID) {
        this.amphurID = amphurID;
    }

    public int getAmphurCode() {
        return amphurCode;
    }

    public void setAmphurCode(int amphurCode) {
        this.amphurCode = amphurCode;
    }

    public String getAmphurName() {
        return amphurName;
    }

    public void setAmphurName(String amphurName) {
        this.amphurName = amphurName;
    }

    public int getGEO_ID() {
        return GEO_ID;
    }

    public void setGEO_ID(int GEO_ID) {
        this.GEO_ID = GEO_ID;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(amphurID);
        parcel.writeInt(amphurCode);
        parcel.writeString(amphurName);
        parcel.writeInt(GEO_ID);
        parcel.writeInt(provinceID);
    }
}
