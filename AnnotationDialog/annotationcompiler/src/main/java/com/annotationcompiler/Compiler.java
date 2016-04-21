package com.annotationcompiler;

import com.annotationapi.AnnotationUtils;
import com.annotationapi.Dialog;
import com.google.auto.service.AutoService;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class) public class Compiler extends AbstractProcessor {
  private Filer filer;
  private Elements elementsUtils;
  private Messager messager;
  private Types types;
  private Map<String, AnnotatedGroupDialog> map = new LinkedHashMap<>();

  @Override public Set<String> getSupportedAnnotationTypes() {
    return AnnotationUtils.getSupportedAnnotations();
  }

  @Override public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    filer = processingEnv.getFiler();
    elementsUtils = processingEnv.getElementUtils();
    messager = processingEnv.getMessager();
    types = processingEnv.getTypeUtils();
  }

  @Override public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    try {

      for (Element element : roundEnv.getElementsAnnotatedWith(Dialog.class)) {
        if (element.getKind() != ElementKind.CLASS) {
          throw new ProcessingException(element, "Only Class can be annotated with @%s",
              Dialog.class.getSimpleName());
        } else {
          TypeElement typeElement = (TypeElement) element;
          AnnotatedDialog annotatedDialog = new AnnotatedDialog(typeElement);
          checkValidDialog(annotatedDialog);
          AnnotatedGroupDialog annotatedGroupDialog =
              map.get(annotatedDialog.getQuilifiedSimpleClassName());

          if (annotatedGroupDialog == null) {
            String qulifiedClassName = annotatedDialog.getQuilifiedSimpleClassName();
            annotatedGroupDialog = new AnnotatedGroupDialog(qulifiedClassName);
            map.put(qulifiedClassName, annotatedGroupDialog);
          }
          annotatedGroupDialog.addToMap(annotatedDialog);
        }
        for (AnnotatedGroupDialog factoryClass : map.values()) {
          factoryClass.generateCode(elementsUtils, filer);
        }
        map.clear();
      }
    } catch (ProcessingException processingException) {
      printErrorStackTrace(processingException.getElement(), processingException.getMessage());
    } catch (IOException ioException) {
      printErrorStackTrace(null, ioException.getMessage());
    }
    return true;
  }

  private void printErrorStackTrace(Element element, String message, Objects... args) {
    messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args));
  }

  private void checkValidDialog(AnnotatedDialog annotatedDialog) throws ProcessingException {
    TypeElement typeElement = annotatedDialog.getTypeElement();

    if (!typeElement.getModifiers().contains(Modifier.PUBLIC)) {
      throw new ProcessingException(typeElement, "The class %s is not public.",
          typeElement.getQualifiedName().toString());
    }

    // Check if it's an abstract class
    if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
      throw new ProcessingException(typeElement,
          "The class %s is abstract. You can't annotate abstract classes with @%",
          typeElement.getQualifiedName().toString(), Dialog.class.getSimpleName());
    }
  }
}
