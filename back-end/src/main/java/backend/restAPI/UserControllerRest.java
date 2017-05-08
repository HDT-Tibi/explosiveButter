package backend.restAPI;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import backend.exceptions.CustomException;
import backend.repository.model.User;
import backend.restAPI.configuration.CORSEnable;
import backend.service.general.UserService;

@CORSEnable
@RestController
public class UserControllerRest {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/register", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> registerNewUser(@RequestBody User user) {
		// ex.addMockDataToServer();
		try {
			userService.registerValidateUser(user);
			return new ResponseEntity<Object>("User registered successfully", HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/users", method = RequestMethod.GET)
	public @ResponseBody List<User> getData() {
		return userService.getAll();
	}
}
