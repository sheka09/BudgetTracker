package io.dawn.budget.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.dawn.budget.entity.Expense;
import io.dawn.budget.entity.ExpenseCategory;
import io.dawn.budget.error.AccountNotFoundException;
import io.dawn.budget.error.ExpenseAlreadyExistsException;
import io.dawn.budget.error.ExpenseNotFoundException;
import io.dawn.budget.error.InsufficientBalanceException;
import io.dawn.budget.error.UserNotFoundException;

@Service
public interface ExpenseService {

    public Expense createExpense(Long userId, Long accountId, Expense expense)
            throws ExpenseAlreadyExistsException, InsufficientBalanceException, AccountNotFoundException,UserNotFoundException;

    public void deleteExpenseById(Long id) throws ExpenseNotFoundException;

    public Expense updateExpense(Long id, Expense expense) throws ExpenseNotFoundException;

    public Page<Expense> fetchExpenseByCategory(Long userId, ExpenseCategory category, Pageable pageable)
            throws ExpenseNotFoundException;

    public Expense fetchExpenseById(Long userId,Long id) throws ExpenseNotFoundException;

    public Page<Expense> fetchExpenseByDate(Long userId, LocalDate date, Pageable pageable)
            throws ExpenseNotFoundException;

    public Page<Expense> fetchExpenseBetweenDates(Long userId,LocalDate startDate, LocalDate endDate, Pageable pageable)
            throws ExpenseNotFoundException;// by date start_to_end period

    public Page<Expense> fetchAllOrderByDate(Long userId, Pageable pageable) throws ExpenseNotFoundException;

    public Page<Expense> fetchAllOrderByCategory(Long userId, Pageable pageable) throws ExpenseNotFoundException;

    public Page<Expense> fetchAllOrderByPrice(Long userId, Pageable pageable) throws ExpenseNotFoundException;

}
