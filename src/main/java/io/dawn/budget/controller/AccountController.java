package io.dawn.budget.controller;


import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.dawn.budget.entity.Account;
import io.dawn.budget.error.AccountAlreadyExistsException;
import io.dawn.budget.error.AccountNotFoundException;
import io.dawn.budget.error.UserNotFoundException;
import io.dawn.budget.service.AccountService;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    @GetMapping("account/{aId}")
    public ResponseEntity<?> fetchAccountById(@PathVariable("aId") Long accountId) throws AccountNotFoundException {
        try {
            return new ResponseEntity<Account>(accountService.fetchAccountById(accountId), HttpStatus.OK);
        } catch (AccountNotFoundException accountNotFoundException) {
            return new ResponseEntity<String>(accountNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/user/{uId}/account")
    public ResponseEntity<?> createAccount(@PathVariable("uId") Long userId, @RequestBody Account account)
            throws AccountAlreadyExistsException, UserNotFoundException {
        try {
            return new ResponseEntity<Account>(accountService.createAccount(userId, account), HttpStatus.CREATED);
        } catch (AccountAlreadyExistsException accountAlreadyExistsException) {
            return new ResponseEntity<>(accountAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity<String>(userNotFoundException.getMessage(), HttpStatus.CONFLICT);

        }

    }

    @DeleteMapping("/account/{aId}")
    public ResponseEntity<?> deleteAccountById(@PathVariable("aId") Long accountId)
            throws AccountNotFoundException {
        Account account = accountService.fetchAccountById(accountId);
           if (Objects.nonNull(account)) {
            accountService.deleteAccountById(accountId);
            return new ResponseEntity<String>(HttpStatus.OK);
        }
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/users/{uId}/accounts")  // /users/{uId}/accounts
    public ResponseEntity<Page<Account>> fetchAccountList(@PathVariable("uId") Long userId, Pageable pageable) {
        return new ResponseEntity<Page<Account>>(accountService.fetchAllAccounts(userId,pageable), HttpStatus.OK); //fetch all accounts that belong to a particular user
    }

    @PutMapping("/account/{aId}")
    public ResponseEntity<?> updateAccount(@PathVariable("aId") Long accountId, @RequestBody Account account)
            throws AccountNotFoundException {
        try {
            return new ResponseEntity<Account>(accountService.updateAccount(accountId, account), HttpStatus.OK);
        } catch (AccountNotFoundException accountNotFoundException) {
            return new ResponseEntity<String>(accountNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
