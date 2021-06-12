package za.co.robusttech.sewain.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import za.co.robusttech.sewain.R;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.ArrayList;


public class BuyedFragments extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        return inflater.inflate(R.layout.fragment_buyed, container, false);
    }
}