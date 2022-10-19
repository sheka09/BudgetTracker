package io.dawn.budget.service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.dawn.budget.entity.Account;
import io.dawn.budget.entity.Expense;
import io.dawn.budget.entity.ExpenseCategory;
import io.dawn.budget.error.AccountNotFoundException;
import io.dawn.budget.error.ExpenseAlreadyExistsException;
import io.dawn.budget.error.ExpenseNotFoundException;
import io.dawn.budget.error.InsufficientBalanceException;
import io.dawn.budget.error.UserNotFoundException;
import io.dawn.budget.repository.AccountRepository;
import io.dawn.budget.repository.ExpenseRepository;
import io.dawn.budget.repository.UserRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Expense createExpense(Long userId, Long accountId, Expense expenseRequest)
            throws ExpenseAlreadyExistsException, InsufficientBalanceException, AccountNotFoundException,
            UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        if(!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("Account with id"+accountId+" not found");
        }
        Account ExistingAccount = accountRepository.findById(accountId).get();
        if (expenseRequest.getPrice().compareTo(ExistingAccount.getBalance()) == 1) {
            throw new InsufficientBalanceException();
        }

        return accountRepository.findById(accountId).map(account -> {
            expenseRequest.setAccount(account);
            account.setBalance(account.getBalance().subtract(expenseRequest.getPrice()));
            return expenseRepository.save(expenseRequest);
        }).orElseThrow(() -> new AccountNotFoundException("Account with id: " + accountId + " Not Found."));
    }

    @Override
    public void deleteExpenseById(Long expenseId) {
        expenseRepository.deleteById(expenseId);

    }

    @Override
    public Expense updateExpense(Long expenseId, Expense expense) throws ExpenseNotFoundException {
        Expense existingExpense = expenseRepository.findById(expenseId).get();

        if (Objects.nonNull(expense.getPrice())) {
            existingExpense.setPrice(expense.getPrice());
        }

        if (Objects.nonNull(expense.getCategory())) {
            existingExpense.setCategory(expense.getCategory());
        }
        existingExpense.setNote(expense.getNote());

        if (Objects.nonNull(expense.getDate())) {
            existingExpense.setDate(expense.getDate());
        }

        return expenseRepository.save(existingExpense);
    }

    @Override
    public Page<Expense> fetchExpenseByCategory(Long userId, ExpenseCategory category, Pageable pageable) {
        return expenseRepository.findByCategory(category, pageable);
    }

    @Override
    public Page<Expense> fetchExpenseByDate(Long userId, LocalDate date, Pageable pageable) { //
        return expenseRepository.findByDate(date, pageable);
    }

    @Override
    public Page<Expense> fetchExpenseBetweenDates(Long userId, LocalDate startDate, LocalDate endDate,
            Pageable pageable) throws ExpenseNotFoundException {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate should be before endDate");
        }
        return expenseRepository.findByDateBetween(startDate, endDate, pageable);
    }

    @Override
    public Page<Expense> fetchAllOrderByDate(Long userId, Pageable pageable) {
        return expenseRepository.findAllByOrderByDateDesc(pageable);
    }

    @Override
    public Page<Expense> fetchAllOrderByPrice(Long userId, Pageable pageable) {
        return expenseRepository.findAllByOrderByPriceAsc(pageable);
    }

    @Override
    public Expense fetchExpenseById(Long userId, Long id) throws ExpenseNotFoundException {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (!expense.isPresent()) {
            throw new ExpenseNotFoundException();
        }
        return expense.get();
    }

    @Override
    public Page<Expense> fetchAllOrderByCategory(Long userId, Pageable pageable) throws ExpenseNotFoundException {
        return expenseRepository.findAllByOrderByCategoryAsc(pageable);
    }

}
