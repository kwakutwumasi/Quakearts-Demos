package com.quakearts.webtools.demo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Priority;
import javax.faces.context.FacesContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class FacesLoggingInterceptor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3898973732888797449L;
	static final List<ActionLog> ACTION_LOGS = new CopyOnWriteArrayList<>();
	
	@AroundInvoke
	public Object log(InvocationContext context) throws Exception {
		String host = "unknown";
		if(FacesContext.getCurrentInstance()!=null) {
			host = ((HttpServletRequest)FacesContext.getCurrentInstance()
					.getExternalContext()
					.getRequest()).getRemoteHost();
			if(host==null)
				host = "undetermined";
		}
		
		ACTION_LOGS.add(new ActionLog().withAction(context.getMethod().getName())
				.withActionDate(new Date())
				.withActionHost(host));
		return context.proceed();
	}
}
