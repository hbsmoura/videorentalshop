package com.hbsmoura.videorentalshop.config.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class LinkReferrer {

    private LinkReferrer() {}

    public static <T extends RepresentationModel<? extends T>> T doRefer(T obj, boolean idOnly) {
        HateoasModel hateoasModelAnnotation = obj.getClass().getAnnotation(HateoasModel.class);

        if (hateoasModelAnnotation == null) return obj;

        Class<?> controller = hateoasModelAnnotation.controller();
        Field identityField;

        Optional<Field> idFieldOptional = Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(HateoasIdentity.class) != null)
                .findAny();

        if (idFieldOptional.isEmpty()) return obj;

        identityField = idFieldOptional.get();

        String identityFieldName = identityField.getName();

        Object identity;

        try {
            identity = obj.getClass()
                    .getDeclaredMethod("get" +
                            identityFieldName.substring(0, 1).toUpperCase() + identityFieldName.substring(1)
                    ).invoke(obj);
        } catch (Exception e) {
            return obj;
        }


        List<Method> chosenMethods = Arrays.stream(controller.getDeclaredMethods())
                .filter(m -> m.getAnnotation(HateoasLink.class) != null).toList();

        if (idOnly) {
            Optional<Method> optionalMethod = chosenMethods.stream()
                    .filter(method -> method.getAnnotation(HateoasLink.class).selfRel())
                    .findAny();

            if (optionalMethod.isEmpty()) return obj;

            chosenMethods = Collections.singletonList(optionalMethod.get());

        }

        obj.removeLinks();

        for (Method method : chosenMethods) {
            HateoasLink linkAnnotation = method.getAnnotation(HateoasLink.class);

            WebMvcLinkBuilder linkBuilder = hasArgAsUniqueParameter(method, identityField.getName()) ?
                    linkTo(controller, method, identity) :
                    linkTo(controller, method);

            Link link = linkAnnotation.selfRel() ? linkBuilder.withSelfRel() :
                    linkBuilder.withRel(linkAnnotation.relation());
            link.withType(linkAnnotation.requestType());

            obj.add(link);
        }

        return obj;
    }

    public static <T extends RepresentationModel<? extends T>> T doRefer(T obj){
        return doRefer(obj, false);
    }

    private static boolean hasArgAsUniqueParameter(Method method, String arg) {
        return Arrays.stream(method.getParameters())
                .anyMatch(p -> p.getName().equals(arg))
                && method.getParameters().length == 1;
    }
}
