package com.annotationcompiler;

import com.annotationapi.VarDialog;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

/**
 * Created by user on 4/23/16.
 */
public class AnnotatedVariableDialog {
  private VariableElement variableElement;
  private String declaredName;
  private Class<?> clazzType;
  private String declaredFieldClassName;
  private String title;
  private String message;
  private String type;
  private String quilifiedSimpleClassName;
  private String simpleClassName;
  private boolean touchOutsideClosed;

  public AnnotatedVariableDialog(VariableElement variableElement) {
    this.variableElement = variableElement;
    VarDialog varDialog = variableElement.getAnnotation(VarDialog.class);
    message = varDialog.message();
    title = varDialog.title();
    type = varDialog.type();
    touchOutsideClosed = varDialog.touchOutsideClosed();
    declaredName = variableElement.getSimpleName().toString();
    if (title == null || title.length() <= 0) {
      title = "Dialog";
    }
    try {
      clazzType = varDialog.getClass();
      quilifiedSimpleClassName = clazzType.getCanonicalName();
      simpleClassName = clazzType.getSimpleName();
      TypeMirror typeMirror = variableElement.asType();
      declaredFieldClassName = typeMirror.toString();
    } catch (MirroredTypeException mirror) {
      DeclaredType declaredType = (DeclaredType) mirror.getTypeMirror();
      declaredFieldClassName = declaredType.toString();
      TypeElement classTypeElement = (TypeElement) declaredType.asElement();
      quilifiedSimpleClassName = classTypeElement.getQualifiedName().toString();
      simpleClassName = classTypeElement.getSimpleName().toString();
      mirror.printStackTrace();
    }
  }

  public Class<?> getClazzType() {
    return clazzType;
  }

  public void setClazzType(Class<?> clazzType) {
    this.clazzType = clazzType;
  }

  public String getDeclaredFieldClassName() {
    return declaredFieldClassName;
  }

  public void setDeclaredFieldClassName(String declaredFieldClassName) {
    this.declaredFieldClassName = declaredFieldClassName;
  }

  public String getDeclaredName() {
    return declaredName;
  }

  public void setDeclaredName(String declaredName) {
    this.declaredName = declaredName;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getQuilifiedSimpleClassName() {
    return quilifiedSimpleClassName;
  }

  public void setQuilifiedSimpleClassName(String quilifiedSimpleClassName) {
    this.quilifiedSimpleClassName = quilifiedSimpleClassName;
  }

  public String getSimpleClassName() {
    return simpleClassName;
  }

  public void setSimpleClassName(String simpleClassName) {
    this.simpleClassName = simpleClassName;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public VariableElement getVariableElement() {
    return variableElement;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setVariableElement(VariableElement variableElement) {
    this.variableElement = variableElement;
  }

  public boolean isTouchOutsideClosed() {
    return touchOutsideClosed;
  }

  public void setTouchOutsideClosed(boolean touchOutsideClosed) {
    this.touchOutsideClosed = touchOutsideClosed;
  }
}
