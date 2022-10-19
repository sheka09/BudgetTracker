package io.dawn.budget.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.dawn.budget.entity.Account;
import io.dawn.budget.error.AccountAlreadyExistsException;
import io.dawn.budget.error.AccountNotFoundException;
import io.dawn.budget.error.UserNotFoundException;

@Service
public interface AccountService {

    public Account createAccount( Long userId, Account account) throws AccountAlreadyExistsException, UserNotFoundException;

    public Page<Account> fetchAllAccounts(Long userId,Pageable pageable);

    public void deleteAccountById(Long accountId) throws AccountNotFoundException;

    public Account updateAccount(Long accountId, Account account) throws AccountNotFoundException;

    public Account fetchAccountByName(String AccountName) throws AccountNotFoundException;

    public Account fetchAccountById(Long accountId) throws AccountNotFoundException;
    
}
