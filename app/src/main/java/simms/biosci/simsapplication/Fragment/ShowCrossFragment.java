package simms.biosci.simsapplication.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import simms.biosci.simsapplication.Manager.FeedCross;
import simms.biosci.simsapplication.Manager.FeedGermplasm;
import simms.biosci.simsapplication.R;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class ShowCrossFragment extends Fragment {

    private Typeface montserrat_regular, montserrat_bold;
    private TextView tv_title, tv_cross_name, tv_cross_desc;
    private Button btn_update, btn_delete;
    private EditText et_cross_name, et_cross_desc;
    private DatabaseReference mRootRef;
    private String key, crossName;
    private static final int REQUEST_CODE_SHOW = 8;

    public ShowCrossFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static ShowCrossFragment newInstance(String key) {
        ShowCrossFragment fragment = new ShowCrossFragment();
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

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("cross").orderByChild("c_key").equalTo(key).addChildEventListener(show_cross_click);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_cross, container, false);
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

        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        btn_update = (Button) rootView.findViewById(R.id.btn_update);
        btn_delete = (Button) rootView.findViewById(R.id.btn_delete);
        tv_cross_name = (TextView) rootView.findViewById(R.id.tv_cross_name);
        et_cross_name = (EditText) rootView.findViewById(R.id.et_cross_name);
        tv_cross_desc = (TextView) rootView.findViewById(R.id.tv_cross_desc);
        et_cross_desc = (EditText) rootView.findViewById(R.id.et_cross_desc);

        btn_update.setTypeface(montserrat_bold);
        btn_delete.setTypeface(montserrat_bold);
        tv_title.setTypeface(montserrat_bold);
        tv_cross_name.setTypeface(montserrat_bold);
        et_cross_name.setTypeface(montserrat_regular);
        tv_cross_desc.setTypeface(montserrat_bold);
        et_cross_desc.setTypeface(montserrat_regular);

        btn_update.setOnClickListener(btn_update_click);
        btn_delete.setOnClickListener(btn_delete_click);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (show_cross_click != null)
            mRootRef.removeEventListener(show_cross_click);
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

            if (et_cross_name.getText().toString().equals("") || et_cross_desc.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Please fill in all information.", Toast.LENGTH_SHORT).show();
            } else {
                new MaterialDialog.Builder(getContext())
                        .title("Are you want to update cross ?")
                        .content("This change will be affected to all germplasm.")
                        .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
                        .positiveText("YES")
                        .negativeText("NO")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                HashMap<String, Object> cross = new HashMap<String, Object>();
                                cross.put("c_name", et_cross_name.getText().toString());
                                cross.put("c_desc", et_cross_desc.getText().toString());
                                cross.put("c_key", key);
                                Map<String, Object> child = new HashMap<>();
                                child.put(key, cross);
                                mRootRef.child("cross").updateChildren(child);
                                mRootRef.child("germplasm").orderByChild("g_cross").equalTo(crossName).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        FeedGermplasm model = dataSnapshot.getValue(FeedGermplasm.class);
                                        mRootRef.child("germplasm").child(model.getG_key()).child("g_cross").setValue(et_cross_name.getText().toString());
//                                        HashMap<String, Object> germplasm = new HashMap<String, Object>();
//                                        germplasm.put("g_name", model.getG_name());
//                                        germplasm.put("g_cross", model.getG_cross());
//                                        germplasm.put("g_source", et_source_name.getText().toString());
//                                        germplasm.put("g_lot", model.getG_lot());
//                                        germplasm.put("g_location", model.getG_location());
//                                        germplasm.put("g_stock", model.getG_stock());
//                                        germplasm.put("g_balance", model.getG_balance());
//                                        germplasm.put("g_room", model.getG_room());
//                                        germplasm.put("g_shelf", model.getG_shelf());
//                                        germplasm.put("g_row", model.getG_row());
//                                        germplasm.put("g_box", model.getG_box());
//                                        germplasm.put("g_note", model.getG_note());
//                                        germplasm.put("g_key", model.getG_key());
//                                        Map<String, Object> child = new HashMap<>();
//                                        child.put(model.getG_key(), germplasm);
//                                        mGermplasmRef.updateChildren(child);
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
                                Toast.makeText(getContext(), "Update cross successfully.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra("KEY", key);
                                intent.putExtra("c_name", et_cross_name.getText().toString());
                                intent.putExtra("c_desc", et_cross_desc.getText().toString());
                                intent.putExtra("what2do", "update");
                                getActivity().setResult(REQUEST_CODE_SHOW, intent);
                                getActivity().finish();
                            }
                        })
                        .show();

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

            new MaterialDialog.Builder(getContext())
                    .title("Are you want to delete cross ?")
                    .content("This change will be affected to all germplasm.")
                    .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
                    .positiveText("YES")
                    .negativeText("NO")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mRootRef.child("cross").child(key).removeValue();
//                            mRootRef.child("germplasm").orderByChild("g_source").equalTo(sourceName).addChildEventListener(new ChildEventListener() {
//                                @Override
//                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                                    FeedGermplasm model = dataSnapshot.getValue(FeedGermplasm.class);
//                                    mRootRef.child("germplasm").child(model.getG_key()).removeValue();
//                                }
//
//                                @Override
//                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                                }
//
//                                @Override
//                                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                                }
//
//                                @Override
//                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
                            Toast.makeText(getContext(), "Delete cross successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("KEY", key);
                            intent.putExtra("what2do", "delete");
                            getActivity().setResult(REQUEST_CODE_SHOW, intent);
                            getActivity().finish();
                        }
                    })
                    .show();
        }
    };

    ChildEventListener show_cross_click = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            FeedCross model = dataSnapshot.getValue(FeedCross.class);
            et_cross_name.setText(model.getC_name() + "");
            et_cross_desc.setText(model.getC_desc() + "");
            crossName = model.getC_name() + "";
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
}
