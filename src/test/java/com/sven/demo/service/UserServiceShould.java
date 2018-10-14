package com.sven.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sven.demo.model.User;
import com.sven.demo.repository.UserRepository;

public class UserServiceShould
{
	@InjectMocks
	public UserService underTest;

	@Mock
	private UserRepository userRepository;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void should_findById_return_user()
	{

		User user = User.builder().withName("test_user").build();
		Optional<User> expected = Optional.ofNullable(user);
		when(userRepository.findById(any(Long.class))).thenReturn(expected);

		Optional<User> actual = underTest.findUserById(1l);

		assertThat(actual).isEqualTo(expected);
	}
}
