package com.github.weiss.example.util;

import com.google.android.material.snackbar.Snackbar;
import android.view.View;

public class SnackbarUtil {

    public static void showMessage(View view, String text) {

        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }
}
