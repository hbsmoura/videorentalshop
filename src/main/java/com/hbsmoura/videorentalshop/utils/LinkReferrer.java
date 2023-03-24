package com.hbsmoura.videorentalshop.utils;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class LinkReferrer {

    public static <T extends RepresentationModel> T doRefer(RepresentationModel obj, Class<T> objType, Class controller, UUID objectId) {
        obj.removeLinks();
        Method[] controllerMethods = controller.getDeclaredMethods();
        for (Method method : controllerMethods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation a : annotations) {
                RequestMethod requestMethod = null;
                if (a instanceof RequestMapping)
                    requestMethod = method.getAnnotation(RequestMapping.class).method()[0];

                if (a instanceof GetMapping || a instanceof PostMapping || a instanceof PutMapping ||
                        a instanceof DeleteMapping ||a instanceof PatchMapping
                )
                    requestMethod = a.annotationType().getAnnotation(RequestMapping.class).method()[0];

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

        return objType.cast(obj);
    }

    private static String camelCaseToSentenceCase(String text) {
        if (!text.equals("")) return text.replaceAll("([A-Z])", " $1").toLowerCase();
        return "";
    }
}
