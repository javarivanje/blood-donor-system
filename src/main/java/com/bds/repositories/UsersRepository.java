package com.bds.repositories;

import com.bds.models.Role;
import com.bds.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository
        extends JpaRepository<Users, Long> {

    @Query(value = "SELECT u FROM users u WHERE u.role = ?1")
    List<Users> findByRoleIs(Role role);

    @Query(value = "SELECT count(u.email) = 1 FROM users u WHERE u.email = :email")
    boolean existsUsersByEmail(@Param("email") String email);

    @Query(value = "SELECT u FROM users u WHERE u.email = :email")
    Users findUsersByEmail(@Param("email") String email);
}