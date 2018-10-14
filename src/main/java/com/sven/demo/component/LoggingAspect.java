package com.sven.demo.component;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect for logging execution of service and repository Spring components.
 */
@Aspect
public class LoggingAspect
{

	private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);

	/**
	 * Pointcut that matches all services and Web REST endpoints.
	 */
	@Pointcut("within(@org.springframework.stereotype.Service *)"
			+ " || within(@org.springframework.stereotype.Controller *)"
			+ " || within(@org.springframework.web.bind.annotation.RestController *)")
	public void springBeanPointcut()
	{
		// Method is empty as this is just a Pointcut, the implementations are
		// in the advices.
	}

	/**
	 * Pointcut that matches all Spring beans in the application's main
	 * packages.
	 */
	@Pointcut("within(com.sven.demo.service..*)" + " || within(com.sven.demo.component..*)")
	public void applicationPackagePointcut()
	{
		// Method is empty as this is just a Pointcut, the implementations are
		// in the advices.
	}

	/**
	 * Advice that logs methods throwing exceptions.
	 *
	 * @param joinPoint
	 *            join point for advice
	 * @param e
	 *            exception
	 */
	@AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
	public void logAfterThrowing(final JoinPoint joinPoint, final Throwable e)
	{

		LOG.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'",
				joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
				e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);

	}

	/**
	 * Advice that logs when a method is entered and exited.
	 *
	 * @param joinPoint
	 *            join point for advice
	 * @return result
	 * @throws Throwable
	 *             throws IllegalArgumentException
	 */
	@Around("applicationPackagePointcut() && springBeanPointcut() && !execution(public new(..))")
	public Object logAround(final ProceedingJoinPoint joinPoint) throws Throwable
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Enter: {}.{}()", joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName());
		}
		try
		{
			Object result = joinPoint.proceed();
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
						joinPoint.getSignature().getName(), result);
			}
			return result;
		} catch (IllegalArgumentException e)
		{
			LOG.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

			throw e;
		}
	}
}