package songchay.assignment.HelloWorld.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import songchay.assignment.HelloWorld.model.UserLoginAttempt;
import songchay.assignment.HelloWorld.model.UserTest;
import songchay.assignment.HelloWorld.repo.UserData;
import songchay.assignment.HelloWorld.service.UserDataService;

@Service 
@RequiredArgsConstructor
public class UserDataServiceImpl implements UserDataService , UserDetailsService{

	@Autowired
	private UserData userData;
	
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	
	@Override
	public UserTest retrieveUser() {
		
		UserTest user1 = new UserTest();
		user1.setUsername(userData.getUsername());
		user1.setPassword(userData.getPassword());
		return user1;
		
	}
	
	//override user authentication data
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		boolean isLocked = UserLoginAttempt.getFailAttempts() >= 3;
		if (isLocked) {
			
			throw new LockedException(
					this.messages.getMessage("AccountStatusUserDetailsChecker.locked", "User account is locked"));
          
        }
		
		UserTest user = this.retrieveUser();
		if ( !username.equals(user.getUsername())) {
			throw new UsernameNotFoundException( "User not found!" );
		}
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("USER"));
		
		return new User (user.getUsername(), user.getPassword(), authorities);
	}

	@Override
	public void unlockedUser() {
		UserLoginAttempt.setFailAttempts(0);
		System.out.println("Succesful unlocked! Login failed attempts = " + UserLoginAttempt.getFailAttempts());
	}
	
}
