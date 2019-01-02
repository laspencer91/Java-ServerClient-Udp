package shared.annotations;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageDataAnnotationProcessor extends AbstractProcessor {



    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, String> annotatedElements = new HashMap<>();

        for (Element element : roundEnv.getElementsAnnotatedWith(MessageData.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                System.out.println(Diagnostic.Kind.ERROR + " Can only be applied to a class");
                return true;
            }

            TypeElement typeElement = (TypeElement) element;
            annotatedElements.put(typeElement.getSimpleName().toString(),
                                  processingEnv.getElementUtils().getPackageOf(typeElement).toString());

            String activityName = typeElement.getSimpleName().toString();
            String packageName  = processingEnv.getElementUtils().getPackageOf(typeElement).toString();

            ClassName activityClass = ClassName.get(packageName, activityName);
            MethodSpec noargsConstructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build();

        }



        for (Map.Entry<String, String> element : annotatedElements.entrySet()){
            String activityName = element.getKey();
            String packageName  = element.getValue();

            ClassName activityClass = ClassName.get(packageName, activityName);
            MethodSpec noargsConstructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build();


        }

            return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }
}
