package com.example.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.demo.controller..*(..))") // 拦截 Controller 包下的所有方法
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = Instant.now().toEpochMilli();

        logger.info("=== Request Start ===");
        logger.info("Method: {}", joinPoint.getSignature().toString());
        logger.info("Args: {}", Arrays.toString(joinPoint.getArgs()));
        logger.info("Start Time: {}", startTime);

        Object result;
        try {
            result = joinPoint.proceed(); // 调用目标方法
        } catch (Throwable throwable) {
            logger.error("Exception: {}", throwable.getMessage());
            throw throwable;
        }

        long endTime = Instant.now().toEpochMilli();
        logger.info("Response: {}", result);
        logger.info("End Time: {}", endTime);
        logger.info("Duration: {}ms", (endTime - startTime));
        logger.info("====================");

        return result;
    }
}