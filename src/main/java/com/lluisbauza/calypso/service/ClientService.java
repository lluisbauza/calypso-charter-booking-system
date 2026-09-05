package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.dto.ReservationRequest;
import com.lluisbauza.calypso.model.Client;
import com.lluisbauza.calypso.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Optional<Client> findClientByEmail(String email) {
        return clientRepository.findClientByEmail(email);
    }

    public ReservationRequest getReservationRequestBySlotIdAndClientEmail(Long slotId, String email) {

        Optional<Client> client = clientRepository.findClientByEmail(email);

        var reservationRequest = new ReservationRequest();

        if (client.isPresent()) {
            reservationRequest.setSlotId(slotId);
            reservationRequest.setEmail(email);
            reservationRequest.setClientId(client.get().getId());
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

        Optional<Client> client = clientRepository.findClientByEmail(reservationRequest.getEmail());

        if(client.isPresent()) {
            client.get().setFirstName(reservationRequest.getFirstName());
            client.get().setLastName(reservationRequest.getLastName());
            client.get().setPhoneNumber(reservationRequest.getPhoneNumber());

            return clientRepository.save(client.get());
        }

        Client newClient = new Client();
        newClient.setEmail(reservationRequest.getEmail());
        newClient.setFirstName(reservationRequest.getFirstName());
        newClient.setLastName(reservationRequest.getLastName());
        newClient.setPhoneNumber(reservationRequest.getPhoneNumber());

        return clientRepository.save(newClient);

    }

}
