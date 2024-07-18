package io.hhplus.concert.infra.reservation;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.domain.reservation.ReservationRepository;
import io.hhplus.concert.domain.reservation.ReservationTicket;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import jakarta.transaction.Transactional;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {
    ReservationJpaRepository reservationJpaRepository;
    ReservationTicketJpaRepository reservationTicketJpaRepository;
    

    public ReservationRepositoryImpl(ReservationJpaRepository reservationJpaRepository,
            ReservationTicketJpaRepository reservationTicketJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
        this.reservationTicketJpaRepository = reservationTicketJpaRepository;
    }

    @Override
    @Transactional
    public Reservation saveReservation(Reservation reservation) {
        var entity = new ReservationEntity(reservation);
        entity = this.reservationJpaRepository.save(entity);

        return entity.toDomain(null);
    }

    @Override
    public List<ReservationTicket> saveReservationTickets(List<ReservationTicket> reservationTickets) {
        var ticketEntities = reservationTickets
            .stream().map(ReservationTicketEntity::new)
            .toList();
        
        ticketEntities = this.reservationTicketJpaRepository.saveAll(ticketEntities);
        var ticketDomains = ticketEntities.stream().map(ReservationTicketEntity::toDomain).toList();
        return ticketDomains;
    }

    @Override
    @Transactional
    public Reservation getAndLockById(long reservationId) {
        var reservationEntityOptional = this.reservationJpaRepository.findAndLockById(reservationId);
        if(!reservationEntityOptional.isPresent()) throw new CustomNotFoundException(ExceptionCode.RESERVATION_NOT_FOUND);

        var reservationEntity = reservationEntityOptional.get();
        var ticketEntities = this.reservationTicketJpaRepository.findByReservationId(reservationEntity.getId());
        var ticketDomains = ticketEntities.stream().map(ReservationTicketEntity::toDomain).toList();
        return reservationEntity.toDomain(ticketDomains);
    }

    @Override
    @Transactional
    public List<ReservationTicket> getReservedTicketsByConcertScheduleId(long concertScheduleId) {
        return this.reservationTicketJpaRepository.findByConcertScheduleId(concertScheduleId)
            .stream().map(ReservationTicketEntity::toDomain)
            .toList();
    }
    
}
