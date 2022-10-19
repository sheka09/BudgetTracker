package io.dawn.budget.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.dawn.budget.config.JwtTokenUtil;

import io.dawn.budget.entity.JwtRequest;
import io.dawn.budget.entity.JwtResponse;


import io.dawn.budget.entity.User;
import io.dawn.budget.error.UserNotFoundException;
import io.dawn.budget.error.UsernameAlreadyExistsException;
import io.dawn.budget.service.UserService;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/{role}/signup")
    public ResponseEntity<?> saveUser(@RequestBody User user,@PathVariable("role") String roleName)  {
        try {
            return new ResponseEntity<User>(userService.saveUser(user,roleName), HttpStatus.CREATED);
        } catch (UsernameAlreadyExistsException userNameAlreadyExistsException) {
            return new ResponseEntity<>(userNameAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
        }
    }
    
    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {

        System.out.println(authenticationRequest.getUsername()+" "+authenticationRequest.getPassword()); //log
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(2);
        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
        System.out.println(3);
        final String token = jwtTokenUtil.generateToken(userDetails);
        System.out.println(4);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long userId) throws UserNotFoundException {
        User user = userService.fetchUserById(userId);
        if (Objects.nonNull(user)) {
            userService.deleteUserById(userId);
            return new ResponseEntity<String>(HttpStatus.OK);
        }
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long userId, @RequestBody User user)
            throws UserNotFoundException {
        try {
            return new ResponseEntity<User>(userService.updateUser(userId, user), HttpStatus.OK);
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity<>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> fetchAll() throws UserNotFoundException {
        return new ResponseEntity<Page<User>>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<?> fetchByEmail(@PathVariable String email) {
        try {
            return new ResponseEntity<User>(userService.fetchUserByEmail(email), HttpStatus.OK);
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity<>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/firstname/{firstName}")
    public ResponseEntity<?> fetchByFirstname(@PathVariable String firstName) {
        try {
            return new ResponseEntity<User>(userService.fetchUserByFirstname(firstName), HttpStatus.OK);
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity<>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/lastname/{lastName}") 
    public ResponseEntity<?> fetchByLastname(@PathVariable String lastName) {
        try {
            return new ResponseEntity<User>(userService.fetchUserByLastname(lastName), HttpStatus.OK);
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity<>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> fetchById(@PathVariable Long id) throws UserNotFoundException {
        try {
            return new ResponseEntity<User>(userService.fetchUserById(id), HttpStatus.OK);
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity<>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/users/username/{userName}")
    public ResponseEntity<?> fetchByUsername(@PathVariable String userName) {
        try {
            return new ResponseEntity<User>(userService.fetchUserByUsername(userName), HttpStatus.OK);
        } catch (UsernameNotFoundException usernameFoundException) {
            return new ResponseEntity<>(usernameFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
