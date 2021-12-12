package songchay.assignment.HelloWorld.repo;

import org.springframework.stereotype.Repository;

@Repository
public class UserData {
	private static String USERNAME  = "user";
	private static String PASSWORD  = "$2a$12$63SwL6ElEMODOiFkUDus1uzG78T2R5yMPWQGLEW.EYgYVyqrQdSr.";
	
	public  String getUsername () {
		return USERNAME;
	}
	
	public  String getPassword () {
		return PASSWORD;
	}
	
	
}
