package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.dto.ReservationRequest;
import com.lluisbauza.calypso.model.Client;
import com.lluisbauza.calypso.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    public ReservationRequest getReservationRequestBySlotIdAndClientEmail(Long slotId, String receivedEmail) {

        String email = normalizeEmail(receivedEmail);

        Optional<Client> client = clientRepository.findClientByEmail(email);

        var reservationRequest = new ReservationRequest();

        if (client.isPresent()) {
            reservationRequest.setSlotId(slotId);
            reservationRequest.setEmail(email);
            reservationRequest.setFirstName(client.get().getFirstName());
            reservationRequest.setLastName(client.get().getLastName());
            reservationRequest.setPhoneNumber(client.get().getPhoneNumber());

            return reservationRequest;
        }

        reservationRequest.setSlotId(slotId);
        reservationRequest.setEmail(email);

        return reservationRequest;

    }

    public Client updateClient(ReservationRequest reservationRequest) {

        String email = normalizeEmail(reservationRequest.getEmail());

        Optional<Client> client = clientRepository.findClientByEmail(email);

        if(client.isPresent()) {
            client.get().setFirstName(reservationRequest.getFirstName());
            client.get().setLastName(reservationRequest.getLastName());
            client.get().setPhoneNumber(reservationRequest.getPhoneNumber());

            return clientRepository.save(client.get());
        }

        Client newClient = new Client();
        newClient.setEmail(email);
        newClient.setFirstName(reservationRequest.getFirstName());
        newClient.setLastName(reservationRequest.getLastName());
        newClient.setPhoneNumber(reservationRequest.getPhoneNumber());

        return clientRepository.save(newClient);

    }

}
