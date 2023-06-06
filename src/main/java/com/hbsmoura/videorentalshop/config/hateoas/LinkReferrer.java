package com.hbsmoura.videorentalshop.config.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class LinkReferrer {

    private LinkReferrer() {}

    public static <T extends RepresentationModel<? extends T>> T doRefer(T obj) {
        HateoasModel hateoasModelAnnotation = obj.getClass().getAnnotation(HateoasModel.class);

        if (hateoasModelAnnotation == null) return obj;

        Class<?> controller = hateoasModelAnnotation.controller();
        Field identityField;

        Optional<Field> idFieldOptional = Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(HateoasIdentity.class) != null)
                .findAny();

        if (idFieldOptional.isEmpty()) return obj;

        identityField = idFieldOptional.get();

        Object identity = null;
        try {
            identityField.setAccessible(true);
            identity = identityField.get(obj);
        } catch (IllegalAccessException e) {
            return obj;
        }

        List<Method> chosenMethods = Arrays.stream(controller.getDeclaredMethods())
                .filter(m -> m.getAnnotation(HateoasLink.class) != null).toList();

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
    
    private static boolean hasArgAsUniqueParameter(Method method, String arg) {
        return Arrays.stream(method.getParameters())
                .anyMatch(p -> p.getName().equals(arg))
                && method.getParameters().length == 1;
    }
}
