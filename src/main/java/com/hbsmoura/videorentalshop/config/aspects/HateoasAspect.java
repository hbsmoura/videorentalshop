package com.hbsmoura.videorentalshop.config.aspects;

import com.hbsmoura.videorentalshop.config.hateoas.LinkReferrer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HateoasAspect {

    @Around("execution(* com.hbsmoura.videorentalshop.service.*.*(..))")
    Object referLinks(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();

        if (proceed instanceof RepresentationModel<?>) {
            return LinkReferrer.doRefer((RepresentationModel<?>)proceed);
        }

        if (proceed instanceof Page<?>){
            return ((Page<RepresentationModel<?>>) proceed).map(rm -> LinkReferrer.doRefer(rm, true));
        }

        return proceed;
    }
}
