package com.sven.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sven.demo.exception.NotFoundException;
import com.sven.demo.model.Offer;
import com.sven.demo.repository.OfferRepository;

@Service
public class OfferService
{
	private OfferRepository offerRepository;

	public OfferService(OfferRepository offerRepository)
	{
		this.offerRepository = offerRepository;
	}

	/**
	 * find all valid offers by giving <i>ownerId</i>
	 * 
	 * <p>
	 * Valid offer means it must NOT be deleted or expired.
	 * 
	 * @param ownerId
	 * @return NotNull, List of {@link Offer}
	 */
	public List<Offer> findAllValidOfferByOwnerId(Long ownerId)
	{
		return offerRepository.findAllByOwnerIdAndIsDeletedFalseAndExpiryDateAfter(ownerId, new Date());
	}

	//@formatter:off
	/**
	 * 
	 * get a valid offer by giving <i>id</i> and <i>ownerId</i>
	 * 
	 * <p>
	 * Valid offer means it must NOT be deleted or expired.
	 * 
	 * @param id
	 * @param ownerId
	 * @return
	 * @throws NotFoundException 
	 * <p>
	 * 	if with one or more of the following conditions:
	 * 	<i> Offer is not found by giving Id</i>
	 *  <i> Offer is not own by giving ownerId  </i>
	 *  <i> Offer has been cancelled or deleted  </i>
	 *  <i> Offer is expired  </i>
	 */
	//@formatter:on
	public Offer getValidOfferByIdAndOwnerId(Long id, Long ownerId)
	{
		return offerRepository.findByIdAndOwnerIdAndIsDeletedFalseAndExpiryDateAfter(id, ownerId, new Date())
				.orElseThrow(() -> new NotFoundException());
	}

	/**
	 * persist giving <i>offer</i>
	 * 
	 * @param offer
	 *            NotNull
	 * @return NotNull
	 */
	public Offer save(Offer offer)
	{
		return offerRepository.save(offer);
	}

	//@formatter:off
	/**
	 * cancel an valid offer by giving <i>id</i> and <i>ownerId</i>
	 * 
	 * @param id
	 *            of {@link Offer}
	 * @param ownerId
	 *            of {@link Offer#getOwner()}
	 * @return cancelled {@link Offer}
	 * @throws NotFoundException
	 * <p>
	 * 	if with one or more of the following conditions:
	 * 	<i> Offer is not found by giving Id</i>
	 *  <i> Offer is not own by giving ownerId  </i>
	 *  <i> Offer has been cancelled or deleted  </i>
	 *  <i> Offer is expired  </i>
	 */
	//@formatter:on
	public Offer cancel(Long id, Long ownerId)
	{
		Offer offer = this.getValidOfferByIdAndOwnerId(id, ownerId);

		offer.setDeleted(true);
		return offerRepository.save(offer);
	}
}
