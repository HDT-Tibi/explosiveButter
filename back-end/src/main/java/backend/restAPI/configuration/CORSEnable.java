package backend.restAPI.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * This annotation enables cross origin calls from specified URL in @CrossOrigin
 * annotation
 *
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@CrossOrigin(origins = { "http://localhost:4200" })
public @interface CORSEnable {
}
