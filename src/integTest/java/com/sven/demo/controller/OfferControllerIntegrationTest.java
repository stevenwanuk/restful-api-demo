package com.sven.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.sven.demo.RestfulApiDemoApplication;
import com.sven.demo.model.Offer;
import com.sven.demo.utils.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestfulApiDemoApplication.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class OfferControllerIntegrationTest
{
	final private SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN);

	@LocalServerPort
	private int port;

	private RestTemplate restTemplate = new TestRestTemplate().getRestTemplate();

	@Test
	public void should_findAllByOwnerId_return_2_valid_offers() throws Exception
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.add("ownerId", "1");

		String baseUrl = "http://localhost:" + port;

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
		URI endpoint = new URI(baseUrl + "/offers");
		ParameterizedTypeReference<List<Offer>> typeReference = new ParameterizedTypeReference<List<Offer>>()
		{
		};
		ResponseEntity<List<Offer>> response = restTemplate.exchange(endpoint, HttpMethod.GET, request, typeReference);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().size()).isEqualTo(2);
		assertThat(response.getBody().get(0).getId()).isEqualTo(1);
		assertThat(response.getBody().get(1).getId()).isEqualTo(2);
	}

	@Test
	public void should_findByIdAndOwnerId_return_not_found_for_expired_offer() throws Exception
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.add("ownerId", "1");

		String baseUrl = "http://localhost:" + port + "/offers";

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
		// sample 3 is expired offer
		URI endpoint = new URI(baseUrl + "/3");
		ResponseEntity<Offer> response = restTemplate.exchange(endpoint, HttpMethod.GET, request, Offer.class);
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void should_findByIdAndOwnerId_return_not_found_for_cancelled_offer() throws Exception
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.add("ownerId", "1");

		String baseUrl = "http://localhost:" + port + "/offers";

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
		// sample 4 is cancelled offer
		URI endpoint = new URI(baseUrl + "/4");
		ResponseEntity<Offer> response = restTemplate.exchange(endpoint, HttpMethod.GET, request, Offer.class);
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void should_findByIdAndOwnerId_return_not_found_for_valid_offer_with_different_owner() throws Exception
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.add("ownerId", "2");

		String baseUrl = "http://localhost:" + port + "/offers";

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
		// sample 1 is valid offer, owned by owner 1
		URI endpoint = new URI(baseUrl + "/1");
		ResponseEntity<Offer> response = restTemplate.exchange(endpoint, HttpMethod.GET, request, Offer.class);
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void should_findByIdAndOwnerId_return_valid_offer_with_giving_owner() throws Exception
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.add("ownerId", "1");

		String baseUrl = "http://localhost:" + port + "/offers";

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
		// sample 1 is valid offer, owned by owner 1
		URI endpoint = new URI(baseUrl + "/1");
		ResponseEntity<Offer> response = restTemplate.exchange(endpoint, HttpMethod.GET, request, Offer.class);
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(response.getBody().getId()).isEqualTo(1);
		assertThat(response.getBody().getOwner().getId()).isEqualTo(1);

	}

	@Test
	public void should_create_return_unauthorized_request_if_owner_not_exists() throws URISyntaxException
	{

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		// Owner 10 is not existing in test sets
		headers.add("ownerId", "10");

		String baseUrl = "http://localhost:" + port + "/offers";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("name", "offer_name");
		params.add("detail", "offce_detail");
		params.add("expiryDate", sdf.format(new Date()));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		URI endpoint = new URI(baseUrl);
		ResponseEntity<Offer> response = restTemplate.exchange(endpoint, HttpMethod.POST, request, Offer.class);
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void should_create_return_new_created_offer() throws URISyntaxException
	{

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.add("ownerId", "1");

		String baseUrl = "http://localhost:" + port + "/offers";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("name", "offer_name");
		params.add("detail", "offce_detail");
		params.add("expiryDate", sdf.format(new Date()));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		URI endpoint = new URI(baseUrl);
		ResponseEntity<Offer> response = restTemplate.exchange(endpoint, HttpMethod.POST, request, Offer.class);
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
		assertThat(response.getBody().getOwner().getId()).isEqualTo(1);
		assertThat(response.getBody().isDeleted()).isEqualTo(false);
	}

	@Test
	public void should_cancel_return_not_found_for_expired_offer() throws URISyntaxException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.add("ownerId", "1");

		String baseUrl = "http://localhost:" + port + "/offers";

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
		// sample 3 is expired offer
		URI endpoint = new URI(baseUrl + "/3");
		ResponseEntity<Offer> response = restTemplate.exchange(endpoint, HttpMethod.DELETE, request, Offer.class);
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void should_cancel_return_not_found_for_cancelled_offer() throws URISyntaxException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.add("ownerId", "1");

		String baseUrl = "http://localhost:" + port + "/offers";

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
		// sample 4 is cancelled offer
		URI endpoint = new URI(baseUrl + "/4");
		ResponseEntity<Offer> response = restTemplate.exchange(endpoint, HttpMethod.DELETE, request, Offer.class);
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void should_cancel_return_not_found_for_valid_offer_with_different_owner() throws URISyntaxException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.add("ownerId", "10");

		String baseUrl = "http://localhost:" + port + "/offers";

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
		// sample 1 is valid offer with owner 1
		URI endpoint = new URI(baseUrl + "/1");
		ResponseEntity<Offer> response = restTemplate.exchange(endpoint, HttpMethod.DELETE, request, Offer.class);
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void should_cancel_return_cancelled_offer() throws URISyntaxException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.add("ownerId", "2");

		String baseUrl = "http://localhost:" + port + "/offers";

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
		// sample 1 is valid offer with owner 1
		URI endpoint = new URI(baseUrl + "/5");
		ResponseEntity<Offer> response = restTemplate.exchange(endpoint, HttpMethod.DELETE, request, Offer.class);
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(response.getBody().getId()).isEqualTo(5);
		assertThat(response.getBody().getOwner().getId()).isEqualTo(2);
		assertThat(response.getBody().isDeleted()).isEqualTo(true);
	}

}
