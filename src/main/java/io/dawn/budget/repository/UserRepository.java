package io.dawn.budget.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.dawn.budget.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Page<User> findAll(Pageable pageable);

    public User findByEmail(String email);

    public User findByFirstname(String firstName);

    public User findByLastname(String lastName);

    public Optional<User> findById(Long id);

    public Optional<User> findByUsername(String username);

}
