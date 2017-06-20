package simms.biosci.simsapplication.Object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by topyttac on 3/4/2017 AD.
 */

public class District implements Parcelable{
    private int districtID;
    private int districtCode;
    private String districtName;
    private int amphurID;
    private int provinceID;
    private int GEO_ID;

    protected District(Parcel in) {
        districtID = in.readInt();
        districtCode = in.readInt();
        districtName = in.readString();
        amphurID = in.readInt();
        provinceID = in.readInt();
        GEO_ID = in.readInt();
    }

    public static final Creator<District> CREATOR = new Creator<District>() {
        @Override
        public District createFromParcel(Parcel in) {
            return new District(in);
        }

        @Override
        public District[] newArray(int size) {
            return new District[size];
        }
    };

    public District() {
        super();
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public int getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(int districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getAmphurID() {
        return amphurID;
    }

    public void setAmphurID(int amphurID) {
        this.amphurID = amphurID;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public int getGEO_ID() {
        return GEO_ID;
    }

    public void setGEO_ID(int GEO_ID) {
        this.GEO_ID = GEO_ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(districtID);
        parcel.writeInt(districtCode);
        parcel.writeString(districtName);
        parcel.writeInt(amphurID);
        parcel.writeInt(provinceID);
        parcel.writeInt(GEO_ID);
    }
}
