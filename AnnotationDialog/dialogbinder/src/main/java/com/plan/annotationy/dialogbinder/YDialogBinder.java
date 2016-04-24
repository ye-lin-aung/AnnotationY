package com.plan.annotationy.dialogbinder;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by user on 4/23/16.
 */
public class YDialogBinder {
  private static Context mcontext;
  private static AlertDialog.OnCancelListener cancelListener;

  public static void bind(Context context) {
    mcontext = context;
  }

  public static void setMcontext(Context mcontext) {
    YDialogBinder.mcontext = mcontext;
  }

  public static Context getMcontext() {
    return mcontext;
  }

  public AlertDialog.OnCancelListener getCancelListener() {
    return cancelListener;
  }

  public static void setCancelListener(AlertDialog.OnCancelListener cancelListener) {
    YDialogBinder.cancelListener = cancelListener;
  }
}
