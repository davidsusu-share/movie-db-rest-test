package hu.lorem.ipsum.mdf.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchEventRepository extends JpaRepository<SearchEventEntity, Long> {

}
