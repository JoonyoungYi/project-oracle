package kr.ac.kaist.include.kha_zix.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import kr.ac.kaist.include.kha_zix.R;


public class DetailActivity extends Activity {

    TextView mTitleTv;
    ImageButton mBackBtn;

    public String code = "-";
    public String name = "-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        code = bundle.getString("code", "-");
        name = bundle.getString("name", "-");
        setContentView(R.layout.detail_activity);


        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(128, 255, 255, 255)));

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.detail_activity_actionbar);


        View actionBarView = actionBar.getCustomView();
        mTitleTv = (TextView) actionBarView.findViewById(R.id.title_tv);
        mTitleTv.setText(code);
        mBackBtn = (ImageButton) actionBarView.findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(onClickListener);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
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
            case R.id.back_btn:
                this.onBackPressed();
                break;
            default:
                break;
        }
    }

    ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
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

    public void startDetailActivity(String code) {
        Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
        intent.putExtra("code", code);
        startActivity(intent);

    }

}
