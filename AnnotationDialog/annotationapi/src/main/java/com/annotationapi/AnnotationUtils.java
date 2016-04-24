package com.annotationapi;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by user on 4/20/16.
 */
public class AnnotationUtils {
  protected static Set<String> supportedAnnotations = new HashSet<>();

  public static Set<String> getSupportedAnnotations() {
    supportedAnnotations.add(VarDialog.class.getCanonicalName());
    supportedAnnotations.add(Dialog.class.getCanonicalName());
    return supportedAnnotations;
  }
}
