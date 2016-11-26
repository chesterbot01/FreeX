package io.github.chesterboy01.freex.net;

import android.app.Application;

import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * Created by Administrator on 11/25/2016.
 */


public class CookieApplication extends Application {
    private List<Cookie> cookies;

    public List<Cookie> getCookie(){
        return cookies;
    }

    public void setCookie(List<Cookie> cks){
        cookies = cks;
    }
}
