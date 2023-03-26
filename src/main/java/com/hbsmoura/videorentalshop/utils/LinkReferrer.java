package com.hbsmoura.videorentalshop.utils;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class LinkReferrer {

    private LinkReferrer() {}

    public static <T extends RepresentationModel<? extends T>> T doRefer(T obj, UUID objectId, Class<?> controller, Method ...chosenMethods) {
        obj.removeLinks();

        if (chosenMethods.length == 0) chosenMethods = controller.getDeclaredMethods();

        for (Method method : chosenMethods) {
            if (method == null) continue;
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation a : annotations) {
                RequestMethod requestMethod = verifyRequestMappingAnnotation(method, a);
                if (requestMethod != null) {
                    if (Arrays.stream(method.getParameters()).anyMatch(p -> p.getName().equals("id"))) {
                        obj.add(linkTo(controller, method, objectId)
                                .withRel(camelCaseToSentenceCase(method.getName()))
                                .withType(requestMethod.name()));
                    } else {
                        obj.add(linkTo(controller, method)
                                .withRel(camelCaseToSentenceCase(method.getName()))
                                .withType(requestMethod.name()));
                    }
                }

            }
        }

        return obj;
    }

    public static Method extractMethod(Class<?> clazz, String name, Class<?> parametersType) {
        try {
            return clazz.getDeclaredMethod(name, parametersType);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static RequestMethod verifyRequestMappingAnnotation(Method method, Annotation annotation) {
        if (annotation instanceof RequestMapping)
            return method.getAnnotation(RequestMapping.class).method()[0];

        if (annotation instanceof GetMapping || annotation instanceof PostMapping || annotation instanceof PutMapping ||
                annotation instanceof DeleteMapping ||annotation instanceof PatchMapping
        )
            return annotation.annotationType().getAnnotation(RequestMapping.class).method()[0];
        return null;
    }

    private static String camelCaseToSentenceCase(String text) {
        if (!text.equals("")) return text.replaceAll("([A-Z])", " $1").toLowerCase();
        return "";
    }
}
