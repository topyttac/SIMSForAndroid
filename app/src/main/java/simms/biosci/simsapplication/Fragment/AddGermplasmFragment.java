package simms.biosci.simsapplication.Fragment;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simms.biosci.simsapplication.Manager.SingletonSIMS;
import simms.biosci.simsapplication.Object.FeedCross;
import simms.biosci.simsapplication.Object.FeedLocation;
import simms.biosci.simsapplication.Object.FeedSource;
import simms.biosci.simsapplication.R;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class AddGermplasmFragment extends Fragment {

    private SingletonSIMS sims;
    private Typeface montserrat_regular, montserrat_bold;
    private TextInputLayout tv_germplasm, tv_cross, tv_source, tv_lot, tv_location, tv_stock,
            tv_balance, tv_room, tv_shelf, tv_row, tv_box, tv_note;
    private EditText et_germplasm, et_lot, et_stock, et_balance, et_room, et_shelf, et_row, et_box, et_note,
            tv_select_source, tv_select_location, tv_select_cross;
    private Button btn_add, btn_reset;
    private int source = -1, location = -1, cross = -1;
    private DatabaseReference mRootRef, mGermplasmRef;
    private List<FeedLocation> feedLocation;
    private List<FeedSource> feedSources;
    private List<FeedCross> feedCrosses;
    private String[] locationList, sourceList, crossList;

    public AddGermplasmFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static AddGermplasmFragment newInstance() {
        AddGermplasmFragment fragment = new AddGermplasmFragment();
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

        sims = SingletonSIMS.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mGermplasmRef = mRootRef.child(sims.getUser()).child("germplasm");
        mRootRef.child(sims.getUser()).child("source").orderByChild("s_name").addChildEventListener(select_source_click);
        mRootRef.child(sims.getUser()).child("location").orderByChild("l_name").addChildEventListener(select_location_click);
        mRootRef.child(sims.getUser()).child("cross").orderByChild("c_name").addChildEventListener(select_cross_click);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_germplasm, container, false);
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

        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_reset = (Button) rootView.findViewById(R.id.btn_reset);
        tv_germplasm = (TextInputLayout) rootView.findViewById(R.id.tv_germplasm);
        tv_cross = (TextInputLayout) rootView.findViewById(R.id.tv_cross);
        tv_source = (TextInputLayout) rootView.findViewById(R.id.tv_source);
        tv_select_source = (EditText) rootView.findViewById(R.id.tv_select_source);
        tv_lot = (TextInputLayout) rootView.findViewById(R.id.tv_lot);
        tv_location = (TextInputLayout) rootView.findViewById(R.id.tv_location);
        tv_select_location = (EditText) rootView.findViewById(R.id.tv_select_location);
        tv_stock = (TextInputLayout) rootView.findViewById(R.id.tv_stock);
        tv_balance = (TextInputLayout) rootView.findViewById(R.id.tv_balance);
        tv_room = (TextInputLayout) rootView.findViewById(R.id.tv_room);
        tv_shelf = (TextInputLayout) rootView.findViewById(R.id.tv_shelf);
        tv_row = (TextInputLayout) rootView.findViewById(R.id.tv_row);
        tv_box = (TextInputLayout) rootView.findViewById(R.id.tv_box);
        tv_note = (TextInputLayout) rootView.findViewById(R.id.tv_note);
        et_germplasm = (EditText) rootView.findViewById(R.id.et_germplasm);
        tv_select_cross = (EditText) rootView.findViewById(R.id.tv_select_cross);
        et_lot = (EditText) rootView.findViewById(R.id.et_lot);
        et_stock = (EditText) rootView.findViewById(R.id.et_stock);
        et_balance = (EditText) rootView.findViewById(R.id.et_balance);
        et_room = (EditText) rootView.findViewById(R.id.et_room);
        et_shelf = (EditText) rootView.findViewById(R.id.et_shelf);
        et_row = (EditText) rootView.findViewById(R.id.et_row);
        et_box = (EditText) rootView.findViewById(R.id.et_box);
        et_note = (EditText) rootView.findViewById(R.id.et_note);

        btn_add.setTypeface(montserrat_bold);
        btn_reset.setTypeface(montserrat_bold);
        tv_germplasm.setTypeface(montserrat_bold);
        tv_cross.setTypeface(montserrat_bold);
        tv_source.setTypeface(montserrat_bold);
        tv_select_source.setTypeface(montserrat_regular);
        tv_lot.setTypeface(montserrat_bold);
        tv_location.setTypeface(montserrat_bold);
        tv_select_location.setTypeface(montserrat_regular);
        tv_stock.setTypeface(montserrat_bold);
        tv_balance.setTypeface(montserrat_bold);
        tv_room.setTypeface(montserrat_bold);
        tv_shelf.setTypeface(montserrat_bold);
        tv_row.setTypeface(montserrat_bold);
        tv_box.setTypeface(montserrat_bold);
        tv_note.setTypeface(montserrat_bold);
        et_germplasm.setTypeface(montserrat_regular);
        tv_select_cross.setTypeface(montserrat_regular);
        et_lot.setTypeface(montserrat_regular);
        et_stock.setTypeface(montserrat_regular);
        et_balance.setTypeface(montserrat_regular);
        et_room.setTypeface(montserrat_regular);
        et_shelf.setTypeface(montserrat_regular);
        et_row.setTypeface(montserrat_regular);
        et_box.setTypeface(montserrat_regular);
        et_note.setTypeface(montserrat_regular);

        feedCrosses = new ArrayList<>();
        feedLocation = new ArrayList<>();
        feedSources = new ArrayList<>();

        tv_select_cross.setKeyListener(null);
        tv_select_location.setKeyListener(null);
        tv_select_source.setKeyListener(null);

        btn_add.setOnClickListener(btn_add_click);
        btn_reset.setOnClickListener(btn_reset_click);
        tv_select_location.setOnClickListener(tv_select_location_click);
        tv_select_source.setOnClickListener(tv_select_source_click);
        tv_select_cross.setOnClickListener(tv_select_cross_click);
        tv_select_cross.setOnFocusChangeListener(tv_select_cross_focus);
        tv_select_source.setOnFocusChangeListener(tv_select_source_focus);
        tv_select_location.setOnFocusChangeListener(tv_select_location_focus);
        new LoadingFireBase().execute("");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (select_source_click != null)
            mRootRef.removeEventListener(select_source_click);
        if (select_location_click != null)
            mRootRef.removeEventListener(select_location_click);
        if (select_cross_click != null)
            mRootRef.removeEventListener(select_cross_click);
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

            if (tv_select_location.getText().toString().equals("Tap to select") || tv_select_source.getText().toString().equals("Tap to select") ||
                    et_germplasm.getText().toString().equals("") || tv_select_cross.getText().toString().equals("Tap to select") ||
                    et_lot.getText().toString().equals("") || et_stock.getText().toString().equals("") ||
                    et_balance.getText().toString().equals("") || et_room.getText().toString().equals("") ||
                    et_shelf.getText().toString().equals("") || et_row.getText().toString().equals("") ||
                    et_box.getText().toString().equals("") || et_note.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Please fill in all information.", Toast.LENGTH_SHORT).show();
            } else {
                String key = mGermplasmRef.push().getKey();
                HashMap<String, Object> germplasm = new HashMap<String, Object>();
                germplasm.put("g_name", et_germplasm.getText().toString());
                germplasm.put("g_cross", tv_select_cross.getText().toString());
                germplasm.put("g_source", tv_select_source.getText().toString());
                germplasm.put("g_lot", Integer.parseInt(et_lot.getText().toString()));
                germplasm.put("g_location", tv_select_location.getText().toString());
                germplasm.put("g_stock", et_stock.getText().toString());
                germplasm.put("g_balance", Float.parseFloat(et_balance.getText().toString()));
                germplasm.put("g_room", Integer.parseInt(et_room.getText().toString()));
                germplasm.put("g_shelf", Integer.parseInt(et_shelf.getText().toString()));
                germplasm.put("g_row", Integer.parseInt(et_row.getText().toString()));
                germplasm.put("g_box", Integer.parseInt(et_box.getText().toString()));
                germplasm.put("g_note", et_note.getText().toString());
                germplasm.put("g_key", key);
                Map<String, Object> child = new HashMap<>();
                child.put(key, germplasm);
                mGermplasmRef.updateChildren(child);
                Toast.makeText(getContext(), "Add germplasm successfully.", Toast.LENGTH_SHORT).show();
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
        et_germplasm.setText("");
        et_lot.setText("");
        et_stock.setText("");
        et_balance.setText("");
        et_room.setText("");
        et_shelf.setText("");
        et_row.setText("");
        et_box.setText("");
        et_note.setText("");
        tv_select_cross.setText("Tap to select");
        tv_select_location.setText("Tap to select");
        tv_select_source.setText("Tap to select");
        tv_select_cross.setTextColor(getResources().getColor(R.color.soft_gray));
        tv_select_location.setTextColor(getResources().getColor(R.color.soft_gray));
        tv_select_source.setTextColor(getResources().getColor(R.color.soft_gray));
        location = -1;
        source = -1;
        cross = -1;
    }

    View.OnFocusChangeListener tv_select_cross_focus = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
                builder
                        .title("Select Cross")
                        .items(crossList)
                        .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
                        .itemsCallbackSingleChoice(cross, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                tv_select_cross.setText(text + "");
                                tv_select_cross.setTextColor(getResources().getColor(R.color.light_blue));
                                cross = which;
                                return true;
                            }
                        })
                        .negativeText("cancel")
                        .show();
            }
        }
    };

    View.OnClickListener tv_select_cross_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
            builder
                    .title("Select Cross")
                    .items(crossList)
                    .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
                    .itemsCallbackSingleChoice(cross, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            tv_select_cross.setText(text + "");
                            tv_select_cross.setTextColor(getResources().getColor(R.color.light_blue));
                            cross = which;
                            return true;
                        }
                    })
                    .negativeText("cancel")
                    .show();
        }
    };

    View.OnFocusChangeListener tv_select_location_focus = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
                builder
                        .title("Select Source")
                        .items(locationList)
                        .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
                        .itemsCallbackSingleChoice(location, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                tv_select_location.setText(text + "");
                                tv_select_location.setTextColor(getResources().getColor(R.color.light_blue));
                                location = which;
                                return true;
                            }
                        })
                        .negativeText("cancel")
                        .show();
            }
        }
    };

    View.OnClickListener tv_select_location_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
            builder
                    .title("Select Source")
                    .items(locationList)
                    .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
                    .itemsCallbackSingleChoice(location, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            tv_select_location.setText(text + "");
                            tv_select_location.setTextColor(getResources().getColor(R.color.light_blue));
                            location = which;
                            return true;
                        }
                    })
                    .negativeText("cancel")
                    .show();
        }
    };

    View.OnFocusChangeListener tv_select_source_focus = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
                builder
                        .title("Select Source")
                        .items(sourceList)
                        .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
                        .itemsCallbackSingleChoice(source, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                tv_select_source.setText(text + "");
                                tv_select_source.setTextColor(getResources().getColor(R.color.light_blue));
                                source = which;
                                return true;
                            }
                        })
                        .negativeText("cancel")
                        .show();
            }
        }
    };

    View.OnClickListener tv_select_source_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
            builder
                    .title("Select Source")
                    .items(sourceList)
                    .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
                    .itemsCallbackSingleChoice(source, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            tv_select_source.setText(text + "");
                            tv_select_source.setTextColor(getResources().getColor(R.color.light_blue));
                            source = which;
                            return true;
                        }
                    })
                    .negativeText("cancel")
                    .show();
        }
    };

    ChildEventListener select_cross_click = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            FeedCross model = dataSnapshot.getValue(FeedCross.class);
            feedCrosses.add(model);
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
    };

    ChildEventListener select_location_click = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            FeedLocation model = dataSnapshot.getValue(FeedLocation.class);
            feedLocation.add(model);
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
    };

    ChildEventListener select_source_click = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            FeedSource model = dataSnapshot.getValue(FeedSource.class);
            feedSources.add(model);
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
    };

    private class LoadingFireBase extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            locationList = new String[feedLocation.size()];
            sourceList = new String[feedSources.size()];
            crossList = new String[feedCrosses.size()];
            for (int i = 0; i < feedLocation.size(); i++) {
                locationList[i] = feedLocation.get(i).getL_name() + "";
            }
            for (int i = 0; i < feedSources.size(); i++) {
                sourceList[i] = feedSources.get(i).getS_name() + "";
            }
            for (int i = 0; i < feedCrosses.size(); i++) {
                crossList[i] = feedCrosses.get(i).getC_name() + "";
            }
        }
    }
}
