package backend.restAPI;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.restAPI.configuration.CORSEnable;

/**
 * Maps all AngularJS routes to index so that they work with direct linking.
 */
@CORSEnable
@RestController
class Routes {

	@RequestMapping({ "/" })
	public String index() {
		return "forward:/client/index.html";
	}
}
