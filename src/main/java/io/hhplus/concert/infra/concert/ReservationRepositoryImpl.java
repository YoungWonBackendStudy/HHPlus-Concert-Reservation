package io.hhplus.concert.infra.concert;

import io.hhplus.concert.domain.concert.Reservation;
import io.hhplus.concert.domain.concert.ReservationRepository;
import io.hhplus.concert.domain.concert.ReservationTicket;
import io.hhplus.concert.infra.concert.entity.ReservationEntity;
import io.hhplus.concert.infra.concert.entity.ReservationTicketEntity;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

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
    @Transactional
    public Reservation getAndLockById(Long id) {
        var reservationEntityOptional = this.reservationJpaRepository.findAndLockById(id);
        if(reservationEntityOptional.isEmpty()) throw new CustomNotFoundException(ExceptionCode.RESERVATION_NOT_FOUND);

        var reservationEntity = reservationEntityOptional.get();
        var ticketEntities = this.reservationTicketJpaRepository.findByReservationId(reservationEntity.getId());
        var ticketDomains = ticketEntities.stream().map(ReservationTicketEntity::toDomain).toList();
        return reservationEntity.toDomain(ticketDomains);
    }

    @Override
    @Transactional
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
    public List<ReservationTicket> getCompletedOrReservedUnder5mins(List<Long> seadIds) {
        return reservationTicketJpaRepository.findCompletedOrReservedUnder5minBySeatIds(seadIds)
                .stream().map(ReservationTicketEntity::toDomain)
                .toList();
    }

    @Override
    public List<ReservationTicket> getCompletedOrReservedUnder5mins(Long concertScheduleId) {
        return reservationTicketJpaRepository.findCompletedOrReservedUnder5minByConcertScheduleId(concertScheduleId)
                .stream().map(ReservationTicketEntity::toDomain)
                .toList();
    }
}