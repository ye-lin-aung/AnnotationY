package com.plan.annotationy.annotationdialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.annotationapi.Dialog;
import com.plan.annotationy.annotationdialogdialog.DialogUtils;

@Dialog(title = "Hello", message = "Check this out", clazz = MainActivity.class)
public class MainActivity extends AppCompatActivity {
  @Bind(R.id.button) Button button;

  @OnClick(R.id.button) public void Click(View view) {
    DialogUtils.show();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    DialogUtils.bind(this);
  }
}
