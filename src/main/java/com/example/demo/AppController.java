package com.example.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//In the Spring Boot context, we’ll organize controller code into a controller package. 
//@GetMapping is another critical annotation used on controller methods. 
//@GetMapping designates a controller action with a URL path. For every GET request made to the provided path, the controller method will be called. 
//The path is defined with @GetMapping("pathname"). 
//If the pathname value is null, then the path used is the index path, or /.
@Controller
public class AppController {

	@Autowired // Marks a constructor, field, setter method, or config method as to be
				// autowired bySpring's dependency injection facilities. This is an alternative
				// to the JSR-330 javax.inject.Inject annotation, adding required-vs-optional
				// semantics.
	private UserRepository repo;

	@Autowired
	private MeetingRepository repo_meeting;

	@GetMapping("")
	public String viewHomePage() {
		return "home";
	}

	@GetMapping("/calendar")
	public String calendarPage() {
		return "calendar";
	}

	@GetMapping("/register")
	public String showSignUpForm(Model model) {
		model.addAttribute("user", new User());

		return "signup_form";
	}

	@GetMapping("/list_users")
	public String viewListUser(Model model) {
		List<User> listUsers = repo.findAll();
		model.addAttribute("listUsers", listUsers);

		return "users";
	}

	@GetMapping("/meetings")
	public String myMeetings(Model model) {
		List<Meeting> listMeeting = repo_meeting.findAll(); // An ordered collection (also known as a sequence). The
															// user of thisinterface has precise control over where in
															// the list each element isinserted. The user can access
															// elements by their integer index (position inthe list),
															// and search for elements in the list.
		// model.addAttribute("listMeeting", listMeeting);

		List<Meeting> newlistMeeting = new ArrayList<Meeting>();

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		for (Meeting meeting : listMeeting) {
			if (meeting.getFlag() == (long) 1
					&& (meeting.getCreator().equals(username) || meeting.getMeetingWith().equals(username))) {
				newlistMeeting.add(meeting);
			}
		}
		model.addAttribute("newlistMeeting", newlistMeeting);
		return "meetings";
	}

	@GetMapping("/meeting_invites")
	public String myMeetingInvites(Model model) {
		List<Meeting> listMeeting = repo_meeting.findAll();
		// model.addAttribute("listMeeting", listMeeting);

		List<Meeting> newlistMeeting = new ArrayList<Meeting>();

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		for (Meeting meeting : listMeeting) {
			if (meeting.getFlag() == (long) 0 && (meeting.getMeetingWith().equals(username))) {
				newlistMeeting.add(meeting);
			}
		}
		model.addAttribute("newlistMeeting", newlistMeeting);
		return "meeting_invites";
	}

	@PostMapping("/process_register") // Annotation for mapping HTTP POST requests onto specific handlermethods.
										// Specifically, @PostMapping is a composed annotation thatacts as a shortcut
										// for @RequestMapping(method = RequestMethod.POST).

	public String processRegistration(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		repo.save(user);

		return "register_success";
	}

	@GetMapping("/create_meeting")
	public String createMeeting(Model model) {
		List<User> newlistUsers = repo.findAll();
		List<User> listUsers = new ArrayList<User>();

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		for (User user : newlistUsers) {
			if (!user.getEmail().equals(username)) {
				listUsers.add(user);
			}
		}

		model.addAttribute("listUsers", listUsers);

		model.addAttribute("meeting", new Meeting());
		return "create_meeting";
	}

	@PostMapping(value = "/accept")
	public String acceptMeeting(@RequestParam("meetingId") Long meetingId, Model model) {

		Meeting meeting = repo_meeting.getById(meetingId);
		meeting.setFlag((long) 1);
		repo_meeting.save(meeting);

		return "invite_accepted";
	}

	@PostMapping(value = "/reject")
	public String rejectMeeting(@RequestParam("meetingId") Long meetingId, Model model) {

		Meeting meeting = repo_meeting.getById(meetingId);
		meeting.setFlag((long) 2);
		repo_meeting.save(meeting);

		return "invite_rejected";
	}

	@PostMapping("/meeting_register")
	public String meetingRegistration(Meeting meeting, Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		Boolean flag = false;
		String pattern = "yyyy-MM-dd'T'HH:mm";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

		meeting.setCreator(username);
		meeting.setFlag((long) 0);
		List<Meeting> listMeeting = repo_meeting.findAll();
		for (Meeting meet : listMeeting) {
			LocalDateTime meeting_startTime = LocalDateTime.parse(meeting.getStartTime(), formatter);
			LocalDateTime meet_startTime = LocalDateTime.parse(meet.getStartTime(), formatter);
			LocalDateTime meet_endTime = LocalDateTime.parse(meet.getEndTime(), formatter);
			LocalDateTime meeting_endTime = LocalDateTime.parse(meeting.getEndTime(), formatter);

			if (meeting.getMeetingWith().equals(meet.getCreator())
					|| meeting.getMeetingWith().equals(meet.getMeetingWith())) {
				if (meeting_startTime.isAfter(meeting_endTime)) {
					flag = true;
				} else if ((meeting_startTime.isBefore(meet_endTime)) && (meeting_startTime.isAfter(meet_startTime))) {
					flag = true;
				} else if ((meeting_endTime.isBefore(meet_endTime)) && (meeting_endTime.isAfter(meet_startTime))) {
					flag = true;
				} else if ((meeting_startTime.isBefore(meet_startTime)) && (meeting_endTime.isAfter(meet_endTime))) {
					flag = true;
				}

			}
		}

		if (flag == true) {
			return "meeting_failed";
		} else {
			repo_meeting.save(meeting);
			List<Meeting> listMeeting2 = repo_meeting.findAll();

			List<Meeting> newlistMeeting = new ArrayList<Meeting>();

			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}

			for (Meeting meeting1 : listMeeting2) {
				if ((meeting1.getFlag() == (long) 0 || meeting1.getFlag() == (long) 1)
						&& (meeting1.getCreator().equals(username) || meeting1.getMeetingWith().equals(username))) {
					newlistMeeting.add(meeting1);
				}
			}
			model.addAttribute("listMeetings", newlistMeeting);

			return "meeting_success";
		}

	}
}
