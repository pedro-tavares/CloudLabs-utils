package com.cloudlabs.server.dehibernator;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* Dehibernate Aspect
* 
* @author pedro-tavares
* @since Jan 2015  
*/
@Component
@Aspect
public class DehibernateAdvice {

	private Logger LOG = LoggerFactory.getLogger(DehibernateAdvice.class);

	@Autowired
	DehibernateService dehibernateService;

//	@AfterReturning(pointcut = "@annotation(annotation(com.cloudlabs.server.dehibernator.Dehibernate)", returning = "value")
	public void advise(Object value) {
		LOG.info("Advise @AfterReturning...");

		LOG.info("Advise before clean... value:" + value);
		dehibernateService.clean(value);
		LOG.info("Advised after clean... value:" + value);
	}

	@Around("@annotation(com.cloudlabs.server.dehibernator.Dehibernate)")
	public Object advise(ProceedingJoinPoint joinPoint) throws Throwable {
		/*
		LOG.info("Around DehibernateAdvice is running... advised method:{}, advised arguments:{}", 
				joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		*/
		Object value = joinPoint.proceed();
		
//		LOG.info("Around before clean... value:{}", value);
		dehibernateService.clean(value);		
//		LOG.info("Around after clean... value:{}", value);
		LOG.debug("Around after clean... Complete!");
		
		return value;
	}
}
