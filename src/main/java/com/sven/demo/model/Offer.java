package com.sven.demo.model;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sven.demo.utils.Constants;

@Entity
public class Offer
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String detail;
	@JsonFormat(pattern = Constants.DATE_FORMAT_PATTERN)
	private Date createDate;
	@JsonFormat(pattern = Constants.DATE_FORMAT_PATTERN)
	private Date expiryDate;
	@ManyToOne
	private User owner;
	private boolean isDeleted;

	public Offer()
	{

	}

	@Generated("SparkTools")
	private Offer(Builder builder)
	{
		this.id = builder.id;
		this.name = builder.name;
		this.detail = builder.detail;
		this.createDate = builder.createDate;
		this.expiryDate = builder.expiryDate;
		this.owner = builder.owner;
		this.isDeleted = builder.isDeleted;
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

	public String getDetail()
	{
		return detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public Date getExpiryDate()
	{
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate)
	{
		this.expiryDate = expiryDate;
	}

	public User getOwner()
	{
		return owner;
	}

	public void setOwner(User owner)
	{
		this.owner = owner;
	}

	public boolean isDeleted()
	{
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	/**
	 * Creates builder to build {@link Offer}.
	 * 
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder()
	{
		return new Builder();
	}

	/**
	 * Builder to build {@link Offer}.
	 */
	@Generated("SparkTools")
	public static final class Builder
	{
		private long id;
		private String name;
		private String detail;
		private Date createDate;
		private Date expiryDate;
		private User owner;
		private boolean isDeleted;

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

		public Builder withDetail(String detail)
		{
			this.detail = detail;
			return this;
		}

		public Builder withCreateDate(Date createDate)
		{
			this.createDate = createDate;
			return this;
		}

		public Builder withExpiryDate(Date expiryDate)
		{
			this.expiryDate = expiryDate;
			return this;
		}

		public Builder withOwner(User owner)
		{
			this.owner = owner;
			return this;
		}

		public Builder withIsDeleted(boolean isDeleted)
		{
			this.isDeleted = isDeleted;
			return this;
		}

		public Offer build()
		{
			return new Offer(this);
		}
	}

}
