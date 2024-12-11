package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//To enable support for scheduling tasks and the @Scheduled annotation in Spring, we can use the Java enable-style annotation:
@Configuration
@EnableScheduling
public class SpringConfig {
	@Autowired
	EventRepository er;

	@Autowired
	EmailNotification emailNotification;

// the Spring @Scheduled annotation can be used to configure and schedule tasks.
	@Scheduled(fixedDelay = 60000) // milliseconds interval
	public void scheduleFixedDelayTask() {

		LocalDate toLocalDate = LocalDateTime.now().toLocalDate();
		String str = toLocalDate.toString() + " 00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime todayDate = LocalDateTime.parse(str, formatter);

		List<Event> events = er.findAllByStartEquals(todayDate);
		List<String> messages = new ArrayList<>();
		String[] arr = new String[events.size()];

		if (events.size() > 0) {
			for (int i = 0; i < arr.length; i++) {
				arr[i] = events.get(i).getText();
			}

			for (int i = 0; i < arr.length; i++) {
				if (i > 0) {
					if (arr[i].equalsIgnoreCase(arr[i - 1])) {
						continue;
					} else {
						messages.add(arr[i]);
					}
				} else {
					messages.add(arr[i]);
				}
			}
			emailNotification.sendMail("SpringBootMailReceiver@protonmail.com", messages.toString());
		}
	}

	public Iterable<Event> getAllEvents() { // Implementing this interface allows an object to be the target of the
											// enhanced for statement (sometimes called the "for-each loop" statement).
		Iterable<Event> events = er.findAll();
		events.forEach((element) -> {
			if (element.getStart().equals(LocalDateTime.now())) {
				System.out.println("Today is the event");
			}
			System.out.println(element.getText());
		});
		return events;
	}
}