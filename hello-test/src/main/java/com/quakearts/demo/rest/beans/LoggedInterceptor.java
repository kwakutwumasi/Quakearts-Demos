package com.quakearts.demo.rest.beans;

import java.text.MessageFormat;
import java.util.Arrays;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

import com.quakearts.demo.Main;

@Interceptor @Logged
@Priority(Interceptor.Priority.APPLICATION)
public class LoggedInterceptor {
	private static final Logger log = Main.LOGGER;
	
	@AroundInvoke
	public Object log(InvocationContext context) throws Exception {
		log.info(MessageFormat.format("Accessed {0} with parameters {1}", context.getMethod(), Arrays.toString(context.getParameters())));
		return context.proceed();
	}
}
