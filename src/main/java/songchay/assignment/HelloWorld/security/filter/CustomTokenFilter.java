package songchay.assignment.HelloWorld.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class CustomTokenFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if ( request.getServletPath().equals("/login")) {
			filterChain.doFilter(request, response);
		} else {
			String token = request.getHeader("JWT_Token");
			try {
				if ( !token.isEmpty() ) {
					Algorithm algorith = Algorithm.HMAC256("JWT_TOKEN".getBytes());
					JWTVerifier verifiere = JWT.require(algorith).build();
					DecodedJWT decodedJWT = verifiere.verify(token);
					String username = decodedJWT.getSubject();
					UsernamePasswordAuthenticationToken authTokenn = new UsernamePasswordAuthenticationToken (username, null, null);
					SecurityContextHolder.getContext().setAuthentication(authTokenn);
					filterChain.doFilter(request, response);
				}
			} catch(Exception e) {
				response.sendError(403, "No Entry!");
			}
		}
	}
}
