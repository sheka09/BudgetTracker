package io.dawn.budget.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.dawn.budget.entity.User;
import io.dawn.budget.error.UserNotFoundException;
import io.dawn.budget.error.UsernameAlreadyExistsException;

@Service
public interface UserService extends UserDetailsService  {

    public User saveUser(User user,String rolename) throws UsernameAlreadyExistsException;
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public String deleteUserById(Long id) throws UserNotFoundException;

    public User updateUser(Long id, User user) throws UserNotFoundException;

    public Page<User> fetchAllUsers() throws UserNotFoundException;

    public User fetchUserByEmail(String email) throws UserNotFoundException;

    public User fetchUserByFirstname(String firstName) throws UserNotFoundException;

    public User fetchUserByLastname(String lastName) throws UserNotFoundException;

    public User fetchUserById(Long id) throws UserNotFoundException;

    public User fetchUserByUsername(String userName);
    


}
