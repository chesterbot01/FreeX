package io.github.chesterboy01.freex;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.chesterboy01.freex.entity.User;

/**
 * Created by Administrator on 11/8/2016.
 */

public interface MessageResponseFromAsyncToUIThread {
    void onReceivedSuccess(User user_process, JSONObject json) throws JSONException;
}
