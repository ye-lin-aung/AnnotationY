package com.annotationapi;

/**
 * Created by user on 4/23/16.
 */
public @interface VarDialog {


  String title() default "";

  String type() default DialogTypes.INFORMATION_DIALOG;

  String message() default "Hey! Check this out";

  boolean touchOutsideClosed() default true;
}
