package io.dawn.budget.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.dawn.budget.entity.Account;
import io.dawn.budget.error.AccountAlreadyExistsException;
import io.dawn.budget.error.AccountNotFoundException;
import io.dawn.budget.error.UserNotFoundException;
import io.dawn.budget.repository.AccountRepository;
import io.dawn.budget.repository.UserRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;
    
  
    @Override
    public Account createAccount(Long userId, Account accountRequest)
            throws AccountAlreadyExistsException, UserNotFoundException {
        return userRepository.findById(userId).map(user -> {
               accountRequest.setUser(user);
                return accountRepository.save(accountRequest);
        }).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " Not Found."));
    }

    @Override
    public void deleteAccountById(Long accountId) throws AccountNotFoundException {
        Optional<Account> accountDb = accountRepository.findById(accountId);
        if (!accountDb.isPresent()) {
            throw new AccountNotFoundException("Account Not Found.");
        }
        
        accountRepository.deleteById(accountId);

    }

    @Override
    public Account updateAccount(Long accountId, Account account)
            throws AccountNotFoundException, ResponseStatusException {

        if (!accountRepository.findById(accountId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found.");
        }
        Account accountDb = accountRepository.findById(accountId).get();

        if (Objects.nonNull(account.getName()) && !"".equalsIgnoreCase(account.getName())) {
            accountDb.setName(account.getName());
        }

        if (Objects.nonNull(account.getBalance())) {
            accountDb.setBalance(account.getBalance());
        }

        if (Objects.nonNull(account.getAccountType())) {
            accountDb.setAccountType(account.getAccountType());
        }

        return accountRepository.save(accountDb);

    }

    @Override
    public Account fetchAccountById(Long accountId) throws AccountNotFoundException {
        Optional<Account> account = accountRepository.findById(accountId);
        if (!account.isPresent()) {
            throw new AccountNotFoundException("Account Not Found.");
        }
        return account.get();

    }

    @Override
    public Page<Account> fetchAllAccounts(Long userId, Pageable pageable) {
        return accountRepository.findAllAccountByUser(userId,pageable);
    }

    @Override
    public Account fetchAccountByName(String accountName) throws AccountNotFoundException {
        Optional<Account> account = accountRepository.findByName(accountName);
        if (!account.isPresent()) {
            throw new AccountNotFoundException("Account Not Found.");
        }
        return account.get();

    }

   

     

}
