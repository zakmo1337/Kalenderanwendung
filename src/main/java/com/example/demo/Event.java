package com.example.demo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Provides for the specification of generation strategies for
														// thevalues of primary keys. The GeneratedValue annotationmay
														// be applied to a primary key property or field of an entity
														// ormapped superclass in conjunction with the Id annotation.The
														// use of the GeneratedValue annotation is onlyrequired to be
														// supported for simple primary keys. Use of the GeneratedValue
														// annotation is not supported for derivedprimary keys.

	Long id;

	String text;

	LocalDateTime start;

	LocalDateTime end;

	String color;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
