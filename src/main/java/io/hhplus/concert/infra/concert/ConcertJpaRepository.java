package io.hhplus.concert.infra.concert;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConcertJpaRepository extends JpaRepository<ConcertEntity, Long>{
    @Query(value = """
        SELECT con.id, con.name, con.description
        FROM concert con
        INNER JOIN concert_schedule sch
            ON con.id = sch.concert_id
        INNER JOIN concert_seat seat
            ON sch.id = seat.concert_schedule_id
        WHERE sch.reservation_st_date < CURDATE() 
            AND sch.reservation_end_date > CURDATE()
            AND seat.reserved = false
        GROUP BY con.id, con.name, con.description
        HAVING count(seat.id) > 0
    """, nativeQuery = true)
    List<ConcertEntity> findConcertsAvailableToday(Pageable pageable);
}
