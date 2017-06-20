package simms.biosci.simsapplication.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simms.biosci.simsapplication.Object.Amphur;
import simms.biosci.simsapplication.Manager.DatabaseHelper;
import simms.biosci.simsapplication.Object.District;
import simms.biosci.simsapplication.Object.Province;
import simms.biosci.simsapplication.R;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class AddLocationFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private Typeface montserrat_regular, montserrat_bold, prompt_regular, prompt_bold;
    private TextView tv_title, tv_location_name, tv_province, tv_district, tv_sub_district,
            tv_select_province, tv_select_district, tv_select_sub_district;
    private Button btn_add, btn_reset;
    private EditText et_location_name;
    private int province = -1, district = -1, sub_district = -1;
    private DatabaseReference mRootRef, mLocationRef;
    private int[] amphurID, amphurProvinceID, districtID, districtAmphurID, districtProvinceID,
            provinceID, provinceCode, provinceGEO_ID;
    private String[] amphurName, districtName, provinceName;

    public AddLocationFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static AddLocationFragment newInstance() {
        AddLocationFragment fragment = new AddLocationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);

        databaseHelper = new DatabaseHelper(getContext());
        try {
            databaseHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_location, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        montserrat_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.ttf");
        montserrat_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-SemiBold.ttf");
        prompt_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Prompt-Regular.ttf");
        prompt_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Prompt-Bold.ttf");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mLocationRef = mRootRef.child("location");

        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_reset = (Button) rootView.findViewById(R.id.btn_reset);
        tv_location_name = (TextView) rootView.findViewById(R.id.tv_location_name);
        tv_province = (TextView) rootView.findViewById(R.id.tv_province);
        tv_district = (TextView) rootView.findViewById(R.id.tv_district);
        tv_sub_district = (TextView) rootView.findViewById(R.id.tv_sub_district);
        et_location_name = (EditText) rootView.findViewById(R.id.et_location_name);
        tv_select_province = (TextView) rootView.findViewById(R.id.tv_select_province);
        tv_select_district = (TextView) rootView.findViewById(R.id.tv_select_district);
        tv_select_sub_district = (TextView) rootView.findViewById(R.id.tv_select_sub_district);

        btn_add.setTypeface(montserrat_bold);
        btn_reset.setTypeface(montserrat_bold);
        tv_title.setTypeface(montserrat_bold);
        tv_location_name.setTypeface(montserrat_bold);
        tv_province.setTypeface(montserrat_bold);
        tv_district.setTypeface(montserrat_bold);
        tv_sub_district.setTypeface(montserrat_bold);
        et_location_name.setTypeface(montserrat_regular);
        tv_select_province.setTypeface(prompt_regular);
        tv_select_district.setTypeface(prompt_regular);
        tv_select_sub_district.setTypeface(prompt_regular);

        tv_select_province.setText(Html.fromHtml("<u>Tap to select</u>"));
        tv_select_district.setText(Html.fromHtml("<u>Tap to select</u>"));
        tv_select_sub_district.setText(Html.fromHtml("<u>Tap to select</u>"));

        btn_add.setOnClickListener(btn_add_click);
        btn_reset.setOnClickListener(btn_reset_click);
        tv_select_province.setOnClickListener(tv_select_province_click);
        tv_select_district.setOnClickListener(tv_select_district_click);
        tv_select_sub_district.setOnClickListener(tv_select_sub_district_click);

        List<Province> listProvince = databaseHelper.getAllProvince();
        provinceID = new int[listProvince.size()];
        provinceCode = new int[listProvince.size()];
        provinceName = new String[listProvince.size()];
        provinceGEO_ID = new int[listProvince.size()];
        for (int i = 0; i < listProvince.size(); i++) {
            provinceID[i] = listProvince.get(i).getProvinceID();
            provinceCode[i] = listProvince.get(i).getProvinceCode();
            provinceName[i] = listProvince.get(i).getProvinceName();
            provinceGEO_ID[i] = listProvince.get(i).getGEO_ID();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

    View.OnClickListener btn_add_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            btn_add.startAnimation(anim);

            if (et_location_name.getText().toString().equals("") || tv_select_province.getText().toString().equals("Tap to select") || tv_select_province.getText().toString().equals("Tap to select") || tv_select_district.getText().toString().equals("Tap to select")) {
                Toast.makeText(getContext(), "Please fill in all information.", Toast.LENGTH_SHORT).show();
            } else {
                String key = mLocationRef.push().getKey();
                HashMap<String, Object> location = new HashMap<String, Object>();
                location.put("l_name", et_location_name.getText().toString());
                location.put("l_province", tv_select_province.getText().toString());
                location.put("l_district", tv_select_district.getText().toString());
                location.put("l_sub_district", tv_select_sub_district.getText().toString());
                location.put("l_key", key);
                Map<String, Object> child = new HashMap<>();
                child.put(key, location);
                mLocationRef.updateChildren(child);
                Toast.makeText(getContext(), "Add location successfully.", Toast.LENGTH_SHORT).show();
                cleanUp();
                getActivity().finish();
            }
        }
    };

    View.OnClickListener btn_reset_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            btn_reset.startAnimation(anim);
            cleanUp();
        }
    };

    private void cleanUp() {
        et_location_name.setText("");
        tv_select_province.setText(Html.fromHtml("<u>Tap to select</u>"));
        tv_select_district.setText(Html.fromHtml("<u>Tap to select</u>"));
        tv_select_sub_district.setText(Html.fromHtml("<u>Tap to select</u>"));
        tv_select_province.setTextColor(getResources().getColor(R.color.soft_gray));
        tv_select_district.setTextColor(getResources().getColor(R.color.soft_gray));
        tv_select_sub_district.setTextColor(getResources().getColor(R.color.soft_gray));
        province = -1;
        district = -1;
        sub_district = -1;
    }

    View.OnClickListener tv_select_province_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            tv_select_province.startAnimation(anim);
            MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
            builder
                    .title("Select Province")
                    .items(provinceName)
                    .typeface("Montserrat-Regular.ttf", "Prompt-Regular.ttf")
                    .itemsCallbackSingleChoice(province, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            tv_select_province.setText(Html.fromHtml("<u>" + text + "</u>"));
                            tv_select_province.setTextColor(getResources().getColor(R.color.light_blue));
                            province = which;
                            List<Amphur> listAmphur = databaseHelper.getAllAmphur(provinceID[which]);
                            amphurID = new int[listAmphur.size()];
                            amphurName = new String[listAmphur.size()];
                            amphurProvinceID = new int[listAmphur.size()];
                            for (int i = 0; i < listAmphur.size(); i++) {
                                amphurID[i] = listAmphur.get(i).getAmphurID();
                                amphurName[i] = listAmphur.get(i).getAmphurName();
                                amphurProvinceID[i] = listAmphur.get(i).getProvinceID();
                            }
                            return true;
                        }
                    })
                    .negativeText("cancel")
                    .show();
        }
    };

    View.OnClickListener tv_select_district_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            tv_select_district.startAnimation(anim);
            if (tv_select_province.getText().toString().equals("Tap to select")) {
                Toast.makeText(getContext(), "Please select province first.", Toast.LENGTH_SHORT).show();
            } else {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
                builder
                        .title("Select District")
                        .items(amphurName)
                        .typeface("Montserrat-Regular.ttf", "Prompt-Regular.ttf")
                        .itemsCallbackSingleChoice(district, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                tv_select_district.setText(Html.fromHtml("<u>" + text + "</u>"));
                                tv_select_district.setTextColor(getResources().getColor(R.color.light_blue));
                                district = which;

                                List<District> listDistrict = databaseHelper.getAllDistrict(amphurID[which]);
                                districtID = new int[listDistrict.size()];
                                districtName = new String[listDistrict.size()];
                                districtAmphurID = new int[listDistrict.size()];
                                districtProvinceID = new int[listDistrict.size()];
                                for (int i = 0; i < listDistrict.size(); i++) {
                                    districtID[i] = listDistrict.get(i).getDistrictID();
                                    districtName[i] = listDistrict.get(i).getDistrictName();
                                    districtAmphurID[i] = listDistrict.get(i).getAmphurID();
                                    districtProvinceID[i] = listDistrict.get(i).getProvinceID();
                                }
                                return true;
                            }
                        })
                        .negativeText("cancel")
                        .show();
            }
        }
    };

    View.OnClickListener tv_select_sub_district_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            tv_select_sub_district.startAnimation(anim);
            if (tv_select_province.getText().toString().equals("Tap to select") || tv_select_district.getText().toString().equals("Tap to select")) {
                Toast.makeText(getContext(), "Please select district first.", Toast.LENGTH_SHORT).show();
            } else {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
                builder
                        .title("Select Sub-district")
                        .items(districtName)
                        .typeface("Montserrat-Regular.ttf", "Prompt-Regular.ttf")
                        .itemsCallbackSingleChoice(sub_district, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                tv_select_sub_district.setText(Html.fromHtml("<u>" + text + "</u>"));
                                tv_select_sub_district.setTextColor(getResources().getColor(R.color.light_blue));
                                sub_district = which;
                                return true;
                            }
                        })
                        .negativeText("cancel")
                        .show();
            }
        }
    };
}
