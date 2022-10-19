package io.dawn.budget.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import io.dawn.budget.entity.Account;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    Optional<Account> findByName(String accountName);

    Optional<Account> findById(Long accountId);

    Page<Account> findAll(Pageable pageable); 
    
    @Query("select u from User u where u.id=:userId")
    Page<Account> findAllAccountByUser(Long userId,Pageable pageable); 

    


}
