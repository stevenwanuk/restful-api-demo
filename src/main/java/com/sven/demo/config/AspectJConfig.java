package com.sven.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.sven.demo.component.LoggingAspect;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AspectJConfig
{
	public LoggingAspect loggingAspect()
	{
		return new LoggingAspect();
	}
}
