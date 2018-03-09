package kr.ac.kaist.include.kha_zix.api;

import android.app.Application;
import android.util.Log;

import org.apache.http.cookie.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import kr.ac.kaist.include.kha_zix.utils.Argument;
import kr.ac.kaist.include.kha_zix.utils.RequestManager;


public class LoginApi extends ApiBase {
    private final static String TAG = "Login Api";
    private boolean request_success = false;
    private List<Cookie> cookies = null;

    private String klms_username = null;
    private String klms_password = null;

    /**
     * @param application
     * @param klms_username
     * @param klms_password
     */
    public LoginApi(Application application, String klms_username, String klms_password) {
        this.application = application;

        this.klms_username = klms_username;
        this.klms_password = klms_password;

        String CookieMoodelSession = null;


        RequestManager rm = new RequestManager("", "GET", "http");
        rm.setBaseUrl("klms.kaist.ac.kr");
        rm.doRequest();

        cookies = rm.getResponse_cookie();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("MoodleSession")) {
                CookieMoodelSession = cookie.getValue();
                Log.d(TAG, "Cookie MoodleSession : " + CookieMoodelSession);
            }
        }

        /*

         */
        rm = new RequestManager("/login/index.php", "POST", "http");
        rm.setBaseUrl("klms.kaist.ac.kr");
        rm.setRedirect(false);
        rm.addBodyValue("username", klms_username);
        rm.addBodyValue("password", klms_password);
        rm.addHeader("Cookie", "MoodleSession=" + CookieMoodelSession);
        rm.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");
        rm.addHeader("Accept", "*/*");
        rm.addHeader("Accept-Encoding", "gzip,deflate,sdch");
        rm.addHeader("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
        rm.doRequest();

        cookies = rm.getResponse_cookie();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("MoodleSession")) {
                CookieMoodelSession = cookie.getValue();
                Log.d(TAG, "Cookie MoodleSession : " + CookieMoodelSession);
            }
        }

        rm = new RequestManager("", "GET", "http");
        rm.setBaseUrl("klms.kaist.ac.kr");
        rm.addHeader("Cookie", "MoodleSession=" + CookieMoodelSession);
        rm.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");
        rm.addHeader("Accept", "*/*");
        rm.addHeader("Accept-Encoding", "gzip,deflate,sdch");
        rm.addHeader("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
        rm.doRequest();

        response = rm.getResponse_body();

    }

    /**
     * 결과를 반환합니다.
     */

    public void getResult() {
        /**
         *
         */
        Document doc = Jsoup.parse(response);
        Elements divs = doc.select("div");

        for (Element div : divs) {

            if (div.className().trim().equals("dropdown userinfo")) {
                Elements as = div.getElementsByTag("a");

                for (Element a : as) {
                    Log.d(TAG, "a html: " + a.html());
                    if (a.html().trim().equals("개인정보 수정") || a.html().trim().equals("Update profile")) {

                        Log.d(TAG, "a attr: " + a.attr("abs:href"));

                        String uid = a.attr("abs:href");
                        uid = uid.substring(41);

                        Log.d(TAG, uid);
                        setString2Prefs(application, Argument.PREFS_UID, uid);
                        request_success = true;
                        setString2Prefs(application, Argument.PREFS_USERNAME, klms_username);
                        setString2Prefs(application, Argument.PREFS_PASSWORD, klms_password);


                    }
                }

                Elements strongs = div.getElementsByTag("strong");
                setString2Prefs(application, Argument.PREFS_NAME, strongs.get(0).html());
                setString2Prefs(application, Argument.PREFS_DEPARTMENT, strongs.get(2).html());


            }

        }


    }

    /**
     * @return
     */
    public int getRequestCode() {
        if (request_success) {
            return Argument.REQUEST_CODE_SUCCESS;
        } else {
            return Argument.REQUEST_CODE_FAIL;
        }
    }
}
