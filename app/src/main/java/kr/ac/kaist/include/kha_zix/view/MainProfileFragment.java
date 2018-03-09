package kr.ac.kaist.include.kha_zix.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.ac.kaist.include.kha_zix.R;
import kr.ac.kaist.include.kha_zix.api.ApiBase;
import kr.ac.kaist.include.kha_zix.api.HistoryListApi;
import kr.ac.kaist.include.kha_zix.model.Course;
import kr.ac.kaist.include.kha_zix.utils.Argument;

public class MainProfileFragment extends Fragment {
    private static final String TAG = "KhazixProfileFragment";
    /**
     *
     */

    private View mErrorView;
    private TextView mErrorTv;
    private ProgressBar mErrorPb;

    Button mGpaBtn;
    private ListView mLv;
    private LvAdapter mLvAdapter;

    private TextView mNameTv;
    private TextView mDepartmentTv;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProgressDialog dialog;


    /**
     *
     */

    private ListApiTask mListApiTask = null;
    private GpaApiTask mGpaApiTask = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KhazixProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainProfileFragment newInstance(String param1, String param2) {
        MainProfileFragment fragment = new MainProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.khazix_profile_fragment, container, false);
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
        View headerView = inflater.inflate(R.layout.khazix_profile_fragment_lv_header, null);
        mNameTv = (TextView) headerView.findViewById(R.id.name_tv);
        mDepartmentTv = (TextView) headerView.findViewById(R.id.department_tv);
        mNameTv.setText(ApiBase.getStringInPrefs(getActivity().getApplication(), Argument.PREFS_NAME, "-"));
        mDepartmentTv.setText(ApiBase.getStringInPrefs(getActivity().getApplication(), Argument.PREFS_DEPARTMENT, "-"));
        mLv.addHeaderView(headerView);

        View footerView = inflater.inflate(R.layout.khazix_profile_fragment_lv_footer, null);
        mGpaBtn = (Button) footerView.findViewById(R.id.gpa_btn);
        mLv.addFooterView(footerView);


        /*
         * ListView Setting
		 */

        ArrayList<Course> courses = new ArrayList<Course>();
        mLvAdapter = new LvAdapter(getActivity(), R.layout.khazix_profile_fragment_lv,
                courses);
        mLv.setAdapter(mLvAdapter);
        //       mLv.setOnItemClickListener(onItemClickListener);

                /*

         */
        mListApiTask = new ListApiTask();
        mListApiTask.execute();

        mGpaBtn.setOnClickListener(onClickListener);

        // Inflate the layout for this fragment
        return rootView;
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
            case R.id.gpa_btn:
                if (mGpaApiTask == null) {
                    dialog = ProgressDialog.show(getActivity(), "", "내 학점을 파싱해오는 중입니다", true);

                    mGpaApiTask = new GpaApiTask();
                    mGpaApiTask.execute();
                }
                break;
        }
    }

    ;


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class GpaApiTask extends AsyncTask<Void, Void, Void> {
        private int request_code = Argument.REQUEST_CODE_UNEXPECTED;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ApiBase.setString2Prefs(MainProfileFragment.this.getActivity().getApplication(), Argument.PREFS_IS_SERVED_GRADES, "true");

            String test = ApiBase.getStringInPrefs(MainProfileFragment.this.getActivity().getApplication(), Argument.PREFS_IS_SERVED_GRADES, "");
            Log.d(TAG, test);

            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            mGpaApiTask = null;

            for (Course course : mLvAdapter.courses) {
                course.setGrade(-1);
            }
            mLvAdapter.notifyDataSetChanged();
            dialog.dismiss();

            Toast.makeText(getActivity(), "성공적으로 파싱이 완료되었습니다.", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mGpaApiTask = null;

        }
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

        public LvAdapter(Activity context, int textViewResourceId,
                         ArrayList<Course> courses) {
            super(context, textViewResourceId, courses);

            this.textViewResourceId = textViewResourceId;
            this.courses = courses;

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

                viewHolder.mSemesterTv = (TextView) convertView.findViewById(R.id.semester_tv);
                viewHolder.mCodeTv = (TextView) convertView.findViewById(R.id.code_tv);
                viewHolder.mNameTv = (TextView) convertView.findViewById(R.id.name_tv);
                viewHolder.mGradeTv = (TextView) convertView.findViewById(R.id.grade_tv);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Course course = this.getItem(position);

			/*
             * Data Import and export
			 */

            if (position == 0 || !course.getSemester().equals(this.getItem(position - 1).getSemester())) {
                viewHolder.mSemesterTv.setText(course.getSemester());
                viewHolder.mSemesterTv.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mSemesterTv.setVisibility(View.INVISIBLE);
            }

            viewHolder.mCodeTv.setText(course.getCode());
            viewHolder.mNameTv.setText(course.getName());

            if (course.getGrade() != 0) {
                viewHolder.mGradeTv.setText("-");
                viewHolder.mGradeTv.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mGradeTv.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }

        private class ViewHolder {
            TextView mSemesterTv;
            TextView mCodeTv;
            TextView mNameTv;
            TextView mGradeTv;
        }

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
                HistoryListApi historyListApi = new HistoryListApi(getActivity().getApplication());

                stores = historyListApi.getResult();
                request_code = Argument.REQUEST_CODE_SUCCESS;


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

                ApiBase.setString2Prefs(getActivity().getApplication(), Argument.PREFS_IS_SERVED_GRADES, "true");
                mLvAdapter.courses.clear();
                mLvAdapter.courses.addAll(courses);
                mLvAdapter.notifyDataSetChanged();


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

    /**
     *
     */
    public void onDestroy() {
        super.onDestroy();

        if (mListApiTask != null) {
            mListApiTask.cancel(true);
        }

        if (mGpaApiTask != null) {
            mGpaApiTask.cancel(true);
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
