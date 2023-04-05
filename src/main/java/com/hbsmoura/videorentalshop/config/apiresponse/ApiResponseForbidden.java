package com.hbsmoura.videorentalshop.config.apiresponse;

import com.hbsmoura.videorentalshop.config.exceptionhandling.ApiErrorResponse;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApiResponse(
        responseCode = "403",
        description = "FORBIDDEN",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
)
public @interface ApiResponseForbidden {
    @AliasFor(annotation = ApiResponse.class)
    Header[] headers() default {};

    @AliasFor(annotation = ApiResponse.class)
    Link[] links() default {};

    @AliasFor(annotation = ApiResponse.class)
    Extension[] extensions() default {};

    @AliasFor(annotation = ApiResponse.class)
    String ref() default "";

    @AliasFor(annotation = ApiResponse.class)
    boolean useReturnTypeSchema() default false;
}
