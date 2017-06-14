package simms.biosci.simsapplication.Fragment;

import android.content.Intent;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import simms.biosci.simsapplication.Manager.FeedLocation;
import simms.biosci.simsapplication.R;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class ShowLocationFragment extends Fragment {

    private Typeface montserrat_regular, montserrat_bold;
    private TextView tv_title, tv_location_name, tv_province, tv_district, tv_sub_district,
            tv_select_province, tv_select_district, tv_select_sub_district;
    private Button btn_update, btn_delete;
    private EditText et_location_name;
    private int province = -1, district = -1, sub_district = -1;
    private DatabaseReference mRootRef, mLocationRef;
    private String key;
    private static final int REQUEST_CODE_SHOW = 5;

    public ShowLocationFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static ShowLocationFragment newInstance(String key) {
        ShowLocationFragment fragment = new ShowLocationFragment();
        Bundle args = new Bundle();
        args.putString("KEY", key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_location, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        Bundle bundle = getArguments();
        key = bundle.getString("KEY");
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        montserrat_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.ttf");
        montserrat_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-SemiBold.ttf");
        mRootRef = FirebaseDatabase.getInstance().getReference();

        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        btn_update = (Button) rootView.findViewById(R.id.btn_update);
        btn_delete = (Button) rootView.findViewById(R.id.btn_delete);
        tv_location_name = (TextView) rootView.findViewById(R.id.tv_location_name);
        tv_province = (TextView) rootView.findViewById(R.id.tv_province);
        tv_district = (TextView) rootView.findViewById(R.id.tv_district);
        tv_sub_district = (TextView) rootView.findViewById(R.id.tv_sub_district);
        et_location_name = (EditText) rootView.findViewById(R.id.et_location_name);
        tv_select_province = (TextView) rootView.findViewById(R.id.tv_select_province);
        tv_select_district = (TextView) rootView.findViewById(R.id.tv_select_district);
        tv_select_sub_district = (TextView) rootView.findViewById(R.id.tv_select_sub_district);

        btn_update.setTypeface(montserrat_bold);
        btn_delete.setTypeface(montserrat_bold);
        tv_title.setTypeface(montserrat_bold);
        tv_location_name.setTypeface(montserrat_bold);
        tv_province.setTypeface(montserrat_bold);
        tv_district.setTypeface(montserrat_bold);
        tv_sub_district.setTypeface(montserrat_bold);
        et_location_name.setTypeface(montserrat_regular);
        tv_select_province.setTypeface(montserrat_regular);
        tv_select_district.setTypeface(montserrat_regular);
        tv_select_sub_district.setTypeface(montserrat_regular);

        tv_select_province.setText(Html.fromHtml("<u>Tap to select</u>"));
        tv_select_district.setText(Html.fromHtml("<u>Tap to select</u>"));
        tv_select_sub_district.setText(Html.fromHtml("<u>Tap to select</u>"));

        btn_update.setOnClickListener(btn_update_click);
        btn_delete.setOnClickListener(btn_delete_click);
        tv_select_province.setOnClickListener(tv_select_province_click);
        tv_select_district.setOnClickListener(tv_select_district_click);
        tv_select_sub_district.setOnClickListener(tv_select_sub_district_click);

        mRootRef.child("location").orderByChild("l_key").equalTo(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FeedLocation model = dataSnapshot.getValue(FeedLocation.class);
                et_location_name.setText(model.getL_name() + "");
                tv_select_province.setText(Html.fromHtml("<u>" + model.getL_province() + "</u>"));
                tv_select_province.setTextColor(getResources().getColor(R.color.light_blue));
                tv_select_district.setText(Html.fromHtml("<u>" + model.getL_district() + "</u>"));
                tv_select_district.setTextColor(getResources().getColor(R.color.light_blue));
                tv_select_sub_district.setText(Html.fromHtml("<u>" + model.getL_sub_district() + "</u>"));
                tv_select_sub_district.setTextColor(getResources().getColor(R.color.light_blue));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    View.OnClickListener btn_update_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            btn_update.startAnimation(anim);

            if (et_location_name.getText().toString().equals("") || tv_select_province.getText().toString().equals("Tap to select") || tv_select_province.getText().toString().equals("Tap to select") || tv_select_district.getText().toString().equals("Tap to select")) {
                Toast.makeText(getContext(), "Please fill in all information.", Toast.LENGTH_SHORT).show();
            } else {
                HashMap<String, Object> location = new HashMap<String, Object>();
                location.put("l_name", et_location_name.getText().toString());
                location.put("l_province", tv_select_province.getText().toString());
                location.put("l_district", tv_select_district.getText().toString());
                location.put("l_sub_district", tv_select_sub_district.getText().toString());
                location.put("l_key", key);
                Map<String, Object> child = new HashMap<>();
                child.put(key, location);
                mRootRef.child("location").updateChildren(child);
                Toast.makeText(getContext(), "Update location successfully.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("KEY", key);
                intent.putExtra("l_name", et_location_name.getText().toString());
                intent.putExtra("l_province", tv_select_province.getText().toString());
                intent.putExtra("l_district", tv_select_district.getText().toString());
                intent.putExtra("l_sub_district", tv_select_sub_district.getText().toString());
                intent.putExtra("what2do", "update");
                getActivity().setResult(REQUEST_CODE_SHOW, intent);
                getActivity().finish();
            }
        }
    };

    View.OnClickListener btn_delete_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            btn_delete.startAnimation(anim);

            mRootRef.child("location").child(key).removeValue();
            Toast.makeText(getContext(), "Delete location successfully.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("KEY", key);
            intent.putExtra("what2do", "delete");
            getActivity().setResult(REQUEST_CODE_SHOW, intent);
            getActivity().finish();
        }
    };

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
                    .items("Bangkok", "Phatumthani")
                    .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
                    .itemsCallbackSingleChoice(province, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            tv_select_province.setText(Html.fromHtml("<u>" + text + "</u>"));
                            tv_select_province.setTextColor(getResources().getColor(R.color.light_blue));
                            province = which;
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
                        .items("Klongluang")
                        .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
                        .itemsCallbackSingleChoice(district, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                tv_select_district.setText(Html.fromHtml("<u>" + text + "</u>"));
                                tv_select_district.setTextColor(getResources().getColor(R.color.light_blue));
                                district = which;
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
                        .items("Klong 1", "Klong 3")
                        .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
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
