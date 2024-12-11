package com.example.demo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController // A convenience annotation that is itself annotated with @Controller and
				// @ResponseBody. Types that carry this annotation are treated as controllers
				// where @RequestMapping methods assume @ResponseBody semantics by default.

public class CalendarController {

	@Autowired
	EventRepository er;

	@RequestMapping("/api")
	@ResponseBody
	String home() {
		return "Welcome!";
	}

	@GetMapping("/api/events")
	@JsonSerialize(using = LocalDateTimeSerializer.class) // Annotation used for configuring serialization aspects, by
															// attachingto "getter" methods or fields, or to value
															// classes.When annotating value classes, configuration is
															// used for instancesof the value class but can be
															// overridden by more specific annotations(ones that attach
															// to methods or fields).
	Iterable<Event> events(@RequestParam("start") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end) {
		return er.findBetween(start, end);
	}

	@PostMapping("/api/events/create")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@Transactional
	Event createEvent(@RequestBody EventCreateParams params) {

		Event e = new Event();
		e.setStart(params.start);
		e.setEnd(params.end);
		e.setText(params.text);
		er.save(e);

		return e;
	}

	@PostMapping("/api/events/move")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@Transactional
	Event moveEvent(@RequestBody EventMoveParams params) {

		Event e = er.findById(params.id).get();
		e.setStart(params.start);
		e.setEnd(params.end);
		er.save(e);

		return e;
	}

	@PostMapping("/api/events/setColor")
	@JsonSerialize(using = LocalDateTimeSerializer.class) // Annotation used for configuring serialization aspects, by
															// attachingto "getter" methods or fields, or to value
															// classes.When annotating value classes, configuration is
															// used for instancesof the value class but can be
															// overridden by more specific annotations(ones that attach
															// to methods or fields).
	@Transactional
	Event setColor(@RequestBody SetColorParams params) {

		Event e = er.findById(params.id).get();
		e.setColor(params.color);
		er.save(e);

		return e;
	}

	@PostMapping("/api/events/delete")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@Transactional
	EventDeleteResponse deleteEvent(@RequestBody EventDeleteParams params) {

		er.deleteById(params.id);

		return new EventDeleteResponse() {
			{
				message = "Deleted";
			}
		};
	}

	public void getAllEvents() {
		Iterable<Event> events = er.findAll();
		events.forEach((element) -> {
			System.out.println(element.getText());
		});
	}

	public static class EventDeleteParams {
		public Long id;
	}

	public static class EventDeleteResponse {
		public String message;
	}

	public static class EventCreateParams {
		public LocalDateTime start;
		public LocalDateTime end;
		public String text;
	}

	public static class EventMoveParams {
		public Long id;
		public LocalDateTime start;
		public LocalDateTime end;
	}

	public static class SetColorParams {
		public Long id;
		public String color;
	}

}