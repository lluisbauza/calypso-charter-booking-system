package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.dto.ReservationBasicInfo;
import com.lluisbauza.calypso.dto.ReservationEditRequest;
import com.lluisbauza.calypso.dto.ReservationEmailData;
import com.lluisbauza.calypso.dto.ReservationRequest;
import com.lluisbauza.calypso.enums.ReservationStatus;
import com.lluisbauza.calypso.enums.SlotAvailability;
import com.lluisbauza.calypso.exception.CapacityExceededException;
import com.lluisbauza.calypso.exception.ReservationNotFoundException;
import com.lluisbauza.calypso.exception.SlotNotAvailableException;
import com.lluisbauza.calypso.exception.SlotNotFoundException;
import com.lluisbauza.calypso.model.Boat;
import com.lluisbauza.calypso.model.Client;
import com.lluisbauza.calypso.model.Reservation;
import com.lluisbauza.calypso.model.Slot;
import com.lluisbauza.calypso.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    private void checkCapacity(Slot slot, Integer pax) {
        Long boatCapacity = slot.getTrip().getBoat().getCapacity();
        if (boatCapacity < pax) {
            throw new CapacityExceededException(boatCapacity);
        }
    }

    @Transactional
    public Reservation createReservation(ReservationRequest reservationRequest) {

        Slot slotRequested = slotService.findById(reservationRequest.getSlotId());

        if (slotRequested == null) {
            throw new SlotNotFoundException("Slot not found");
        }

        if (slotRequested.getAvailability() != SlotAvailability.AVAILABLE) {
            throw new SlotNotAvailableException("Slot is not available");
        }

        checkCapacity(slotRequested, reservationRequest.getPax());

        Client client = clientService.updateClient(reservationRequest);
        Slot slot = slotService.updateSlotBookedById(reservationRequest.getSlotId());

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setSlot(slot);
        reservation.setPax(reservationRequest.getPax());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setReservationCode(generateReservationCode(reservation));

        Reservation savedReservation = reservationRepository.save(reservation);
        sendHtmlEmail(savedReservation);

        return savedReservation;

    }

    public void sendHtmlEmail(Reservation reservation) {

        String name = reservation.getClient().getFirstName() +  " " + reservation.getClient().getLastName();

        String subject = "";

        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            subject = "Reservation Confirmation - ";
        } else if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            subject = "Reservation Cancelled - ";
        }

        subject += reservation.getReservationCode();

        ReservationEmailData data = new ReservationEmailData(
                name,
                reservation.getReservationCode(),
                reservation.getSlot().getDate(),
                reservation.getSlot().getDepartureTime(),
                reservation.getPax(),
                reservation.getStatus()
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

    public Reservation getReservationByCodeAndEmailIfConfirmed(String reservationCode, String email) {
        return reservationRepository.findByCodeAndEmailIfConfirmed(reservationCode, email);
    }

    private Reservation getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);

        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation not found");
        }

        return reservation;
    }

    private Reservation getReservationByIdIfConfirmed(Long reservationId) {
        Reservation reservation = reservationRepository.findByIdIfConfirmed(reservationId).orElse(null);

        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation not found or already cancelled");
        }

        return reservation;
    }

    public LocalDate getReservationDate(Long reservationId) {
        return getReservationById(reservationId).getSlot().getDate();
    }

    public String getReservationCode(Long reservationId) {
        return getReservationById(reservationId).getReservationCode();
    }

    public Boat getBoatByReservationId(Long reservationId) {
        return getReservationById(reservationId).getSlot().getTrip().getBoat();
    }

    public Slot getSlotByReservationId(Long reservationId) {
        return getReservationById(reservationId).getSlot();
    }

    public ReservationBasicInfo getReservationBasicInfo(Long reservationId) {

        Reservation reservation = getReservationById(reservationId);

        return new ReservationBasicInfo(
                reservation.getReservationCode(),
                reservation.getSlot().getDate(),
                reservation.getSlot().getDepartureTime(),
                reservation.getSlot().getTrip().getBoat().getBoatName()
        );
    }

    public ReservationBasicInfo getNewReservationInfo(Long reservationId, Long newSlotId) {

        Reservation reservation = getReservationById(reservationId);

        Slot slot = slotService.findById(newSlotId);

        if(slot == null) {
            throw new SlotNotFoundException("Slot not found");
        }

        return new ReservationBasicInfo(
                reservation.getReservationCode(),
                slot.getDate(),
                slot.getDepartureTime(),
                slot.getTrip().getBoat().getBoatName()
        );
    }

    @Transactional
    public Reservation updateReservationSlot(Long reservationId, Long slotId) {

        Reservation reservation = getReservationByIdIfConfirmed(reservationId);

        Slot oldSlot = reservation.getSlot();
        Slot newSlot = slotService.findById(slotId);

        checkCapacity(newSlot, reservation.getPax());

        if(newSlot == null) {
            throw new SlotNotFoundException("Slot not found");
        }

        if (oldSlot.getId().equals(newSlot.getId())) {
            return reservation;
        }

        if (newSlot.getAvailability() != SlotAvailability.AVAILABLE) {
            throw new SlotNotAvailableException("Slot not available");
        }

        oldSlot.setAvailability(SlotAvailability.AVAILABLE);
        newSlot.setAvailability(SlotAvailability.BOOKED);

        reservation.setSlot(newSlot);

        Reservation modifiedReservation = reservationRepository.save(reservation);

        sendHtmlEmail(modifiedReservation);

        return modifiedReservation;

    }

    public ReservationEditRequest getReservationEditRequest(Long reservationId) {

        Reservation reservation = getReservationByIdIfConfirmed(reservationId);

        var client = reservation.getClient();

        return new ReservationEditRequest(
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getPhoneNumber(),
                reservation.getPax()
        );
    }

    public Reservation updateReservationInformation(Long reservationId, ReservationEditRequest reservationEditRequest) {

        Reservation reservation = getReservationByIdIfConfirmed(reservationId);

        reservation.getClient().setFirstName(reservationEditRequest.firstName());
        reservation.getClient().setLastName(reservationEditRequest.lastName());
        reservation.getClient().setPhoneNumber(reservationEditRequest.phoneNumber());
        reservation.setPax(reservationEditRequest.pax());

        Reservation modifiedReservation = reservationRepository.save(reservation);

        sendHtmlEmail(modifiedReservation);

        return modifiedReservation;

    }

    @Transactional
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = getReservationByIdIfConfirmed(reservationId);

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.getSlot().setAvailability(SlotAvailability.AVAILABLE);

        var cancelledReservation = reservationRepository.save(reservation);
        sendHtmlEmail(cancelledReservation);
        return cancelledReservation;

    }

}
