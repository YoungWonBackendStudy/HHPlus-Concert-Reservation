package io.hhplus.concert.infra.reservation;

import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.domain.reservation.ReservationRepository;
import io.hhplus.concert.domain.reservation.ReservationTicket;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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
    public Reservation getAndLockById(Long id) {
        var reservationEntityOptional = this.reservationJpaRepository.findAndLockById(id);
        if(reservationEntityOptional.isEmpty()) throw new CustomNotFoundException(ExceptionCode.RESERVATION_NOT_FOUND);

        var reservationEntity = reservationEntityOptional.get();
        var ticketEntities = this.reservationTicketJpaRepository.findByReservationId(reservationEntity.getId());
        var ticketDomains = ticketEntities.stream().map(ReservationTicketEntity::toDomain).toList();
        return reservationEntity.toDomain(ticketDomains);
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        var reservationEntity = new ReservationEntity(reservation);
        reservationEntity = this.reservationJpaRepository.save(reservationEntity);
        var tickets = this.saveReservationTickets(reservation.getReservationTickets());
        return reservationEntity.toDomain(tickets);
    }

    @Override
    public List<ReservationTicket> saveReservationTickets(List<ReservationTicket> reservationTickets) {
        var ticketEntities = reservationTickets.stream().map(ReservationTicketEntity::new).toList();
        ticketEntities = this.reservationTicketJpaRepository.saveAll(ticketEntities);
        return ticketEntities.stream().map(ReservationTicketEntity::toDomain).toList();
    }

    @Override
    public List<Reservation> getReservedOver5minsAndStatusStillReserved() {
        var expiredReservations = reservationJpaRepository.findByStatusAndReservedAtBefore(Reservation.ReservationStatus.RESERVED, new Date(System.currentTimeMillis() - 5 * 60 * 1000L));
        var expiredTicketDomains = reservationTicketJpaRepository.findByReservationIdIn(expiredReservations.stream().map(ReservationEntity::getId).toList())
                .stream().map(ReservationTicketEntity::toDomain)
                .toList();

        return expiredReservations.stream().map(reservation -> {
            var tickets = expiredTicketDomains.stream().filter(ticket -> ticket.getReservationId().equals(reservation.getId())).toList();
            return reservation.toDomain(tickets);
        }).toList();
    }
}