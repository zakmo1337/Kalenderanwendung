package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users") // In this case, the duration between the end of the last execution and the
						// start of the next execution is fixed. The task always waits until the
						// previous one is finished.This option should be used when it’s mandatory that
						// the previous execution is completed before running again.
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // The GeneratedValue annotationmay be applied to a primary key
														// property or field of an entity ormapped superclass in
														// conjunction with the Id annotation.The use of the
														// GeneratedValue annotation is onlyrequired to be supported for
														// simple primary keys. Use of the GeneratedValue annotation is
														// not supported for derivedprimary keys.
	private Long id;

	@Column(nullable = false, unique = true, length = 45)
	private String email;

	@Column(nullable = false, length = 64) // Specifies the mapped column for a persistent property or field.If no
											// Column annotation is specified, the default values apply.
	private String password;

	@Column(nullable = false, length = 20)
	private String firstName;

	@Column(nullable = false, length = 20)
	private String lastName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
