package com.hbsmoura.videorentalshop.config.aspects;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.hbsmoura.videorentalshop")
public class AspectConfig {}
