package simms.biosci.simsapplication.Manager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import simms.biosci.simsapplication.Object.Amphur;
import simms.biosci.simsapplication.Object.District;
import simms.biosci.simsapplication.Object.Province;

/**
 * Created by topyttac on 3/4/2017 AD.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DB_PATH = "/data/data/simms.biosci.simsapplication/databases/";

    public static String DB_NAME = "database.octet-stream";

    private SQLiteDatabase myDB;

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("tle99 - create", e.getMessage());
            }
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("tle99 - create", e.getMessage());
            }
        }

    }

    private boolean checkDataBase() {

        SQLiteDatabase tempDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            Log.e("tle99 - check", e.getMessage());
        }

        if (tempDB != null) {
            tempDB.close();
        }

        return tempDB != null ? true : false;
    }

    public void copyDataBase() throws IOException {
        try {
            InputStream myInput = context.getAssets().open(DB_NAME);
            String outputFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.e("tle99 - copyDatabase", e.getMessage());
        }
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if (myDB != null) {
            myDB.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Amphur> getAllAmphur(int province) {
        List<Amphur> listAmphur = new ArrayList<>();
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        Cursor cursor;
        try {
            cursor = mDatabase.rawQuery("SELECT * FROM amphur WHERE provinceID = ?", new String[]{province + ""});

            if (cursor == null) {
                return null;
            } else {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Amphur amphur = CursorAmphur(cursor);
                    listAmphur.add(amphur);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("tle99", e.getMessage());
        }
        return listAmphur;
    }

    private Amphur CursorAmphur(Cursor cursor) {
        Amphur amphur = new Amphur();
        amphur.setAmphurID(cursor.getInt(0));
        amphur.setAmphurCode(cursor.getInt(1));
        amphur.setAmphurName(cursor.getString(2));
        amphur.setGEO_ID(cursor.getInt(3));
        amphur.setProvinceID(cursor.getInt(4));
        return amphur;
    }

    public List<District> getAllDistrict(int amphur) {
        List<District> listDistrict = new ArrayList<>();
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        Cursor cursor;
        try {
            cursor = mDatabase.rawQuery("SELECT * FROM district WHERE amphurID = ?", new String[]{amphur + ""});

            if (cursor == null) {
                return null;
            } else {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    District district = CursorDistrict(cursor);
                    listDistrict.add(district);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("tle99", e.getMessage());
        }
        return listDistrict;
    }

    private District CursorDistrict(Cursor cursor) {
        District district = new District();
        district.setDistrictID(cursor.getInt(0));
        district.setDistrictCode(cursor.getInt(1));
        district.setDistrictName(cursor.getString(2));
        district.setAmphurID(cursor.getInt(3));
        district.setProvinceID(cursor.getInt(4));
        district.setGEO_ID(cursor.getInt(5));
        return district;
    }

    public List<Province> getAllProvince() {
        List<Province> listProvince = new ArrayList<>();
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        Cursor cursor;
        try {
            cursor = mDatabase.rawQuery("SELECT * FROM province", null);

            if (cursor == null) {
                return null;
            } else {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Province province = CursorProvince(cursor);
                    listProvince.add(province);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("tle99", e.getMessage());
        }
        return listProvince;
    }

    private Province CursorProvince(Cursor cursor) {
        Province province = new Province();
        province.setProvinceID(cursor.getInt(0));
        province.setProvinceCode(cursor.getInt(1));
        province.setProvinceName(cursor.getString(2));
        province.setGEO_ID(cursor.getInt(3));
        return province;
    }

}
