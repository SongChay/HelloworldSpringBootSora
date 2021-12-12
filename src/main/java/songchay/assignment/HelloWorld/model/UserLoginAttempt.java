package songchay.assignment.HelloWorld.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "login.failed.attempt")
public class UserLoginAttempt {

	private static int failAttempts;

	public static int getFailAttempts() {
		return failAttempts;
	}

	public static void setFailAttempts(int failAttempts) {
		UserLoginAttempt.failAttempts = failAttempts;
	}
}
