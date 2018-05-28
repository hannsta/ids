package com.cop.ids.services;

import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cop.ids.data.User;
import com.cop.ids.repositories.UserRepository;

public class DatabaseUserDetailsService implements UserDetailsService {
	
	@Autowired 
	private UserRepository userRepository;
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
        User user = userRepository.findByUsername(username);
        UserBuilder builder = null;
        if (user != null) {
          builder = org.springframework.security.core.userdetails.User.withUsername(username);
          builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));
          builder.roles("myRole");
        } else {
          throw new UsernameNotFoundException("User not found.");
        }
        return builder.build();
    }


}