package za.co.robusttech.sewain.rest;

import android.util.Log;

import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Project Name - sewa-in
 * Created on 2021/05/14 at 10:31 PM
 */
public class HttpResponseHelper extends TextHttpResponseHandler {

    private static final String TAG = "HttpResponseHelper";

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String clientToken) {
       if (clientToken != null) {
           // process payment
           Log.d(TAG, "onSuccess: process payment");
       }

    }

}
