package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.email = ?1") // Annotation to declare finder queries directly on repository
														// methods.

	User findByEmail(String email);
}
