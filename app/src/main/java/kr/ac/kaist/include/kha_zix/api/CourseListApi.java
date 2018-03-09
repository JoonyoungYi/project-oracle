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

public class CourseListApi extends ApiBase {
    private final static String TAG = "Store List Api";

    /**
     * Init
     */
    public CourseListApi(Application application) {

        RequestManager rc = new RequestManager("/course/list/", "GET", "http");
        rc.addBodyValue("klms_uid", getStringInPrefs(application, Argument.PREFS_UID, ""));
        rc.doRequest();

        response = rc.getResponse_body();
        Log.d(TAG, response);
    }

    /**
     * @return
     */
    public ArrayList<Course> getResult() {
        ArrayList<Course> courses = new ArrayList<Course>();

        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = null;

            jsonArray = jsonObj.getJSONArray("courses");
            for (int i = 0; i < Math.min(jsonArray.length(), 10); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Course course = new Course();

                if (!obj.isNull("priority")) {
                    double priority = obj.getDouble("priority");
                    course.setPriority((int) (priority * 10000));
                }

                if (!obj.isNull("code")) {
                    String code = obj.getString("code");
                    course.setCode(code);
                }

                if (!obj.isNull("name")) {
                    String menu = obj.getString("name");
                    course.setName(menu);
                }

                courses.add(course);

            }


        } catch (JSONException e) {
            e.printStackTrace();

            /* For Debugging */
            for (int i = 9 ; i > 0  ; i -- ){
                Course course = new Course();
                course.setPriority(i*10);
                course.setCode("CS360");
                course.setName("데이터베이스개론");

                courses.add(course);
            }

            /* For Real */
            /* courses = null */
        }


        return courses;


    }


}
