package com.cloudlabs.server.advice;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
* Performance Measuring Aspect
* 
* @author pedro-tavares
* @since Jan 2015  
*/
@Component
@Aspect
public class PerformanceMeasuredAdvice {

	private Logger LOG = LoggerFactory.getLogger(PerformanceMeasuredAdvice.class);

	@Around("@annotation(com.cloudlabs.server.advice.PerformanceMeasured)")
	public Object advise(ProceedingJoinPoint joinPoint) throws Throwable {

		LOG.debug("Around PerformanceMeasuredAdvice is running... advised method:{}, advised arguments:{}", 
				joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Object value = joinPoint.proceed();

		stopWatch.stop();

		LOG.info("\n{} time:{}", joinPoint.getSignature().getDeclaringTypeName() + "."
				+ joinPoint.getSignature().getName(), stopWatch.getTotalTimeSeconds() + "(s)");

		return value;
	}
}
