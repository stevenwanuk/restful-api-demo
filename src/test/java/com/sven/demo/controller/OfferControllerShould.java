package com.sven.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.sven.demo.component.GlobalExceptionHandler;
import com.sven.demo.exception.NotFoundException;
import com.sven.demo.model.Offer;
import com.sven.demo.model.User;
import com.sven.demo.service.OfferService;
import com.sven.demo.service.UserService;
import com.sven.demo.utils.Constants;

public class OfferControllerShould
{

	private MockMvc mvc;

	final private SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN);
	@InjectMocks
	private OfferController underTest;

	@Mock
	private OfferService offerService;

	@Mock
	private UserService userService;

	@Before
	public void setup()
	{
		// using "utc" timezone as date format
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(underTest).setHandlerExceptionResolvers(createExceptionResolver())
				.build();
	}

	private ExceptionHandlerExceptionResolver createExceptionResolver()
	{
		// setup ControllerAdvice
		ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver()
		{
			protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
					Exception exception)
			{
				Method method = new ExceptionHandlerMethodResolver(GlobalExceptionHandler.class)
						.resolveMethod(exception);
				return new ServletInvocableHandlerMethod(new GlobalExceptionHandler(), method);
			}
		};
		exceptionResolver.afterPropertiesSet();
		return exceptionResolver;
	}

	@Test
	public void should_findAllByOwnerId_return_200_with_all_offers() throws Exception
	{
		Offer offer1 = Offer.builder().withId(1).build();
		List<Offer> expected = Arrays.asList(offer1);
		when(offerService.findAllValidOfferByOwnerId(any(Long.class))).thenReturn(expected);

		mvc.perform(get("/offers").header("ownerId", "1")).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", Matchers.hasSize(1))).andExpect(jsonPath("$[0].id", Matchers.is(1)));

		verify(offerService).findAllValidOfferByOwnerId(any(Long.class));
	}

	@Test
	public void should_findByIdAndOwnerId_return_404_if_not_found() throws Exception
	{
		when(offerService.getValidOfferByIdAndOwnerId(any(Long.class), any(Long.class)))
				.thenThrow(new NotFoundException());

		mvc.perform(get("/offers/1").header("ownerId", "1")).andExpect(MockMvcResultMatchers.status().isNotFound());

		verify(offerService).getValidOfferByIdAndOwnerId(any(Long.class), any(Long.class));
	}

	@Test
	public void should_findByIdAndOwnerId_return_200_if_found() throws Exception
	{
		Offer expected = Offer.builder().withId(99).build();
		when(offerService.getValidOfferByIdAndOwnerId(any(Long.class), any(Long.class))).thenReturn(expected);

		mvc.perform(get("/offers/1").header("ownerId", "1")).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", Matchers.is(99)));

		verify(offerService).getValidOfferByIdAndOwnerId(any(Long.class), any(Long.class));
	}

	@Test
	public void should_create_return_401_if_user_is_not_found() throws Exception
	{

		when(userService.findUserById(any(Long.class))).thenReturn(Optional.empty());

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("name", "offer_name");
		params.add("detail", "offce_detail");
		params.add("expiryDate", sdf.format(new Date()));
		mvc.perform(post("/offers/").header("ownerId", "1").params(params))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());

		verify(userService).findUserById(any(Long.class));
	}

	@Test
	public void should_create_return_200_if_is_valid_request() throws Exception
	{

		User user = User.builder().withId(1).build();
		when(userService.findUserById(any(Long.class))).thenReturn(Optional.ofNullable(user));

		Offer expected = Offer.builder().withId(99).withName("offer_name").withDetail("offce_detail")
				.withExpiryDate(new Date()).withOwner(user).build();
		when(offerService.save(any(Offer.class))).thenReturn(expected);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("name", expected.getName());
		params.add("detail", expected.getDetail());
		params.add("expiryDate", sdf.format(expected.getExpiryDate()));
		mvc.perform(post("/offers/").header("ownerId", expected.getOwner().getId()).params(params))
				.andExpect(MockMvcResultMatchers.status().isCreated()).andDo(print())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", Matchers.is(((Long) expected.getId()).intValue())))
				.andExpect(jsonPath("$.name", Matchers.is(expected.getName())))
				.andExpect(jsonPath("$.detail", Matchers.is(expected.getDetail())))
				.andExpect(jsonPath("$.expiryDate", Matchers.is(sdf.format(expected.getExpiryDate()))))
				.andExpect(jsonPath("$.owner.id", Matchers.is(((Long) expected.getOwner().getId()).intValue())));
	}

	@Test
	public void should_cancel_return_404_if_offer_is_not_found() throws Exception
	{

		when(offerService.cancel(any(Long.class), any(Long.class))).thenThrow(new NotFoundException());

		mvc.perform(delete("/offers/99").header("ownerId", "1")).andExpect(MockMvcResultMatchers.status().isNotFound());

		verify(offerService).cancel(any(Long.class), any(Long.class));
	}

	@Test
	public void should_cancel_return_cancelled_offer() throws Exception
	{
		User user = User.builder().withId(1).build();
		Offer expected = Offer.builder().withId(99).withName("offer_name").withDetail("offce_detail")
				.withExpiryDate(new Date()).withOwner(user).build();

		when(offerService.cancel(any(Long.class), any(Long.class))).thenReturn(expected);

		mvc.perform(delete("/offers/99").header("ownerId", "1")).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", Matchers.is(((Long) expected.getId()).intValue())));

		verify(offerService).cancel(any(Long.class), any(Long.class));
	}

}
