package com.mybatis.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 系统日志类，主要记录系统中日志情况。
 * 1：AOP拦截日志
 * 
 * @author wangzh
 */
@Aspect
@Component
public class AspectService {

	/**
	 * log对象声明
	 */
	private final Log logger = LogFactory.getLog(AspectService.class);

	@Pointcut("execution(* com.mybatis.main.rest.*.*(..))")
	public void pointcut() {
		// ignore
	}

	@Before("pointcut()")
	public void beginTransaction() {
		logger.info("before beginTransaction");
	}

	@After("pointcut()")
	public void commit() {
		logger.info("after commit");
	}

}
