package com.annotationcompiler;

import javax.lang.model.element.Element;

/**
 * Created by user on 4/20/16.
 */
public class ProcessingException extends Exception {
  private Element element;

  public ProcessingException(Element element, String type, Object... args) {
    super(String.format(type, args));
    this.element = element;

  }
  public Element getElement() {
    return element;
  }
}
