package io.dawn.budget.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.dawn.budget.entity.Expense;
import io.dawn.budget.entity.ExpenseCategory;

@Repository
public interface ExpenseRepository extends PagingAndSortingRepository<Expense, Long> {
    @Query("SELECT e from Expense e LEFT JOIN e.account a LEFT JOIN a.user u where e.category=:category")
    Page<Expense> findByCategory(@Param("category")ExpenseCategory category, Pageable pageable); //

    @Query("SELECT e from Expense e LEFT JOIN e.account a LEFT JOIN a.user u where e.date=:date")
    Page<Expense> findByDate(@Param("date") LocalDate date, Pageable pageable); //

    @Query("SELECT e from Expense e LEFT JOIN e.account a LEFT JOIN a.user u order by e.date desc")
    Page<Expense> findAllByOrderByDateDesc(Pageable pageable);

    @Query("SELECT e from Expense e LEFT JOIN e.account a LEFT JOIN a.user u order by e.category asc")
    Page<Expense> findAllByOrderByCategoryAsc( Pageable pageable);

    @Query("SELECT e from Expense e LEFT JOIN e.account a LEFT JOIN a.user u order by e.price asc")
    Page<Expense> findAllByOrderByPriceAsc( Pageable pageable);

    @Query("SELECT e from Expense e LEFT JOIN e.account a LEFT JOIN a.user u")
    Page<Expense> findAll( Pageable pageable); 

    @Query("SELECT e from Expense e LEFT JOIN e.account a LEFT JOIN a.user u where e.id=:expenseId")
    Optional<Expense> findById(Long expenseId);

    @Query("SELECT e from Expense e LEFT JOIN e.account a LEFT JOIN a.user u where e.date>=:startDate and e.date<=:endDate order by e.date asc")
    Page<Expense> findByDateBetween(@Param("startDate")LocalDate start, @Param("endDate")LocalDate end, Pageable pageable) throws IllegalArgumentException;

}
