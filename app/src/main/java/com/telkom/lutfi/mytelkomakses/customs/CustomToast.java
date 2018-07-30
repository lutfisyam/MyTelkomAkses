package com.telkom.lutfi.mytelkomakses.customs;

import android.content.Context;
import android.widget.Toast;

public class CustomToast {

    private Context context;
    private Toast toast;

    public CustomToast() {
    }

    public CustomToast(Context context) {
        this.context = context;
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
