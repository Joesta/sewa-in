package za.co.robusttech.sewain.rest;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import za.co.robusttech.sewain.BuildConfig;


/**
 * Project Name - sewa-in
 * Created on 2021/05/14 at 10:39 PM
 */
public class SewainRestClient {
    private static final String BASE_URL = BuildConfig.BRAINTREE_PAYMENT_SERVER_URL;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
