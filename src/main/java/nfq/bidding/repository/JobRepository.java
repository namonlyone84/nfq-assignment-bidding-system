package nfq.bidding.repository;

import nfq.bidding.entity.Job;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends CrudRepository<Job, Long> {

    @Query(value = "SELECT j FROM #{#entityName} j WHERE j.user.id = :userId")
    List<Job> findAllByUserId(@Param("userId") long userId);
}