package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> { // Central repository marker interface.
																			// Captures the domain type to manage as
																			// well as the domain type's id type.
																			// Generalpurpose is to hold type
																			// information as well as being able to
																			// discover interfaces that extend this one
																			// duringclasspath scanning for easy Spring
																			// bean creation.

}
