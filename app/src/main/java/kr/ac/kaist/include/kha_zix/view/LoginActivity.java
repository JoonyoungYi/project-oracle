package kr.ac.kaist.include.kha_zix.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import kr.ac.kaist.include.kha_zix.R;
import kr.ac.kaist.include.kha_zix.api.ApiBase;
import kr.ac.kaist.include.kha_zix.api.LoginApi;
import kr.ac.kaist.include.kha_zix.utils.Argument;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends FragmentActivity {
    private static final String TAG = "Login Activity";

    /**
     *
     */
    private String mUsername = "";
    private String mPassword = "";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private LoginApiTask mLoginApiTask = null;

    /**
     * Preference Auto Login
     */

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefs_editor;

    /**
     * UI references.
     */

    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private View mLoginFormView;
    private View mLoginStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        /**
         * Set up the login form.
         */

        mUsernameEt = (EditText) findViewById(R.id.username_et);
        mPasswordEt = (EditText) findViewById(R.id.password_et);

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLoginApi();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void requestLoginApi() {
        if (mLoginApiTask != null) {
            return;
        }

        // Reset errors.
        mUsernameEt.setError(null);

        // Store values at the time of the login attempt.
        mUsername = mUsernameEt.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(mUsername)) {
            mUsernameEt.setError(getString(R.string.error_field_required));
            focusView = mUsernameEt;
            cancel = true;
        }

        mPassword = mPasswordEt.getText().toString();
        // Check for a valid email address.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordEt.setError(getString(R.string.error_field_required));
            focusView = mPasswordEt;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mLoginApiTask = new LoginApiTask();
            mLoginApiTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     *
     */
    public class LoginApiTask extends AsyncTask<Void, Void, Void> {
        private int request_code = Argument.REQUEST_CODE_UNEXPECTED;

        private String response = "";


        /**
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {

            try {
                LoginApi loginApi = new LoginApi(getApplication(), mUsername, mPassword);
                loginApi.getResult();
                request_code = loginApi.getRequestCode();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            mLoginApiTask = null;
            ApiBase.showToastMsg(getApplication(), request_code);


            if (request_code == Argument.REQUEST_CODE_SUCCESS) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);

            } else if (request_code == Argument.REQUEST_CODE_FAIL) {
                /*
                Intent intent = null;
                long currentTime = System.currentTimeMillis();
                Log.d(TAG, "currentTime: " + currentTime);
                long requestTime = Long.parseLong(ApiBase.getStringInPrefs(getApplication(), Argument.PREFS_LOGIN_AUTH_API_REQUEST_TIME, "0"));
                Log.d(TAG, "requestTime: " + requestTime);
                if (currentTime - requestTime < (long) (300000)) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);

                } else {
                    intent = new Intent(SplashActivity.this, LoginAuthActivity.class);
                }

                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);*/
            }

            finish();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mLoginApiTask = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mLoginApiTask != null) {
            mLoginApiTask.cancel(true);
        }

    }
}