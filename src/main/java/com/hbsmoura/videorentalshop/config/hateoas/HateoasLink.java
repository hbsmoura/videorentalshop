package com.hbsmoura.videorentalshop.config.hateoas;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HateoasLink {
    boolean selfRel() default false;
    String relation() default "self";
    String requestType() default "GET";
}
