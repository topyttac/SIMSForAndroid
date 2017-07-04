package simms.biosci.simsapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import simms.biosci.simsapplication.Manager.SingletonSIMS;
import simms.biosci.simsapplication.R;

public class LoginActivity extends AppCompatActivity {

    private SingletonSIMS sims;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private Typeface montserrat_regular, montserrat_bold;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.tv_email)
    TextInputLayout tv_email;
    @BindView(R.id.tv_password)
    TextInputLayout tv_password;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_forgot_password)
    TextView tv_forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        sims = SingletonSIMS.getInstance();

        montserrat_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        montserrat_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");

        _emailText.setTypeface(montserrat_regular);
        _passwordText.setTypeface(montserrat_regular);
        _loginButton.setTypeface(montserrat_bold);
        _signupLink.setTypeface(montserrat_regular);
        tv_email.setTypeface(montserrat_bold);
        tv_password.setTypeface(montserrat_bold);
        tv_title.setTypeface(montserrat_bold);
        tv_forgot_password.setTypeface(montserrat_regular);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    email = email.replace(".", "");
                    email = email.replace(".", "");
                    email = email.replace(".", "");
                    email = email.replace("#", "");
                    email = email.replace("$", "");
                    email = email.replace("[", "");
                    email = email.replace("]", "");
                    sims.setUser(email);
                }
            }
        };

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            // On complete call either onLoginSuccess or onLoginFailed
                                            onLoginFailed();
                                            progressDialog.dismiss();
                                        }
                                    }, 2000);
                        } else {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            // On complete call either onLoginSuccess or onLoginFailed
                                            onLoginSuccess();
                                            progressDialog.dismiss();
                                        }
                                    }, 2000);
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically

            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Invalid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            _passwordText.setError("Password must more than 4 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
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