package nfq.bidding.repository;

import nfq.bidding.entity.Bidding;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiddingRepository extends CrudRepository<Bidding, Long> {
}