package simms.biosci.simsapplication.Fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import simms.biosci.simsapplication.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class SettingFragment extends Fragment {

    private Typeface montserrat_regular, montserrat_bold;
    private TextView tv_general, tv_display_type, tv_display_result, tv_germplasm_column, tv_germplasm_result;
    private LinearLayout ll_display_type, ll_germplasm_column;
    private SharedPreferences.Editor display_write, germplasm_one_write, germplasm_two_write, first_time_write;
    private SharedPreferences display_read, germplasm_one_read, germplasm_two_read, first_time_read;
    private Boolean firstTime, card_view_type;
    private int germplasm_which_one, germplasm_which_two;

    public SettingFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
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

        ll_display_type = (LinearLayout) rootView.findViewById(R.id.ll_display_type);
        ll_germplasm_column = (LinearLayout) rootView.findViewById(R.id.ll_germplasm_column);
        tv_general = (TextView) rootView.findViewById(R.id.tv_general);
        tv_display_type = (TextView) rootView.findViewById(R.id.tv_display_type);
        tv_display_result = (TextView) rootView.findViewById(R.id.tv_display_result);
        tv_germplasm_column = (TextView) rootView.findViewById(R.id.tv_germplasm_column);
        tv_germplasm_result = (TextView) rootView.findViewById(R.id.tv_germplasm_result);

        tv_general.setTypeface(montserrat_bold);
        tv_display_type.setTypeface(montserrat_bold);
        tv_display_result.setTypeface(montserrat_regular);
        tv_germplasm_column.setTypeface(montserrat_bold);
        tv_germplasm_result.setTypeface(montserrat_regular);

        ll_display_type.setOnClickListener(ll_display_type_click);
        ll_germplasm_column.setOnClickListener(ll_germplasm_column_click);

        sharedPreferences();
    }

    private void sharedPreferences() {
        display_write = getActivity().getSharedPreferences("card_view_type", MODE_PRIVATE).edit();
        display_read = getActivity().getSharedPreferences("card_view_type", MODE_PRIVATE);
        card_view_type = display_read.getBoolean("card_view_type", true);
        if (card_view_type) {
            tv_display_result.setText("Show Results in Card View");
            ll_germplasm_column.setClickable(false);
            tv_germplasm_column.setTextColor(Color.parseColor("#BDBDBD"));
            tv_germplasm_result.setTextColor(Color.parseColor("#BDBDBD"));
        } else {
            tv_display_result.setText("Show Results in Table View");
            ll_germplasm_column.setClickable(true);
            tv_germplasm_column.setTextColor(Color.parseColor("#000000"));
            tv_germplasm_result.setTextColor(Color.parseColor("#757575"));
        }

        germplasm_one_write = getActivity().getSharedPreferences("germplasm_which_one", MODE_PRIVATE).edit();
        germplasm_one_read = getActivity().getSharedPreferences("germplasm_which_one", MODE_PRIVATE);
        germplasm_which_one = germplasm_one_read.getInt("germplasm_which_one", 2);

        germplasm_two_write = getActivity().getSharedPreferences("germplasm_which_two", MODE_PRIVATE).edit();
        germplasm_two_read = getActivity().getSharedPreferences("germplasm_which_two", MODE_PRIVATE);
        germplasm_which_two = germplasm_two_read.getInt("germplasm_which_two", 4);

        String[] germplasm_columns = getResources().getStringArray(R.array.germplasm_column);
        first_time_write = getActivity().getSharedPreferences("first_time", MODE_PRIVATE).edit();
        first_time_read = getActivity().getSharedPreferences("first_time", MODE_PRIVATE);
        firstTime = first_time_read.getBoolean("first_time", true);
        if (firstTime) {
            tv_germplasm_result.setText(germplasm_columns[0] + ", " + germplasm_columns[2] + ", " + germplasm_columns[4]);
        } else {
            tv_germplasm_result.setText(germplasm_columns[0] + ", " + germplasm_columns[germplasm_which_one] + ", " + germplasm_columns[germplasm_which_two]);
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

    View.OnClickListener ll_display_type_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            display_read = getActivity().getSharedPreferences("card_view_type", MODE_PRIVATE);
            card_view_type = display_read.getBoolean("card_view_type", true);
            MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
            builder
                    .title("Select Display Type")
                    .items("Card View", "Table View")
                    .typeface("Montserrat-Regular.ttf", "Prompt-Regular.ttf")
                    .itemsCallbackSingleChoice(card_view_type ? 0 : 1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            tv_display_result.setText("Show Results in " + text);
                            if (which == 0) {
                                display_write.putBoolean("card_view_type", true);
                                display_write.commit();
                                ll_germplasm_column.setClickable(false);
                                tv_germplasm_column.setTextColor(Color.parseColor("#BDBDBD"));
                                tv_germplasm_result.setTextColor(Color.parseColor("#BDBDBD"));
                            } else {
                                display_write.putBoolean("card_view_type", false);
                                display_write.commit();
                                ll_germplasm_column.setClickable(true);
                                tv_germplasm_column.setTextColor(Color.parseColor("#000000"));
                                tv_germplasm_result.setTextColor(Color.parseColor("#757575"));
                            }
                            return true;
                        }
                    })
                    .negativeText("cancel")
                    .show();
        }
    };

    View.OnClickListener ll_germplasm_column_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            first_time_read = getActivity().getSharedPreferences("first_time", MODE_PRIVATE);
            firstTime = first_time_read.getBoolean("first_time", true);

            germplasm_one_read = getActivity().getSharedPreferences("germplasm_which_one", MODE_PRIVATE);
            germplasm_which_one = germplasm_one_read.getInt("germplasm_which_one", 2);

            germplasm_two_read = getActivity().getSharedPreferences("germplasm_which_two", MODE_PRIVATE);
            germplasm_which_two = germplasm_two_read.getInt("germplasm_which_two", 4);
            new MaterialDialog.Builder(getContext())
                    .title(R.string.select_germplasm_column)
                    .typeface("Montserrat-Regular.ttf", "Prompt-Regular.ttf")
                    .items(R.array.germplasm_column)
                    .alwaysCallMultiChoiceCallback()
                    .itemsDisabledIndices(0)
                    .positiveText(R.string.confirm)
                    .itemsCallbackMultiChoice(firstTime ? new Integer[]{0, 2, 4} : new Integer[]{0, germplasm_which_one, germplasm_which_two}, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            boolean allowSelectionChange = which.length <= 3;
                            if (!allowSelectionChange) {
                                showToast(getString(R.string.germplasm_column_caution));
                            }
                            for (int i = 0; i < text.length; i++) {
                                if (i == 1) {
                                    germplasm_one_write.putInt("germplasm_which_one", Integer.valueOf(which[i]));
                                    germplasm_one_write.commit();
                                } else if (i == 2) {
                                    germplasm_two_write.putInt("germplasm_which_two", Integer.valueOf(which[i]));
                                    germplasm_two_write.commit();
                                }
                            }

                            first_time_write.putBoolean("first_time", false);
                            first_time_write.commit();

                            germplasm_one_read = getActivity().getSharedPreferences("germplasm_which_one", MODE_PRIVATE);
                            germplasm_which_one = germplasm_one_read.getInt("germplasm_which_one", 2);

                            germplasm_two_read = getActivity().getSharedPreferences("germplasm_which_two", MODE_PRIVATE);
                            germplasm_which_two = germplasm_two_read.getInt("germplasm_which_two", 4);

                            String[] germplasm_columns = getResources().getStringArray(R.array.germplasm_column);
                            tv_germplasm_result.setText(germplasm_columns[0] + ", " + germplasm_columns[germplasm_which_one] + ", " + germplasm_columns[germplasm_which_two]);
                            return allowSelectionChange;
                        }
                    })
                    .show();
        }
    };

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}