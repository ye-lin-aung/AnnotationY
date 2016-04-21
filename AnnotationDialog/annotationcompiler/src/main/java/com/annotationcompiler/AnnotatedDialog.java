package com.annotationcompiler;

import com.annotationapi.Dialog;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * Created by user on 4/20/16.
 */
public class AnnotatedDialog {
  private TypeElement typeElement;
  private Class<?> clazzType;
  private String title;
  private String message;
  private String quilifiedSimpleClassName;
  private String simpleClassName;
  private String type;

  public AnnotatedDialog(TypeElement typeElement) throws IllegalArgumentException {
    this.typeElement = typeElement;
    Dialog dialog = typeElement.getAnnotation(Dialog.class);

    type = dialog.type();
    title = dialog.title();
    message = dialog.message();
    if (title == null || title.length() <= 0) {
      title = dialog.type();
    }
    try {
      clazzType = dialog.clazz();
      quilifiedSimpleClassName = clazzType.getCanonicalName();
      simpleClassName = clazzType.getSimpleName();
    } catch (MirroredTypeException mirrorException) {
      DeclaredType declareType = (DeclaredType) mirrorException.getTypeMirror();
      TypeElement tpElement = (TypeElement) declareType.asElement();
      quilifiedSimpleClassName = tpElement.getQualifiedName().toString();
      simpleClassName = tpElement.getSimpleName().toString();
    }
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Class<?> getClazzType() {
    return clazzType;
  }

  public void setClazzType(Class<?> clazzType) {
    this.clazzType = clazzType;
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

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public void setTypeElement(TypeElement typeElement) {
    this.typeElement = typeElement;
  }
}
