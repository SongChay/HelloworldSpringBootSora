package songchay.assignment.HelloWorld.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import songchay.assignment.HelloWorld.security.filter.CustomAuthFilter;

@Configuration 
@EnableWebSecurity
public class CustomerSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private  BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	protected void configure( AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	@Override
	protected void configure( HttpSecurity http) throws Exception {
//		CustomerFilter customerFilter = new CustomerFilter();
//		customerFilter.setFilterProcessesUrl("/login");
		
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().anyRequest().permitAll();
		
		//customer authentication
		http.addFilter( new CustomAuthFilter(super.authenticationManager()));
		
	}
 
}
