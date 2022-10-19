package io.dawn.budget.controller;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.dawn.budget.entity.Expense;
import io.dawn.budget.entity.ExpenseCategory;
import io.dawn.budget.error.AccountNotFoundException;
import io.dawn.budget.error.ExpenseAlreadyExistsException;
import io.dawn.budget.error.ExpenseNotFoundException;
import io.dawn.budget.error.InsufficientBalanceException;
import io.dawn.budget.error.UserNotFoundException;
import io.dawn.budget.service.ExpenseService;

@RestController
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/users/{uId}/account/{aId}/expense")
    public ResponseEntity<?> createExpense(@PathVariable("uId") Long userId, @PathVariable("aId") Long accountId,
            @RequestBody Expense expense) throws ExpenseAlreadyExistsException, InsufficientBalanceException,
            AccountNotFoundException, UserNotFoundException {

        try {
            return new ResponseEntity<Expense>(expenseService.createExpense(userId, accountId, expense),
                    HttpStatus.CREATED);
        } catch (ExpenseAlreadyExistsException expenseAlreadyExistsException) {
            return new ResponseEntity<String>(expenseAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
        } catch (InsufficientBalanceException insufficientBalanceException) {
            return new ResponseEntity<String>(insufficientBalanceException.getMessage(), HttpStatus.CONFLICT);
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity<String>(userNotFoundException.getMessage(), HttpStatus.CONFLICT);

        } catch (AccountNotFoundException accountNotFoundException) {
            return new ResponseEntity<String>(accountNotFoundException.getMessage(), HttpStatus.CONFLICT);

        }
    }

    @PutMapping("/users/{uId}/expenses/{eId}")
    public ResponseEntity<?> updateExpense(@PathVariable("uId") Long userId, @PathVariable("eId") Long expenseId, @RequestBody Expense expense)
            throws ExpenseNotFoundException {
        try {
            return new ResponseEntity<Expense>(expenseService.updateExpense(expenseId, expense), HttpStatus.OK);
        } catch (ExpenseNotFoundException expenseNotFoundException) {
            return new ResponseEntity<String>(expenseNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{uid}/expenses/{eId}")
    public ResponseEntity<?> deleteExpense(@PathVariable("uId") Long userId, @PathVariable("eId") Long expenseId) throws ExpenseNotFoundException {

        Expense expense = expenseService.fetchExpenseById(userId,expenseId);
        if (Objects.nonNull(expense)) {
            expenseService.deleteExpenseById(expenseId);
            return new ResponseEntity<String>(HttpStatus.OK);
        }
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/users/{uId}/expenses/{eId}") 
    public ResponseEntity<?> fetchById(@PathVariable("uId") Long userId, @PathVariable("eId") Long expenseId) throws ExpenseNotFoundException {
        try {
            return new ResponseEntity<Expense>(expenseService.fetchExpenseById(userId,expenseId), HttpStatus.OK);
        } catch (ExpenseNotFoundException expenseNotFoundException) {
            return new ResponseEntity<String>(expenseNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/users/{uId}/expenses/category/{category}")
    public ResponseEntity<?> fetchByCategory(@PathVariable("uId") Long userId, @PathVariable ExpenseCategory category,
            Pageable pageable) throws ExpenseNotFoundException {
        try {
            return new ResponseEntity<Page<Expense>>(expenseService.fetchExpenseByCategory(userId, category, pageable),
                    HttpStatus.OK);
        } catch (ExpenseNotFoundException expenseNotFoundException) {
            return new ResponseEntity<String>(expenseNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/users/{uId}/expenses/date/{date}") 
    public ResponseEntity<?> fetchByDate(@PathVariable("uId") Long userId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, Pageable pageable)
            throws ExpenseNotFoundException {
        try {
            return new ResponseEntity<Page<Expense>>(expenseService.fetchExpenseByDate(userId, date, pageable),
                    HttpStatus.OK);
        } catch (ExpenseNotFoundException expenseNotFoundException) {
            return new ResponseEntity<String>(expenseNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/users/{uId}/expenses/sorted-date") 
    public ResponseEntity<?> fetchAllOrderByDate(@PathVariable("uId") Long userId, Pageable pageable)
            throws ExpenseNotFoundException {
        try {
            return new ResponseEntity<Page<Expense>>(expenseService.fetchAllOrderByDate(userId, pageable),
                    HttpStatus.OK);
        } catch (ExpenseNotFoundException expenseNotFoundException) {
            return new ResponseEntity<String>(expenseNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{uId}/expenses/sorted-by-category") 
                                                          
    public ResponseEntity<?> fetchAllOrderByCategory(@PathVariable("uId") Long userId, Pageable pageable)
            throws ExpenseNotFoundException {
        try {
            return new ResponseEntity<Page<Expense>>(expenseService.fetchAllOrderByCategory(userId, pageable),
                    HttpStatus.OK);
        } catch (ExpenseNotFoundException expenseNotFoundException) {
            return new ResponseEntity<String>(expenseNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{uId}/expenses/sorted-by-price") 
    public ResponseEntity<?> fetchAllOrderByPrice(@PathVariable("uId") Long userId, Pageable pageable)
            throws ExpenseNotFoundException {
        try {
            return new ResponseEntity<Page<Expense>>(expenseService.fetchAllOrderByPrice(userId, pageable),
                    HttpStatus.OK);
        } catch (ExpenseNotFoundException expenseNotFoundException) {
            return new ResponseEntity<String>(expenseNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/users/{uId}/expenses/dates/{startDate}/{endDate}") 
    public ResponseEntity<?> fetchExpenseByPeriod(@PathVariable("uId") Long userId, @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate startDate,
             @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate endDate, Pageable pageable) throws ExpenseNotFoundException {
        try {
            return new ResponseEntity<Page<Expense>>(
                    expenseService.fetchExpenseBetweenDates(userId, startDate, endDate, pageable), HttpStatus.OK);
            
        } catch (ExpenseNotFoundException expenseNotFoundException) {
            return new ResponseEntity<String>(expenseNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
