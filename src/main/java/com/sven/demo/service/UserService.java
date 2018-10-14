package com.sven.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sven.demo.model.User;
import com.sven.demo.repository.UserRepository;

@Service
public class UserService
{

	private UserRepository userRepository;

	public UserService(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	public Optional<User> findUserById(Long id)
	{

		return userRepository.findById(id);
	}
}
