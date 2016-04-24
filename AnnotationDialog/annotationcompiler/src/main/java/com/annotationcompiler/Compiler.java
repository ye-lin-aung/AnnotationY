package com.annotationcompiler;

import com.annotationapi.AnnotationUtils;
import com.annotationapi.Dialog;
import com.annotationapi.VarDialog;
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
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class) public class Compiler extends AbstractProcessor {
  private Filer filer;
  private Elements elementsUtils;
  private Messager messager;
  private Types types;
  private Map<String, AnnotatedGroupVariableDialog> map = new LinkedHashMap<>();

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
      Set<VariableElement> fields =
          ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(VarDialog.class));
      for (VariableElement variableElement : fields) {
        if (variableElement.getKind() != ElementKind.FIELD) {
          throw new ProcessingException(variableElement, "Only Variables can be annotated with @%s",
              VarDialog.class.getCanonicalName());
        } else {
          AnnotatedVariableDialog annotatedVariableDialog =
              new AnnotatedVariableDialog(variableElement);
          AnnotatedGroupVariableDialog annotatedGroupDialog =
              map.get(annotatedVariableDialog.getQuilifiedSimpleClassName());
          if (annotatedGroupDialog == null) {
            String qulifiedClassName = annotatedVariableDialog.getQuilifiedSimpleClassName();
            annotatedGroupDialog =
                new AnnotatedGroupVariableDialog(qulifiedClassName, annotatedVariableDialog);
            map.put(qulifiedClassName, annotatedGroupDialog);
          }
          annotatedGroupDialog.addToMap(annotatedVariableDialog);

          for (AnnotatedGroupVariableDialog factoryClass : map.values()) {

            factoryClass.generateCode(elementsUtils, filer);
          }
          map.clear();
        }
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
