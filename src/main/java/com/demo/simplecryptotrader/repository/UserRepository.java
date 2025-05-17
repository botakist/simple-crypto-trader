package com.demo.simplecryptotrader.repository;

import com.demo.simplecryptotrader.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
