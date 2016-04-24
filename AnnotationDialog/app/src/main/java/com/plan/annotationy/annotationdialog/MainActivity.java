package com.plan.annotationy.annotationdialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.annotationapi.Click;
import com.annotationapi.VarDialog;
import com.annotationcompiler.DialogBind.ErrorDialog$YDialog;
import com.annotationcompiler.DialogBind.InfoDialog$YDialog;
import com.annotationcompiler.DialogBind.ShitDialog$YDialog;
import com.plan.annotationy.dialogbinder.YDialogBinder;

public class MainActivity extends AppCompatActivity {
  @Bind(R.id.button) Button button;
  @Bind(R.id.button2) Button button2;
  @Bind(R.id.button3) Button button3;
  
  @VarDialog(message = "What does the fox say", touchOutsideClosed = false) String ShitDialog;
  @VarDialog(title = "HelloError", message = "Check this error") String ErrorDialog;
  @VarDialog(title = "Hello", message = "Check this out") String InfoDialog;

  @Click public void ClickOnPositive() {
    Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);
    YDialogBinder.bind(this);

    button2.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        ErrorDialog$YDialog.show();
      }
    });

    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        InfoDialog$YDialog.show();
      }
    });
    button3.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        ShitDialog$YDialog.show();
      }
    });
  }
}
