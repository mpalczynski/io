package com.io.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class BaseEntity<K> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private K id;
	
	@Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
	
	@Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

	public K getId() {
		return id;
	}

	public void setId(K id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	@PrePersist  
	private void setCreatedAt() {
		this.createdAt = new Date();
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	@PreUpdate
	private void setUpdatedAt() {
		this.updatedAt = new Date();
	}
	
}
