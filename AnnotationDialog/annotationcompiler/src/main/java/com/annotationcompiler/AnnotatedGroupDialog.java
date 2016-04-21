package com.annotationcompiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by user on 4/21/16.
 */
public class AnnotatedGroupDialog {
  private String qualifiedClassName;
  private String SUFFIX = "dialog";
  private Map<String, AnnotatedDialog> dialogMap = new LinkedHashMap<>();

  public AnnotatedGroupDialog(String qualifiedClassName) {
    this.qualifiedClassName = qualifiedClassName;
  }

  public void addToMap(AnnotatedDialog annotatedDialog) {
    if ((dialogMap.get(annotatedDialog.getType()) != null)) {
      dialogMap.replace(annotatedDialog.getType(), annotatedDialog);
    } else {
      dialogMap.put(annotatedDialog.getType(), annotatedDialog);
    }
  }

  public void generateCode(Elements elementUtils, Filer filer) throws IOException {
    TypeElement typeElement = elementUtils.getTypeElement(qualifiedClassName);
    PackageElement pkg = elementUtils.getPackageOf(typeElement);
    String packageName = pkg.toString() + SUFFIX;
    ClassName nullPointer = ClassName.get("java.lang", "NullPointerException");
    ClassName IllegalArgument = ClassName.get("java.lang", "IllegalArgumentException");

    ClassName alertDialogClass = ClassName.get("android.support.v7.app", "AlertDialog");
    ClassName android = ClassName.get("android.app", "Application");
    ClassName context = ClassName.get("android.content", "Context");

    MethodSpec main = MethodSpec.methodBuilder("buildDialog")
        .addModifiers(Modifier.PUBLIC)
        .addModifiers(Modifier.STATIC)
        .addParameter(context, "context")
        .addCode(
            "if(alert==null){\n $T.Builder builder = new $T.Builder(context); \n builder.setTitle($S); builder.setMessage(\"SADJLSJLD \");"
                + " \n alert =builder.create(); \n }\nreturn alert;\n", alertDialogClass,
            alertDialogClass, "Tsss")
        .returns(alertDialogClass)
        .build();

    MethodSpec show = MethodSpec.methodBuilder("show")
        .addModifiers(Modifier.PUBLIC)
        .addModifiers(Modifier.STATIC)
        .addCode("if (alert != null) {\n"
            + "      alert.show();\n"
            + "    } else {\n"
            + "      throw new $T(\"Please create a dialog first\");\n"
            + "    }", nullPointer)
        .returns(void.class)
        .build();

    MethodSpec bind = MethodSpec.methodBuilder("bind")
        .addModifiers(Modifier.PUBLIC)
        .addModifiers(Modifier.STATIC)
        .addParameter(context, "context")
        .addCode(
            "if(context instanceof $T){throw new $T(\"Please dont use application context\");}else{mcontext=context;\n"
                + " buildDialog(mcontext); } \n", android, IllegalArgument)
        .returns(void.class)
        .build();

    TypeSpec helloWorld = TypeSpec.classBuilder("DialogUtils")
        .addField(context, "mcontext", Modifier.PRIVATE, Modifier.STATIC)
        .addField(alertDialogClass, "alert", Modifier.PRIVATE, Modifier.STATIC)
        .addModifiers(Modifier.PUBLIC)
        .addMethod(bind)
        .addMethod(show)
        .addMethod(main)
        .build();

    JavaFile.builder(packageName, helloWorld).build().writeTo(filer);
  }
}