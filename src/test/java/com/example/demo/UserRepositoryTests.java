package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;

	@Autowired
	private MeetingRepository repo_meeting;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateUser() {
		User user = new User();
		user.setEmail("Zakaria1337@gmail.com");
		user.setPassword("1234");
		user.setFirstName("Zakaria");
		user.setLastName("Mowafek");

		User savedUser = repo.save(user);
		User existUser = entityManager.find(User.class, savedUser.getId());

		assertThat(existUser.getEmail()).isEqualTo(user.getEmail());
	}

	@Test
	public void testCreateMeeting() {
		Meeting meeting = new Meeting();
		meeting.setCreator("Zakaria");
		meeting.setMeetingWith("Zakaria1337@gmail.com");
		meeting.setStartTime("1234");
		meeting.setEndTime("ZM1337");

		Meeting savedUser = repo_meeting.save(meeting);
		Meeting existUser = entityManager.find(Meeting.class, savedUser.getId());

		assertThat(existUser.getEndTime()).isEqualTo(meeting.getEndTime());
	}

}
