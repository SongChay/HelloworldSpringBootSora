package songchay.assignment.HelloWorld.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import songchay.assignment.HelloWorld.service.UserDataService;

@RestController
public class MainController<T> {
	
	@Autowired
	private UserDataService userDataService;
	
	@RequestMapping(value={"", "/", "/test"})
	public String  test() {
		return "hello";
	}
	
	@RequestMapping(value={"/unlock"})
	public String  unlockUser() {
		userDataService.unlockedUser();
		return "Successful!";
	}
}
