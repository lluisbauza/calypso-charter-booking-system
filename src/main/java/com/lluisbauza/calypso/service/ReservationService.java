package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.dto.ReservationRequest;
import com.lluisbauza.calypso.enums.ReservationStatus;
import com.lluisbauza.calypso.model.Client;
import com.lluisbauza.calypso.model.Reservation;
import com.lluisbauza.calypso.model.Slot;
import com.lluisbauza.calypso.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientService clientService;
    private final SlotService slotService;

    public ReservationService(ReservationRepository reservationRepository, ClientService clientService, SlotService slotService) {
        this.reservationRepository = reservationRepository;
        this.clientService = clientService;
        this.slotService = slotService;
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation createReservation(ReservationRequest reservationRequest) {

        Client client = clientService.updateClient(reservationRequest);
        Slot slot = slotService.updateSlotBookedById(reservationRequest.getSlotId());

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setSlot(slot);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setReservationCode(generateReservationCode(reservation));

        return reservationRepository.save(reservation);

    }

    private String generateReservationCode(Reservation reservation) {

        int counter = 1;

        String reservationCode = reservation.getSlot().getTrip().getBoat().getBoatName().substring(0,2).toUpperCase()
                + "-" + reservation.getSlot().getDate().getYear() +
                reservation.getSlot().getDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).substring(0, 3).toUpperCase()
                + "-" + randomLetters(3);

        String currentReservationCode = reservationCode;

        while(reservationCodeExists(currentReservationCode)) {
            currentReservationCode = reservationCode + counter;
            counter++;
        }
        return currentReservationCode;

    }

    private boolean reservationCodeExists(String reservationCode) {

        List<Reservation> reservations = reservationRepository.findAll();
        boolean exists = false;

        for (Reservation reservation : reservations) {
            if (reservation.getReservationCode().equals(reservationCode)) {
                exists = true;
            }
        }

        return false;

    }

    private String randomLetters(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append((char) ThreadLocalRandom.current().nextInt('A', 'Z' + 1));
        }

        return sb.toString();
    }

}
