package com.sven.demo.model;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;

	public User()
	{

	}

	@Generated("SparkTools")
	private User(Builder builder)
	{
		this.id = builder.id;
		this.name = builder.name;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Creates builder to build {@link User}.
	 * 
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder()
	{
		return new Builder();
	}

	/**
	 * Builder to build {@link User}.
	 */
	@Generated("SparkTools")
	public static final class Builder
	{
		private long id;
		private String name;

		private Builder()
		{
		}

		public Builder withId(long id)
		{
			this.id = id;
			return this;
		}

		public Builder withName(String name)
		{
			this.name = name;
			return this;
		}

		public User build()
		{
			return new User(this);
		}
	}

}
