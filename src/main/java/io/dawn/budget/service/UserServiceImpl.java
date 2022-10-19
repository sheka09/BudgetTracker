package io.dawn.budget.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.dawn.budget.entity.User;
import io.dawn.budget.error.UserNotFoundException;
import io.dawn.budget.error.UsernameAlreadyExistsException;
import io.dawn.budget.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder bcryptEncoder;

    Pageable pageable = PageRequest.of(0, 10);

    @Autowired
    public UserServiceImpl(@Lazy PasswordEncoder bcryptEncoder) {
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public User saveUser(User user, String role) throws UsernameAlreadyExistsException {

        Optional<User> userdb = userRepository.findByUsername(user.getUsername()); 
        if (userdb.isPresent()) {
            throw new UsernameAlreadyExistsException("That username is taken.Try another.");
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setRole(role);
        return userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).get();
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);

    }

    @Override
    public String deleteUserById(Long id) {
        userRepository.deleteById(id);
        return "user deleted successfully";
    }

    @Override
    public User updateUser(Long id, User user) {
        User userDb = userRepository.findById(id).get();
        if (Objects.nonNull(user.getEmail())) {
            userDb.setEmail(user.getEmail());
        }
        if (Objects.nonNull(user.getFirstname())) {
            userDb.setFirstname(user.getFirstname());
        }
        if (Objects.nonNull(user.getLastname())) {
            userDb.setLastname(user.getLastname());
        }
        
        if (Objects.nonNull(user.getUsername())) {
            userDb.setUsername(user.getUsername());
        }

        if (Objects.nonNull(user.getPassword())) {
            userDb.setPassword(user.getPassword());
        }

        if (Objects.nonNull(user.getRole())) {
            userDb.setRole(user.getRole());
        }

        return userRepository.save(userDb);
    }

    @Override
    public Page<User> fetchAllUsers() {

        return userRepository.findAll(pageable);
    }

    @Override
    public User fetchUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User fetchUserByFirstname(String firstName) {

        return userRepository.findByFirstname(firstName);
    }

    @Override
    public User fetchUserByLastname(String lastName) {
        return userRepository.findByLastname(lastName);
    }

    @Override
    public User fetchUserById(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User with userId " + id + " doen't exist");
        }
        return user.get();
    }

    @Override
    public User fetchUserByUsername(String userName) {
        return userRepository.findByUsername(userName).get();
    }

}
