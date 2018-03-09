package kr.ac.kaist.include.kha_zix.api;

import android.app.Application;
import android.util.Log;

import com.echo.holographlibrary.PieGraph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.kaist.include.kha_zix.model.Course;
import kr.ac.kaist.include.kha_zix.utils.Argument;
import kr.ac.kaist.include.kha_zix.utils.RequestManager;

public class CourseDetailApi extends ApiBase {
    private final static String TAG = "Store List Api";

    /**
     * Init
     */
    public CourseDetailApi(Application application, String code) {

        this.application = application;
        RequestManager rc = new RequestManager("/course/detail/", "GET", "http");
        rc.addBodyValue("klms_uid", getStringInPrefs(application, Argument.PREFS_UID, ""));
        rc.addBodyValue("course_code", code);
        rc.doRequest();

        response = rc.getResponse_body();
        Log.d(TAG, response);
    }

    /**
     * @return
     */
    public Course getResult() {
        Course course = new Course();

        try {
            JSONObject obj = new JSONObject(response);

            if (!obj.isNull("code")) {
                String code = obj.getString("code");
                course.setCode(code);
            }

            if (!obj.isNull("name")) {
                String menu = obj.getString("name");
                course.setName(menu);
            }


            if (!obj.isNull("student_distribution")) {
                JSONObject distObj = obj.getJSONObject("student_distribution");
                int[] shares = new int[4];
                int sum = 0;
                for (int i = 0; i < 4; i++) {
                    if (!distObj.isNull(Integer.toString(i + 1))) {
                        shares[i] = distObj.getInt(Integer.toString(i + 1));
                        sum += shares[i];
                    }
                }

                for (int i = 0; i < 4; i++) {
                    shares[i] = shares[i] * 100 / sum;
                }
                course.setShares(shares);
            }

            if (!obj.isNull("from_courses")) {
                JSONArray courseArray = obj.getJSONArray("from_courses");
                String[] courses_code = {"", "", ""};
                int[] courses_priority = {0, 0, 0};
                for (int i = 0; i < Math.min(courseArray.length(), 3); i++) {
                    JSONObject courseObj = courseArray.getJSONObject(i);

                    if (!courseObj.isNull("code")) {
                        courses_code[i] = courseObj.getString("code");

                    }

                    if (!courseObj.isNull("priority")) {
                        courses_priority[i] = Math.min((int) (courseObj.getDouble("priority") * 100 + 1), 99);

                    }

                }
                course.setFromCourses_code(courses_code);
                course.setFromCourses_priority(courses_priority);
            }

            if (!obj.isNull("to_courses")) {
                JSONArray courseArray = obj.getJSONArray("to_courses");
                String[] courses_code = {"", "", ""};
                int[] courses_priority = {0, 0, 0};
                for (int i = 0; i < Math.min(courseArray.length(), 3); i++) {
                    JSONObject courseObj = courseArray.getJSONObject(i);

                    if (!courseObj.isNull("code")) {
                        courses_code[i] = courseObj.getString("code");

                    }

                    if (!courseObj.isNull("priority")) {
                        courses_priority[i] = Math.min((int) (courseObj.getDouble("priority") * 100 + 1), 99);

                    }

                }
                course.setToCourses_code(courses_code);
                course.setToCourses_priority(courses_priority);

            }

            if (!obj.isNull("why_courses")) {
                JSONArray courseArray = obj.getJSONArray("why_courses");
                String[] courses_code = {"", "", ""};
                for (int i = 0; i < Math.min(courseArray.length(), 3); i++) {
                    JSONObject courseObj = courseArray.getJSONObject(i);

                    if (!courseObj.isNull("code")) {
                        courses_code[i] = courseObj.getString("code");
                    }
                }
                course.setWhyCourses_code(courses_code);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            course = null;
        }


        try {
            String b = getStringInPrefs(application, Argument.PREFS_IS_SERVED_GRADES, "");
            if (b != null && b.equals("true")) {
                course.setGrade(-1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return course;


    }


}
