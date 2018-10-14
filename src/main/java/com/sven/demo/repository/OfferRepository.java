package com.sven.demo.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sven.demo.model.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long>
{

	List<Offer> findAllByOwnerIdAndIsDeletedFalseAndExpiryDateAfter(Long ownerId, Date now);

	Optional<Offer> findByIdAndOwnerIdAndIsDeletedFalseAndExpiryDateAfter(Long id, Long ownerId, Date now);
}
