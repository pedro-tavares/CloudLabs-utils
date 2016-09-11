package com.cloudlabs.server.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
* Pick up Spring Context as a Service 
* 
* @author pedro-tavares
* @since Feb 2015  
*/
@Service
public class ContextAwareness implements ApplicationContextAware {
	
	private Logger LOG = LoggerFactory.getLogger(ContextAwareness.class);
	
	protected ApplicationContext applicationContext;
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		LOG.info("setting ApplicationContext...\n{}", applicationContext.getEnvironment());
	}	

}
