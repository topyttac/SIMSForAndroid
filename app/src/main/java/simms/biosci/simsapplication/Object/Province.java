package simms.biosci.simsapplication.Object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by topyttac on 3/4/2017 AD.
 */

public class Province implements Parcelable{
    private int provinceID;
    private int provinceCode;
    private String provinceName;
    private int GEO_ID;

    protected Province(Parcel in) {
        provinceID = in.readInt();
        provinceCode = in.readInt();
        provinceName = in.readString();
        GEO_ID = in.readInt();
    }

    public static final Creator<Province> CREATOR = new Creator<Province>() {
        @Override
        public Province createFromParcel(Parcel in) {
            return new Province(in);
        }

        @Override
        public Province[] newArray(int size) {
            return new Province[size];
        }
    };

    public Province() {
        super();
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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
        parcel.writeInt(provinceID);
        parcel.writeInt(provinceCode);
        parcel.writeString(provinceName);
        parcel.writeInt(GEO_ID);
    }
}
