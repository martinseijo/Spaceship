package com.martinseijo.spaceship.infrastructure.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SpaceshipAspect {

    @Pointcut("execution(* com.martinseijo.spaceship.infrastructure.web.SpaceshipController.getById(..)) && args(id)")
    public void getByIdMethod(Long id) {}

    @Before("getByIdMethod(id)")
    public void logIfNegativeId(Long id) {
        if (id < 0) {
            log.warn("Requested spaceship with negative ID: {}", id);
        }
    }
}
