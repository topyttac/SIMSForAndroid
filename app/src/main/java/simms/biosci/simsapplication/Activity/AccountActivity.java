package simms.biosci.simsapplication.Activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import simms.biosci.simsapplication.Manager.TypefaceSpan;
import simms.biosci.simsapplication.R;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private Typeface montserrat_regular, montserrat_bold;
    private View positiveAction;
    private String password;
    @BindView(R.id.ll_change_password)
    LinearLayout ll_change_password;
    @BindView(R.id.tv_change_password)
    TextView tv_change_password;
    @BindView(R.id.tv_change_password_detail)
    TextView tv_change_password_detail;
    @BindView(R.id.tv_account)
    TextView tv_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        montserrat_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        montserrat_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");

        tv_account.setTypeface(montserrat_bold);
        tv_change_password.setTypeface(montserrat_bold);
        tv_change_password_detail.setTypeface(montserrat_regular);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };

        SpannableString s = new SpannableString("Settings");
        s.setSpan(new TypefaceSpan(AccountActivity.this, "Montserrat-SemiBold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ll_change_password.setOnClickListener(ll_change_password_click);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener ll_change_password_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final TextInputLayout tv_new_password, tv_re_new_password;
            final EditText et_new_password, et_re_new_password;
            final CheckBox cb_show_password;
            MaterialDialog dialog =
                    new MaterialDialog.Builder(AccountActivity.this)
                            .title(R.string.change_password)
                            .typeface("Montserrat-Regular.ttf", "Montserrat-Regular.ttf")
                            .customView(R.layout.custom_view_change_password, true)
                            .positiveText(R.string.confirm)
                            .negativeText(R.string.cencel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    final ProgressDialog progressDialog = new ProgressDialog(AccountActivity.this,
                                            R.style.AppTheme_Dark_Dialog);
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setMessage("Changing password...");
                                    progressDialog.show();

                                    user.updatePassword(password)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        new android.os.Handler().postDelayed(
                                                                new Runnable() {
                                                                    public void run() {
                                                                        // On complete call either onLoginSuccess or onLoginFailed
                                                                        progressDialog.dismiss();
                                                                    }
                                                                }, 1000);
                                                        showToast("Reset Password Successful");
                                                    } else {
                                                        new android.os.Handler().postDelayed(
                                                                new Runnable() {
                                                                    public void run() {
                                                                        // On complete call either onLoginSuccess or onLoginFailed
                                                                        progressDialog.dismiss();
                                                                    }
                                                                }, 1000);
                                                        showToast("Reset Password Failed");
                                                    }
                                                }
                                            });
                                }

                            })
                            .build();

            positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
            //noinspection ConstantConditions
            tv_new_password = (TextInputLayout) dialog.getCustomView().findViewById(R.id.tv_new_password);
            tv_new_password.setTypeface(montserrat_bold);
            tv_re_new_password = (TextInputLayout) dialog.getCustomView().findViewById(R.id.tv_re_new_password);
            tv_re_new_password.setTypeface(montserrat_bold);
            et_new_password = (EditText) dialog.getCustomView().findViewById(R.id.et_new_password);
            et_new_password.setTypeface(montserrat_regular);
            et_re_new_password = (EditText) dialog.getCustomView().findViewById(R.id.et_re_new_password);
            et_re_new_password.setTypeface(montserrat_regular);
            cb_show_password = (CheckBox) dialog.getCustomView().findViewById(R.id.cb_show_password);
            cb_show_password.setTypeface(montserrat_regular);

            et_new_password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (et_new_password.getText().toString().length() >= 4 && et_re_new_password.getText().toString().length() >= 4 && et_new_password.getText().toString().equals(et_re_new_password.getText().toString())) {
                        positiveAction.setEnabled(true);
                        password = et_new_password.getText().toString();
                    } else {
                        positiveAction.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            et_re_new_password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (et_new_password.getText().toString().length() >= 4 && et_re_new_password.getText().toString().length() >= 4 && et_new_password.getText().toString().equals(et_re_new_password.getText().toString())) {
                        positiveAction.setEnabled(true);
                        password = et_re_new_password.getText().toString();
                    } else {
                        positiveAction.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            // Toggling the show password CheckBox will mask or unmask the password input EditText

            cb_show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    et_new_password.setInputType(
                            !isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                    et_new_password.setTypeface(montserrat_regular);
                    et_new_password.setTransformationMethod(
                            !isChecked ? PasswordTransformationMethod.getInstance() : null);
                    et_re_new_password.setInputType(
                            !isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                    et_re_new_password.setTypeface(montserrat_regular);
                    et_re_new_password.setTransformationMethod(
                            !isChecked ? PasswordTransformationMethod.getInstance() : null);
                }
            });

            dialog.show();
            positiveAction.setEnabled(false); // disabled by default
        }
    };

    private void showToast(String text) {
        Toast.makeText(AccountActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
