package com.sven.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sven.demo.exception.NotFoundException;
import com.sven.demo.model.Offer;
import com.sven.demo.repository.OfferRepository;

public class OfferServiceShould
{

	@InjectMocks
	public OfferService underTest;

	@Mock
	private OfferRepository offerRepository;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void should_findAllByOwnerIdAndIsDeletedFalseAndExpiryDateAfter_return_offer_list()
	{
		Offer offer1 = Offer.builder().withId(1).build();
		List<Offer> expected = Arrays.asList(offer1);
		when(offerRepository.findAllByOwnerIdAndIsDeletedFalseAndExpiryDateAfter(any(Long.class), any(Date.class)))
				.thenReturn(expected);

		List<Offer> actual = underTest.findAllValidOfferByOwnerId(1l);

		assertThat(actual).containsExactlyElementsOf(expected);
		verify(offerRepository).findAllByOwnerIdAndIsDeletedFalseAndExpiryDateAfter(any(Long.class), any(Date.class));
	}

	@Test(expected = NotFoundException.class)
	public void should_getValidOfferByIdAndOwnerId_throws_exception_when_offer_is_not_found()
	{
		when(offerRepository.findByIdAndOwnerIdAndIsDeletedFalseAndExpiryDateAfter(any(Long.class), any(Long.class),
				any(Date.class))).thenReturn(Optional.empty());

		underTest.getValidOfferByIdAndOwnerId(1l, 999l);

		assertThat(false).isEqualTo(true);
	}

	@Test
	public void should_getValidOfferByIdAndOwnerId_return_offer()
	{
		Offer expected = Offer.builder().withId(1).build();
		when(offerRepository.findByIdAndOwnerIdAndIsDeletedFalseAndExpiryDateAfter(any(Long.class), any(Long.class),
				any(Date.class))).thenReturn(Optional.of(expected));

		Offer actual = underTest.getValidOfferByIdAndOwnerId(1l, 999l);

		assertThat(actual).isEqualTo(expected);
		verify(offerRepository).findByIdAndOwnerIdAndIsDeletedFalseAndExpiryDateAfter(any(Long.class), any(Long.class),
				any(Date.class));
	}

	@Test
	public void should_save_offer()
	{
		Offer expected = Offer.builder().withId(1).build();
		when(offerRepository.save(any(Offer.class))).thenReturn(expected);

		Offer actual = underTest.save(expected);
		assertThat(actual).isEqualTo(expected);
		verify(offerRepository).save(any(Offer.class));
	}

	@Test(expected = NotFoundException.class)
	public void should_cancel_throws_exception_if_offer_is_not_found()
	{

		when(offerRepository.findByIdAndOwnerIdAndIsDeletedFalseAndExpiryDateAfter(any(Long.class), any(Long.class),
				any(Date.class))).thenReturn(Optional.empty());

		underTest.cancel(1l, 999l);

		assertThat(false).isEqualTo(true);
	}

	@Test
	public void should_cancel_return_cancelled_offer()
	{

		Offer expected = Offer.builder().withId(1).build();
		when(offerRepository.findByIdAndOwnerIdAndIsDeletedFalseAndExpiryDateAfter(any(Long.class), any(Long.class),
				any(Date.class))).thenReturn(Optional.of(expected));
		when(offerRepository.save(any(Offer.class))).thenReturn(expected);

		Offer actual = underTest.cancel(1l, 999l);

		assertThat(actual).isEqualTo(expected);
		assertThat(actual).hasFieldOrPropertyWithValue("isDeleted", true);
		verify(offerRepository).findByIdAndOwnerIdAndIsDeletedFalseAndExpiryDateAfter(any(Long.class), any(Long.class),
				any(Date.class));
		verify(offerRepository).save(any(Offer.class));
	}

}
