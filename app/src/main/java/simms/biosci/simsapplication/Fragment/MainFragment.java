package simms.biosci.simsapplication.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.spark.submitbutton.SubmitButton;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import simms.biosci.simsapplication.Manager.SingletonSIMS;
import simms.biosci.simsapplication.R;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class MainFragment extends Fragment {

    private RadioRealButtonGroup rb_0;
    private RadioRealButton rb_1, rb_2, rb_3;
    private SubmitButton btn_sensor, btn_camera;
    private TextView tv_title, tv_search;
    private EditText et_search;
    private Typeface montserrat_regular, montserrat_bold;
    private FrameLayout fl_search;
    private SingletonSIMS singletonSIMS;

    public MainFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        singletonSIMS = SingletonSIMS.getInstance();
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        montserrat_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.ttf");
        montserrat_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-SemiBold.ttf");
        rb_0 = (RadioRealButtonGroup) rootView.findViewById(R.id.rb_0);
        rb_1 = (RadioRealButton) rootView.findViewById(R.id.rb_1);
        rb_2 = (RadioRealButton) rootView.findViewById(R.id.rb_2);
        rb_3 = (RadioRealButton) rootView.findViewById(R.id.rb_3);
        btn_sensor = (SubmitButton) rootView.findViewById(R.id.btn_sensor);
        btn_camera = (SubmitButton) rootView.findViewById(R.id.btn_camera);
        tv_search = (TextView) rootView.findViewById(R.id.tv_search);
        et_search = (EditText) rootView.findViewById(R.id.et_search);
        fl_search = (FrameLayout) rootView.findViewById(R.id.fl_search);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);

        tv_title.setTypeface(montserrat_bold);
        btn_sensor.setTypeface(montserrat_regular);
        btn_camera.setTypeface(montserrat_regular);
        tv_search.setTypeface(montserrat_regular);
        et_search.setTypeface(montserrat_regular);

        btn_camera.setVisibility(View.INVISIBLE);
        btn_sensor.setVisibility(View.INVISIBLE);

        rb_0.setOnClickedButtonListener(radio_group_click);
        et_search.setOnEditorActionListener(et_search_click);
        tv_search.setOnClickListener(tv_search_click);
        tv_search.setText(Html.fromHtml("<u>By Name</u>:"));
        if(singletonSIMS.getSelectedType() > 0)
            tv_search.setText(Html.fromHtml("<u>" + singletonSIMS.getSelectedTypeName() + "</u>:"));

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

    RadioRealButtonGroup.OnClickedButtonListener radio_group_click = new RadioRealButtonGroup.OnClickedButtonListener() {
        @Override
        public void onClickedButton(RadioRealButton button, int position) {
            switch (position) {
                case 0:
                    fl_search.setVisibility(View.INVISIBLE);
                    btn_sensor.setVisibility(View.INVISIBLE);
                    btn_camera.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    btn_camera.setVisibility(View.INVISIBLE);
                    btn_sensor.setVisibility(View.INVISIBLE);
                    fl_search.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    fl_search.setVisibility(View.INVISIBLE);
                    btn_camera.setVisibility(View.INVISIBLE);
                    btn_sensor.setVisibility(View.VISIBLE);
            }
        }
    };

    TextView.OnEditorActionListener et_search_click = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            return false;
        }
    };

    View.OnClickListener tv_search_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            tv_search.startAnimation(anim);
            MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
            builder
                    .title("Select Search Type")
                    .items("By Name", "By Germplasm", "By Location", "By Source")
                    .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
                    .itemsCallbackSingleChoice(singletonSIMS.getSelectedType() > 0 ? singletonSIMS.getSelectedType() : 0, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            tv_search.setText(Html.fromHtml("<u>" + text + "</u>:"));
                            singletonSIMS.setSelectedType(which);
                            singletonSIMS.setSelectedTypeName(text + ":");
                            if(which == 0){
                                et_search.setHint("rice, corn - e.g.");
                                et_search.setInputType(InputType.TYPE_CLASS_TEXT);
                                et_search.setText("");
                            } else if (which == 1){
                                et_search.setHint("20, 130 - e.g.");
                                et_search.setInputType(InputType.TYPE_CLASS_NUMBER);
                                et_search.setText("");
                            } else if (which == 2){
                                et_search.setHint("Thamasat, Biosci - e.g.");
                                et_search.setInputType(InputType.TYPE_CLASS_TEXT);
                                et_search.setText("");
                            } else if (which == 3){
                                et_search.setHint("Nusery 1, Nusery 2 - e.g.");
                                et_search.setInputType(InputType.TYPE_CLASS_TEXT);
                                et_search.setText("");
                            }
                            return true;
                        }
                    })
                    .negativeText("cancel")
                    .show();
        }
    };
}
