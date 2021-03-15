package za.co.robusttech.sewa_in.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Project Name - sewa-in
 * Created on 2021/03/14 at 8:34 PM
 */
public final class ProgressBar {
    private static  ProgressDialog progressDialog;

    //displaying dialog
    public static  void displayDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //hide dialog
    public static void hideDialog() {
        progressDialog.dismiss();
    }
}
