package songchay.assignment.HelloWorld.security.filter;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import songchay.assignment.HelloWorld.model.UserLoginAttempt;
import songchay.assignment.HelloWorld.model.UserTest;

public class CustomAuthFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public CustomAuthFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		StringBuffer sb = new StringBuffer();
		String line = null;

		BufferedReader reader;
		UserTest loginRequest = new UserTest();
		try {
			reader = request.getReader();

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			// json transformation
			ObjectMapper mapper = new ObjectMapper();
			loginRequest = mapper.readValue(sb.toString(), UserTest.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub

		User user = (User) authResult.getPrincipal();

		Algorithm algorith = Algorithm.HMAC256("JWT_TOKEN".getBytes());
		String access_token = JWT.create().withSubject(user.getUsername()).sign(algorith);

		response.setHeader("JWT_Token", access_token);
		
		String failAttempts = "{ \"failAttempts\" :"  + UserLoginAttempt.getFailAttempts() + "}";
		response.getWriter().write(new ObjectMapper().writeValueAsString(failAttempts));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		int j = UserLoginAttempt.getFailAttempts() + 1;

		UserLoginAttempt.setFailAttempts(j);

		System.out.println("Fail attempts: " + j);
		
		String failAttempts = "{ \"failAttempts\" :"  + UserLoginAttempt.getFailAttempts() + "}";
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new ObjectMapper().writeValueAsString(failAttempts));

//		super.unsuccessfulAuthentication(request, response, failed);
	}

}
