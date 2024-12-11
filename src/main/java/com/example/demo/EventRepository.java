package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
	@Query("from Event e where not(e.end < :from or e.start > :to)")
	public List<Event> findBetween(@Param("from") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
			@Param("to") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end);

	Iterable<Event> findEventByStartEquals(LocalDateTime date);

	List<Event> findAllByStartEquals(LocalDateTime today);
}

//Central repository marker interface. Captures the domain type to manage as well as the domain type's id type. 
//Generalpurpose is to hold type information as well as being able to discover interfaces that extend this one during class path scanning for easy Spring bean creation. 