package com.annotationcompiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

/**
 * Created by user on 4/23/16.
 */
public class AnnotatedGroupVariableDialog {
  private String qualifiedClassName;
  private AnnotatedVariableDialog varName;
  private String packageName = "com.annotationcompiler.DialogBind";
  private MethodSpec methodSpec;

  private TypeSpec typeSpec;

  ClassName nullPointer = ClassName.get("java.lang", "NullPointerException");
  ClassName IllegalArgument = ClassName.get("java.lang", "IllegalArgumentException");
  ClassName YDialogBinder = ClassName.get(" com.plan.annotationy.dialogbinder", "YDialogBinder");

  ClassName alertDialogClass = ClassName.get("android.support.v7.app", "AlertDialog");
  ClassName android = ClassName.get("android.app", "Application");
  ClassName context = ClassName.get("android.content", "Context");

  private Map<String, AnnotatedVariableDialog> dialogMap = new LinkedHashMap<>();

  public AnnotatedGroupVariableDialog(String qualifiedClassName, AnnotatedVariableDialog varName) {
    this.qualifiedClassName = qualifiedClassName;
    this.varName = varName;
  }

  public void addToMap(AnnotatedVariableDialog annotatedDialog) {
    if ((dialogMap.get(annotatedDialog.getDeclaredName()) != null)) {
      dialogMap.replace(annotatedDialog.getDeclaredName(), annotatedDialog);
    } else {
      dialogMap.put(annotatedDialog.getDeclaredName(), annotatedDialog);
    }
  }

  public void generateCode(Elements elementUtils, Filer filer) throws IOException {
    elementUtils.getTypeElement(qualifiedClassName);
    String methodName = "YDialog" + varName.getDeclaredName();
    methodSpec = MethodSpec.methodBuilder(methodName)
        .addModifiers(Modifier.PUBLIC)
        .addModifiers(Modifier.STATIC)
        .addParameter(context, "context")
        .addCode(
            "if(alert==null){\n $T.Builder builder = new $T.Builder(context); \n builder.setTitle($S); builder.setMessage($S);"
                + " \n alert =builder.create(); \n"
                + "alert.setCanceledOnTouchOutside("
                + varName.isTouchOutsideClosed()
                + ");\n"
                + " }\nreturn alert;\n", alertDialogClass, alertDialogClass, varName.getTitle(),
            varName.getMessage())
        .returns(alertDialogClass)
        .build();

    MethodSpec show = MethodSpec.methodBuilder("show")
        .addModifiers(Modifier.PUBLIC)
        .addModifiers(Modifier.STATIC)
        .addCode("if (alert != null) {\n"
            + "      alert.show();\n"
            + "    } else {\n"
            + methodName
            + "(mcontext);\n"
            + "alert.show();"
            + "    }")
        .returns(void.class)
        .build();

    FieldSpec android = FieldSpec.builder(context, "mcontext")
        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
        .initializer("$T.getMcontext()", YDialogBinder)
        .build();

    TypeSpec.Builder builder = TypeSpec.classBuilder(varName.getDeclaredName() + "$YDialog");
    builder.addField(android)
        .addField(alertDialogClass, "alert", Modifier.PRIVATE, Modifier.STATIC)
        .addModifiers(Modifier.PUBLIC)
        .addMethod(methodSpec)

        .addMethod(show);
    typeSpec = builder.build();
    JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
  }
}
