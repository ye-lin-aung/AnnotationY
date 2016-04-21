package com.annotationapi;

public @interface Dialog {
  Class<?> clazz();

  String title() default "";

  String type() default DialogConstants.INFORMATION_DIALOG;

  String message() default "Hey! Check this out";
}
