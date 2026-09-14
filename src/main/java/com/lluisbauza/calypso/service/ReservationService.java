package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.dto.ReservationEmailData;
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
    private final EmailHtmlService emailHtmlService;

    public ReservationService(ReservationRepository reservationRepository, ClientService clientService, SlotService slotService, EmailHtmlService emailHtmlService) {
        this.reservationRepository = reservationRepository;
        this.clientService = clientService;
        this.slotService = slotService;
        this.emailHtmlService = emailHtmlService;
    }

    @Transactional
    public Reservation createReservation(ReservationRequest reservationRequest) {

        Client client = clientService.updateClient(reservationRequest);
        Slot slot = slotService.updateSlotBookedById(reservationRequest.getSlotId());

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setSlot(slot);
        reservation.setPax(reservationRequest.getPax());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setReservationCode(generateReservationCode(reservation));

        Reservation savedReservation = reservationRepository.save(reservation);
        if (savedReservation != null) {
            sendConfirmationHtmlEmail(savedReservation);
        }
        return savedReservation;

    }

    public void sendConfirmationHtmlEmail(Reservation reservation) {

        String name = reservation.getClient().getFirstName() +  " " + reservation.getClient().getLastName();
        String subject = "Reservation Confirmation - " + reservation.getReservationCode();

        ReservationEmailData data = new ReservationEmailData(
                name,
                reservation.getReservationCode(),
                reservation.getSlot().getDate(),
                reservation.getSlot().getDepartureTime(),
                reservation.getPax()
        );

        emailHtmlService.sendEmailWithHtml(reservation.getClient().getEmail(), subject, data);

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

        for (Reservation reservation : reservations) {
            if (reservation.getReservationCode().equals(reservationCode)) {
                return true;
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

    public Reservation getReservationByCodeAndEmail(String reservationCode, String email) {
        return reservationRepository.findByCodeAndEmail(reservationCode, email);
    }

}
