package com.sven.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sven.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{

}
