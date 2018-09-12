package com.quakearts.demo.rest.beans;

import java.text.MessageFormat;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

import com.quakearts.demo.Main;

@Interceptor @Timed
@Priority(Interceptor.Priority.APPLICATION)
public class TimedInterceptor {
	private static final Logger log = Main.LOGGER;

	@AroundInvoke
	public Object log(InvocationContext context) throws Exception {
		long start = System.currentTimeMillis();
		Object object = context.proceed();
		log.info(MessageFormat.format("Processed {0} in {1, time, ss.S} seconds", context.getMethod(), System.currentTimeMillis()-start));		
		return object;
	}
}
