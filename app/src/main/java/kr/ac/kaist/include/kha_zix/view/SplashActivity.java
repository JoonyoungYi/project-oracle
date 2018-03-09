package kr.ac.kaist.include.kha_zix.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import kr.ac.kaist.include.kha_zix.R;
import kr.ac.kaist.include.kha_zix.api.ApiBase;
import kr.ac.kaist.include.kha_zix.utils.Argument;

public class SplashActivity extends Activity {
    private static final String TAG = "Splash Activity";

    /**
     *
     */

    SplashApiTask splashApiTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        splashApiTask = new SplashApiTask();
        splashApiTask.execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    public class SplashApiTask extends AsyncTask<Void, Void, Void> {
        private int request_code = Argument.REQUEST_CODE_UNEXPECTED;

        private String response = "";


        /**
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {

            String uid = ApiBase.getStringInPrefs(getApplication(), Argument.PREFS_UID, null);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (uid == null) {
                request_code = Argument.REQUEST_CODE_FAIL;
            } else {
                request_code = Argument.REQUEST_CODE_SUCCESS;
            }

            Log.d(TAG, "mills" + System.currentTimeMillis());

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            splashApiTask = null;

            if (request_code == Argument.REQUEST_CODE_SUCCESS) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);

            } else if (request_code == Argument.REQUEST_CODE_FAIL) {

                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);


            }

            finish();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            splashApiTask = null;
        }
    }

    @Override
    public void onDestroy() {
        if (splashApiTask != null) {
            splashApiTask.cancel(true);
        }

        super.onDestroy();
    }
}
