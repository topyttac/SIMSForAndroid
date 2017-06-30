package simms.biosci.simsapplication.Fragment;

import android.graphics.Typeface;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import simms.biosci.simsapplication.Manager.SingletonSIMS;
import simms.biosci.simsapplication.R;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class AddCrossFragment extends Fragment {

    private SingletonSIMS sims;
    private Typeface montserrat_regular, montserrat_bold;
    private TextInputLayout tv_cross_name, tv_cross_desc;
    private Button btn_add, btn_reset;
    private EditText et_cross_name, et_cross_desc;
    private DatabaseReference mRootRef, mCrossRef;

    public AddCrossFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static AddCrossFragment newInstance() {
        AddCrossFragment fragment = new AddCrossFragment();
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
        mCrossRef = mRootRef.child(sims.getUser()).child("cross");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_cross, container, false);
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
        tv_cross_name = (TextInputLayout) rootView.findViewById(R.id.tv_cross_name);
        et_cross_name = (EditText) rootView.findViewById(R.id.et_cross_name);
        tv_cross_desc = (TextInputLayout) rootView.findViewById(R.id.tv_cross_desc);
        et_cross_desc = (EditText) rootView.findViewById(R.id.et_cross_desc);

        btn_add.setTypeface(montserrat_bold);
        btn_reset.setTypeface(montserrat_bold);
        tv_cross_name.setTypeface(montserrat_bold);
        et_cross_name.setTypeface(montserrat_regular);
        tv_cross_desc.setTypeface(montserrat_bold);
        et_cross_desc.setTypeface(montserrat_regular);

        btn_add.setOnClickListener(btn_add_click);
        btn_reset.setOnClickListener(btn_reset_click);
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

            if (et_cross_name.getText().toString().equals("") || et_cross_desc.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Please fill in all information.", Toast.LENGTH_SHORT).show();
            } else {
                String key = mCrossRef.push().getKey();
                HashMap<String, Object> cross = new HashMap<String, Object>();
                cross.put("c_name", et_cross_name.getText().toString());
                cross.put("c_desc", et_cross_desc.getText().toString());
                cross.put("c_key", key);
                Map<String, Object> child = new HashMap<>();
                child.put(key, cross);
                mCrossRef.updateChildren(child);
                Toast.makeText(getContext(), "Add source successfully.", Toast.LENGTH_SHORT).show();
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
        et_cross_name.setText("");
        et_cross_desc.setText("");
    }


}
