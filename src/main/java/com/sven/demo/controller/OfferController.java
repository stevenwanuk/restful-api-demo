package com.sven.demo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sven.demo.exception.UnauthorizedException;
import com.sven.demo.model.Offer;
import com.sven.demo.model.User;
import com.sven.demo.service.OfferService;
import com.sven.demo.service.UserService;
import com.sven.demo.utils.Constants;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/offers")
public class OfferController
{

	private OfferService offerService;
	private UserService userService;

	public OfferController(OfferService offerService, UserService userService)
	{
		this.userService = userService;
		this.offerService = offerService;

	}

	@GetMapping()
	public List<Offer> findAllByOwnerId(@RequestHeader Long ownerId)
	{
		return offerService.findAllValidOfferByOwnerId(ownerId);
	}

	@GetMapping("/{id}")
	public Offer findByIdAndOwnerId(@PathVariable Long id, @RequestHeader Long ownerId)
	{
		return offerService.getValidOfferByIdAndOwnerId(id, ownerId);
	}

	@PostMapping()
	public ResponseEntity<Offer> create(@RequestParam String name, @RequestParam String detail,
			@RequestParam @DateTimeFormat(pattern = Constants.DATE_FORMAT_PATTERN) Date expiryDate,
			@RequestHeader Long ownerId)
	{

		User owner = userService.findUserById(ownerId).orElseThrow(() -> new UnauthorizedException());

		Offer offer = Offer.builder().withName(name).withDetail(detail).withCreateDate(new Date())
				.withExpiryDate(expiryDate).withOwner(owner).build();

		Offer savedOffer = offerService.save(offer);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedOffer);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Offer> cancel(@PathVariable Long id, @RequestHeader Long ownerId)
	{
		Offer cancelledOffer = offerService.cancel(id, ownerId);
		return ResponseEntity.ok(cancelledOffer);
	}
}
