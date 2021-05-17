package za.co.robusttech.sewain.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Project Name - sewa-in
 * Created on 2021/03/14 at 4:35 PM
 */
public final class NavUtil {
    public static void moveTo(@NonNull Activity fromContext, @NonNull Class<? extends AppCompatActivity> toContext, @Nullable Bundle bundleExtra) {
        Intent intent = new Intent(fromContext, toContext);
        if (bundleExtra == null) {
            fromContext.startActivity(intent);
        } else {
            assert bundleExtra != null;
            intent.putExtras(bundleExtra);
            fromContext.startActivity(intent);
        }
    }
}
