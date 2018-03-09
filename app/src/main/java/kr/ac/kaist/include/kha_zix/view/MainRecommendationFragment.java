package kr.ac.kaist.include.kha_zix.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.kaist.include.kha_zix.R;
import kr.ac.kaist.include.kha_zix.api.CourseListApi;
import kr.ac.kaist.include.kha_zix.model.Course;
import kr.ac.kaist.include.kha_zix.utils.Argument;


public class MainRecommendationFragment extends Fragment {

    /**
     *
     */

    private View mErrorView;
    private TextView mErrorTv;
    private ProgressBar mErrorPb;

    private ListView mLv;
    private LvAdapter mLvAdapter;

    /**
     *
     */

    private ListApiTask mListApiTask = null;


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainRecommendationFragment newInstance(int sectionNumber) {
        MainRecommendationFragment fragment = new MainRecommendationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);


        fragment.setArguments(args);
        return fragment;
    }

    public MainRecommendationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.khazix_fragment, container, false);

        /**
         * ListView Default Setting
         */
        mLv = (ListView) rootView.findViewById(R.id.lv);
        mErrorView = rootView.findViewById(R.id.error_view);
        mErrorTv = (TextView) mErrorView.findViewById(R.id.error_tv);
        mErrorPb = (ProgressBar) mErrorView.findViewById(R.id.error_pb);

        /**
         *
         */
        View headerView = new View(getActivity());
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            headerView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, actionBarHeight));
        }
        mLv.addHeaderView(headerView);

        View footerView = new View(getActivity());
        mLv.addFooterView(footerView);


        /*
         * ListView Setting
		 */

        ArrayList<Course> courses = new ArrayList<Course>();
        mLvAdapter = new LvAdapter(getActivity(), R.layout.khazix_fragment_lv,
                courses);
        mLv.setAdapter(mLvAdapter);
        mLvAdapter.refreshPriority();
        mLv.setOnItemClickListener(onItemClickListener);

        /*

         */
        mListApiTask = new ListApiTask();
        mListApiTask.execute();


        return rootView;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ListApiTask extends AsyncTask<Void, Void, ArrayList<Course>> {
        private int request_code = Argument.REQUEST_CODE_UNEXPECTED;

        @Override
        protected ArrayList<Course> doInBackground(Void... params) {
            ArrayList<Course> stores = null;

            try {
                CourseListApi storeListApi = new CourseListApi(getActivity().getApplication());

                stores = storeListApi.getResult();
                if (stores != null){
                    request_code = Argument.REQUEST_CODE_SUCCESS;
                }


            } catch (Exception e) {
                e.printStackTrace();
                request_code = Argument.REQUEST_CODE_FAIL;
                cancel(true);
            }

            return stores;
        }

        @Override
        protected void onPostExecute(ArrayList<Course> courses) {
            mListApiTask = null;

            if (request_code == Argument.REQUEST_CODE_SUCCESS) {

                mLvAdapter.courses.clear();
                mLvAdapter.courses.addAll(courses);
                mLvAdapter.notifyDataSetChanged();
                mLvAdapter.refreshPriority();


               /* if (stores == null) {
                    showErrorView(true, "오류가 발생해 식당을 불러오지 못했습니다");

                } else if (stores.size() != 0) {
                    showErrorView(false, "");

                } else if (store_type == 100) {
                    showErrorView(true, "등록된 즐겨찾기가 없습니다.");

                } else {
                    showErrorView(true, "데이터가 없습니다");
                }*/
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(0);
    }


    /**
     * ListView Apdater Setting
     */

    private class LvAdapter extends ArrayAdapter<Course> {
        private static final String TAG = "BabFragment LvAdapter";

        private ViewHolder viewHolder = null;
        public ArrayList<Course> courses;
        private int textViewResourceId;
        private Resources r = getResources();


        private int max_priority = 0;
        private int min_priority = -1;

        private int fl_size_max = 250;
        private int fl_size_min = 100;
        private int code_tv_size_max = 45;
        private int code_tv_size_min = 18;
        private int name_tv_size_max = 20;
        private int name_tv_size_min = 12;
        private int r_max = 34;
        private int r_min = 146;
        private int g_max = 148;
        private int g_min = 213;
        private int b_max = 211;
        private int b_min = 239;

        public LvAdapter(Activity context, int textViewResourceId,
                         ArrayList<Course> courses) {
            super(context, textViewResourceId, courses);

            this.textViewResourceId = textViewResourceId;
            this.courses = courses;

        }

        public void refreshPriority() {

            max_priority = 0;
            min_priority = -1;

            for (Course course : courses) {
                if (course.getPriority() > max_priority) {
                    max_priority = course.getPriority();
                }

                if (min_priority == -1 || course.getPriority() < min_priority) {
                    min_priority = course.getPriority();
                }
            }

        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getCount() {
            return courses.size();
        }

        @Override
        public Course getItem(int position) {
            return courses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

			/*
             * UI Initiailizing : View Holder
			 */

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(textViewResourceId, null);

                viewHolder = new ViewHolder();

                viewHolder.mCircleFl = (FrameLayout) convertView.findViewById(R.id.circle_fl);

                viewHolder.mCodeTv = (TextView) convertView.findViewById(R.id.code_tv);
                viewHolder.mNameTv = (TextView) convertView.findViewById(R.id.name_tv);
                viewHolder.mRankTv = (TextView) convertView.findViewById(R.id.rank_tv);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Course course = this.getItem(position);

			/*
             * Data Import and export
			 */

            int fl_size = fl_size_max * (course.getPriority() - min_priority) / (max_priority - min_priority) + fl_size_min * (max_priority - course.getPriority()) / (max_priority - min_priority);
            int code_tv_size = code_tv_size_max * (course.getPriority() - min_priority) / (max_priority - min_priority) + code_tv_size_min * (max_priority - course.getPriority()) / (max_priority - min_priority);
            int name_tv_size = name_tv_size_max * (course.getPriority() - min_priority) / (max_priority - min_priority) + name_tv_size_min * (max_priority - course.getPriority()) / (max_priority - min_priority);


            viewHolder.mCodeTv.setText(course.getCode());
            viewHolder.mCodeTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, code_tv_size);
            if (fl_size > 150) {
                viewHolder.mNameTv.setText(course.getName());
                viewHolder.mNameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, name_tv_size);
                viewHolder.mNameTv.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mNameTv.setVisibility(View.GONE);
            }
            viewHolder.mRankTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, name_tv_size * 3 / 2);
            String rank = Integer.toString(position + 1);
            if (position + 1 == 1) {
                rank += "st";
            } else if (position + 1 == 2) {
                rank += "rd";
            } else {
                rank += "th";
            }
            viewHolder.mRankTv.setText(rank);


            fl_size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, fl_size, r.getDisplayMetrics());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(fl_size, fl_size);
            viewHolder.mCircleFl.setLayoutParams(params);


            int r = r_max * (course.getPriority() - min_priority) / (max_priority - min_priority) + r_min * (max_priority - course.getPriority()) / (max_priority - min_priority);
            int g = g_max * (course.getPriority() - min_priority) / (max_priority - min_priority) + g_min * (max_priority - course.getPriority()) / (max_priority - min_priority);
            int b = b_max * (course.getPriority() - min_priority) / (max_priority - min_priority) + b_min * (max_priority - course.getPriority()) / (max_priority - min_priority);

            viewHolder.mCircleFl.setBackgroundColor(Color.rgb(r, g, b));

            return convertView;
        }

        private class ViewHolder {
            FrameLayout mCircleFl;
            TextView mCodeTv;
            TextView mNameTv;
            TextView mRankTv;
        }

    }

    /**
     *
     */
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            MainActivity activity = (MainActivity) getActivity();
            activity.startDetailActivity(mLvAdapter.courses.get(i-1).getCode(), mLvAdapter.courses.get(i-1).getName());

        }
    };

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
