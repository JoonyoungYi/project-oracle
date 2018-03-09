package kr.ac.kaist.include.kha_zix.api;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.kaist.include.kha_zix.model.Course;
import kr.ac.kaist.include.kha_zix.utils.Argument;
import kr.ac.kaist.include.kha_zix.utils.RequestManager;

public class HistoryListApi extends ApiBase {
    private final static String TAG = "History List Api";

    /**
     * Init
     */
    public HistoryListApi(Application application) {

        this.application = application;
        RequestManager rc = new RequestManager("/history/list/", "GET", "http");
        rc.addBodyValue("klms_uid", getStringInPrefs(application, Argument.PREFS_UID, ""));
        rc.doRequest();

        response = rc.getResponse_body();
        Log.d(TAG, response);
    }

    /**
     * @return
     */
    public ArrayList<Course> getResult() {
        ArrayList<Course> stores = new ArrayList<Course>();

        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = null;

            jsonArray = jsonObj.getJSONArray("semesters");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String semester = "";
                if (!obj.isNull("semester")) {
                    semester = obj.getString("semester");
                    semester = semester.replace(" ", "");
                    semester = semester.substring(0, 5);
                }

                JSONArray courseArray = obj.getJSONArray("courses");
                for (int j = 0; j < courseArray.length(); j++) {
                    JSONObject courseObj = courseArray.getJSONObject(j);
                    Course store = new Course();

                    store.setSemester(semester);

                    if (!courseObj.isNull("code")) {
                        String code = courseObj.getString("code");
                        store.setCode(code);
                    }

                    if (!courseObj.isNull("name")) {
                        String menu = courseObj.getString("name");
                        store.setName(menu);
                    }
                    stores.add(store);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
            stores = null;
        }


        try {

            String b = getStringInPrefs(application, Argument.PREFS_IS_SERVED_GRADES, "");
            if (b != null && b.equals("true")) {
                for (Course store : stores) {
                    store.setGrade(-1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stores;


    }


}
