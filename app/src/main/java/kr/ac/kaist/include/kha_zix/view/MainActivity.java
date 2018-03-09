package kr.ac.kaist.include.kha_zix.view;

import android.app.Activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import kr.ac.kaist.include.kha_zix.R;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private ImageButton mDrawerBtn;
    private ImageButton mSearchBtn;
    public TextView mTitleTv;
    /**
     *
     */

    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.khazix_activity);


        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(128, 255, 255, 255)));
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.khazix_activity_actionbar);

        View actionBarView = actionBar.getCustomView();
        mDrawerBtn = (ImageButton) actionBarView.findViewById(R.id.drawer_btn);
        mDrawerBtn.setOnClickListener(onClickListener);
        mSearchBtn = (ImageButton) actionBarView.findViewById(R.id.search_btn);
        mSearchBtn.setOnClickListener(onClickListener);
        mTitleTv = (TextView) actionBarView.findViewById(R.id.title_tv);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    /**
     * Btn OnClickListener
     */

    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            clickListenerHandler(v.getId());
        }
    };

    private void clickListenerHandler(int id) {
        switch (id) {
            case R.id.drawer_btn:
                mNavigationDrawerFragment.toggleDrawer();
                break;
            case R.id.search_btn:
                Toast.makeText(this, "아직 준비 중인 기능입니다", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    ;

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        if (position == 0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, MainRecommendationFragment.newInstance(position + 1))
                    .commit();
        } else {

            fragmentManager.beginTransaction()
                    .replace(R.id.container, MainProfileFragment.newInstance(null, null))
                    .commit();
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        mTitleTv.setText(mTitle);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.khazix, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
    public void startDetailActivity(String code, String name) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("code", code);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    /**
     * 뒤로가기 두번
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "한 번 더 누르시면, \"" + getResources().getString(R.string.app_name) + "\"에서 빠져나갑니다.", Toast.LENGTH_SHORT)
                .show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);
    }


}
