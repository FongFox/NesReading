package com.nesreading.service;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import jakarta.servlet.DispatcherType;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserService userService;

  public CustomUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Todo Here!
    com.nesreading.domain.User user = this.userService.handleFetchUserByEmail(username);

    if (user == null) {
      throw new UsernameNotFoundException("User not found!");
    }

    return new User(
        user.getEmail(),
        user.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
  }

}
