package kr.ac.kaist.include.kha_zix.view;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

import kr.ac.kaist.include.kha_zix.R;
import kr.ac.kaist.include.kha_zix.api.ApiBase;
import kr.ac.kaist.include.kha_zix.api.CourseDetailApi;
import kr.ac.kaist.include.kha_zix.model.Course;
import kr.ac.kaist.include.kha_zix.utils.Argument;


public class DetailFragment extends Fragment {
    private final static String TAG = "Detail Fragment";

    /**
     * Data
     */
    String mCode = "";
    /**
     * UI init
     */
    private TextView mCodeTv;
    private TextView mNameTv;
    private TextView mGradeTv;

    private TextView[] mShareTvs = new TextView[4];
    private View[] mShareViews = new View[4];
    //
    private View[] mRelatedPriorViews = new View[3];
    private TextView[] mRelatedPriorViewCodeTvs = new TextView[3];
    private TextView[] mRelatedPriorViewShareTvs = new TextView[3];
    private PieGraph[] mRelatedPriorViewPieGraphs = new PieGraph[3];
    private Button[] mRelatedPriorBtns = new Button[3];
    //
    private View[] mRelatedNextViews = new View[3];
    private TextView[] mRelatedNextViewCodeTvs = new TextView[3];
    private TextView[] mRelatedNextViewShareTvs = new TextView[3];
    private PieGraph[] mRelatedNextViewPieGraphs = new PieGraph[3];
    private Button[] mRelatedNextBtns = new Button[3];
    //
    private Button[] mWhyCodeBtns = new Button[3];

    /**
     *
     */

    private ListApiTask mListApiTask = null;


    /**
     *
     */

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);

        final DetailActivity activity = (DetailActivity) getActivity();
        mCode = activity.code;

        mCodeTv = (TextView) rootView.findViewById(R.id.code_tv);
        mNameTv = (TextView) rootView.findViewById(R.id.name_tv);

        mCodeTv.setText(activity.code);
        mNameTv.setText(activity.name);

        mGradeTv = (TextView) rootView.findViewById(R.id.grade_tv);

        /**
         *
         */

        mWhyCodeBtns[0] = (Button) rootView.findViewById(R.id.why_code_btn_0);
        mWhyCodeBtns[1] = (Button) rootView.findViewById(R.id.why_code_btn_1);
        mWhyCodeBtns[2] = (Button) rootView.findViewById(R.id.why_code_btn_2);

        mShareTvs[0] = (TextView) rootView.findViewById(R.id.share_1_tv);
        mShareTvs[1] = (TextView) rootView.findViewById(R.id.share_2_tv);
        mShareTvs[2] = (TextView) rootView.findViewById(R.id.share_3_tv);
        mShareTvs[3] = (TextView) rootView.findViewById(R.id.share_4_tv);

        mShareViews[0] = rootView.findViewById(R.id.share_1_view);
        mShareViews[1] = rootView.findViewById(R.id.share_2_view);
        mShareViews[2] = rootView.findViewById(R.id.share_3_view);
        mShareViews[3] = rootView.findViewById(R.id.share_4_view);

        mRelatedPriorViews[0] = rootView.findViewById(R.id.related_prior_0_view);
        mRelatedPriorViews[1] = rootView.findViewById(R.id.related_prior_1_view);
        mRelatedPriorViews[2] = rootView.findViewById(R.id.related_prior_2_view);

        for (int i = 0; i < 3; i++) {
            mRelatedPriorViewCodeTvs[i] = (TextView) mRelatedPriorViews[i].findViewById(R.id.code_tv);
            mRelatedPriorViewShareTvs[i] = (TextView) mRelatedPriorViews[i].findViewById(R.id.share_tv);
            mRelatedPriorViewPieGraphs[i] = (PieGraph) mRelatedPriorViews[i].findViewById(R.id.piegraph);
            mRelatedPriorBtns[i] = (Button) mRelatedPriorViews[i].findViewById(R.id.btn);
        }

        mRelatedNextViews[0] = rootView.findViewById(R.id.related_next_0_view);
        mRelatedNextViews[1] = rootView.findViewById(R.id.related_next_1_view);
        mRelatedNextViews[2] = rootView.findViewById(R.id.related_next_2_view);

        for (int i = 0; i < 3; i++) {
            mRelatedNextViewCodeTvs[i] = (TextView) mRelatedNextViews[i].findViewById(R.id.code_tv);
            mRelatedNextViewShareTvs[i] = (TextView) mRelatedNextViews[i].findViewById(R.id.share_tv);
            mRelatedNextViewPieGraphs[i] = (PieGraph) mRelatedNextViews[i].findViewById(R.id.piegraph);
            mRelatedNextBtns[i] = (Button) mRelatedNextViews[i].findViewById(R.id.btn);
        }

        /*

         */
        mListApiTask = new ListApiTask();
        mListApiTask.execute();

        /**
         *
         */
        for (int i = 0; i < 3; i++) {
            final int j = i;
            mWhyCodeBtns[i].setOnClickListener(onClickListener);
            mRelatedPriorBtns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startDetailActivity(mRelatedPriorViewCodeTvs[j].getText().toString());
                }
            });
            mRelatedNextBtns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startDetailActivity(mRelatedNextViewCodeTvs[j].getText().toString());
                }
            });
        }


        return rootView;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ListApiTask extends AsyncTask<Void, Void, Course> {
        private int request_code = Argument.REQUEST_CODE_UNEXPECTED;

        @Override
        protected Course doInBackground(Void... params) {
            Course course = null;

            try {
                CourseDetailApi storeDetailApi = new CourseDetailApi(getActivity().getApplication(), mCode);
                course = storeDetailApi.getResult();
                if (course != null)
                    request_code = Argument.REQUEST_CODE_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                request_code = Argument.REQUEST_CODE_FAIL;
                cancel(true);
            }

            return course;
        }

        @Override
        protected void onPostExecute(Course course) {
            mListApiTask = null;

            if (request_code == Argument.REQUEST_CODE_SUCCESS) {
                updateName(course);
                updateShare(course);
                updateWhy(course);
                updateRelatedView(course);

            } else {
                // showErrorView(true, "오류가 발생해 식당을 불러오지 못했습니다");
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mListApiTask = null;

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
        DetailActivity activity = (DetailActivity) getActivity();
        switch (id) {
            case R.id.why_code_btn_0:
                activity.startDetailActivity(mWhyCodeBtns[0].getText().toString());
                break;
            case R.id.why_code_btn_1:
                activity.startDetailActivity(mWhyCodeBtns[1].getText().toString());
                break;
            case R.id.why_code_btn_2:
                activity.startDetailActivity(mWhyCodeBtns[2].getText().toString());
                break;
        }
    }

    ;


    private void updateName(Course course) {
        mNameTv.setText(course.getName());

        if (course.getGrade() != 0) {
            mGradeTv.setText("예상학점 " + getHashGrade(course.getCode()));
            mGradeTv.setVisibility(View.VISIBLE);
        } else {
            mGradeTv.setVisibility(View.INVISIBLE);
        }


    }

    private String getHashGrade(String code) {

        StringBuilder builder = new StringBuilder();
        builder.append(code);
        builder.append(ApiBase.getStringInPrefs(getActivity().getApplication(), Argument.PREFS_UID, "0"));

        int hashed = (builder.toString().hashCode()) % 6;

        if (hashed == 0) {
            return "B-";
        } else if (hashed == 1) {
            return "B0";
        } else if (hashed == 2) {
            return "B+";
        } else if (hashed == 3) {
            return "A-";
        } else if (hashed == 4) {
            return "A0";
        } else {
            return "A+";
        }


    }

    /**
     * @param course
     */
    private void updateWhy(Course course) {
        String[] mCodes = course.getWhyCourses_code();
        for (int i = 0; i < 3; i++) {
            Log.d(TAG, mCodes[i]);
            (mWhyCodeBtns[i]).setText(mCodes[i]);
        }
    }

    /**
     * @param course
     */
    private void updateShare(Course course) {

        int[] mShares = course.getShares();
        for (int i = 0; i < 4; i++) {
            mShareTvs[i].setText(Integer.toString(mShares[i]) + "%");
            mShareViews[i].setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, mShares[i]));
        }
    }

    /**
     * @param course
     */
    private void updateRelatedView(Course course) {

        String[] mRelatedPriorCodes = course.getFromCourses_code();
        int[] mRelatedPriorShares = course.getFromCourses_priority();
        String[] mRelatedNextCodes = course.getToCourses_code();
        int[] mRelatedNextShares = course.getToCourses_priority();

        String[] colors = {"#DB4D21", "#E5D728", "#1DBC5D"};
        for (int i = 0; i < 3; i++) {
            /**
             *
             */
            mRelatedPriorViewCodeTvs[i].setText(mRelatedPriorCodes[i]);
            mRelatedPriorViewShareTvs[i].setText(Integer.toString(mRelatedPriorShares[i]) + "%");

            PieSlice slice = new PieSlice();
            slice.setColor(Color.parseColor("#EEEEEE"));
            slice.setValue(100 - mRelatedPriorShares[i]);
            mRelatedPriorViewPieGraphs[i].addSlice(slice);


            slice = new PieSlice();
            slice.setColor(Color.parseColor(colors[i]));
            slice.setValue(mRelatedPriorShares[i]);
            mRelatedPriorViewPieGraphs[i].addSlice(slice);

            /**
             *
             */
            mRelatedNextViewCodeTvs[i].setText(mRelatedNextCodes[i]);
            mRelatedNextViewShareTvs[i].setText(Integer.toString(mRelatedNextShares[i]) + "%");

            slice = new PieSlice();
            slice.setColor(Color.parseColor("#EEEEEE"));
            slice.setValue(100 - mRelatedNextShares[i]);
            mRelatedNextViewPieGraphs[i].addSlice(slice);

            slice = new PieSlice();
            slice.setColor(Color.parseColor(colors[i]));
            slice.setValue(mRelatedNextShares[i]);
            mRelatedNextViewPieGraphs[i].addSlice(slice);


        }
    }

    /**
     *
     */
    public void onDestroy() {
        super.onDestroy();

        if (mListApiTask != null) {
            mListApiTask.cancel(true);
        }

    }
}
