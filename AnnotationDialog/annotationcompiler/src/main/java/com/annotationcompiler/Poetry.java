package com.annotationcompiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Created by user on 4/23/16.
 */
public class Poetry {
  private TypeSpec.Builder builder;
  private String packageName = "com.annotationcompiler.DialogBind";
  private List<String> methodNames = new ArrayList<>();
  String codeBlock = "";

  public void addMethod(String s) {
    methodNames.add(s);
  }

  ClassName nullPointer = ClassName.get("java.lang", "NullPointerException");
  ClassName IllegalArgument = ClassName.get("java.lang", "IllegalArgumentException");

  ClassName alertDialogClass = ClassName.get("android.support.v7.app", "AlertDialog");
  ClassName android = ClassName.get("android.app", "Application");
  ClassName context = ClassName.get("android.content", "Context");

  public void write(Filer filer) throws IOException {
    TypeSpec.Builder builder = TypeSpec.classBuilder("DialogUtils");

    for (String s : methodNames) {
      codeBlock = codeBlock + s + "(context);";
    }
    MethodSpec bind = MethodSpec.methodBuilder("bind")
        .addModifiers(Modifier.PUBLIC)
        .addModifiers(Modifier.STATIC)
        .addParameter(context, "context")
        .addCode(
            "if(context instanceof $T){throw new $T(\"Please dont use application context\");}else{"
                + codeBlock
                + " } \n", android, IllegalArgument)
        .returns(void.class)
        .build();

    TypeSpec typeSpec = builder.addModifiers(Modifier.PUBLIC).addMethod(bind).build();

    JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
    javaFile.writeTo(filer);
  }
}
