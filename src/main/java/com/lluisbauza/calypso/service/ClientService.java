package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.dto.ReservationRequest;
import com.lluisbauza.calypso.model.Client;
import com.lluisbauza.calypso.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client findClientByEmail(String email) {
        return clientRepository.findClientByEmail(email);
    }

    public ReservationRequest getReservationRequestBySlotIdAndClientEmail(Long slotId, String email) {

        var client = clientRepository.findClientByEmail(email);

        var reservationRequest = new ReservationRequest();
        reservationRequest.setSlotId(slotId);
        reservationRequest.setEmail(email);
        reservationRequest.setClientId(client.getId());
        reservationRequest.setFirstName(client.getFirstName());
        reservationRequest.setLastName(client.getLastName());
        reservationRequest.setPhoneNumber(client.getPhoneNumber());

        return reservationRequest;

    }

    public Client updateClient(ReservationRequest reservationRequest) {

        Client client = clientRepository.findClientByEmail(reservationRequest.getEmail());

        if(client == null) {
            Client newClient = new Client();
            newClient.setEmail(reservationRequest.getEmail());
            newClient.setFirstName(reservationRequest.getFirstName());
            newClient.setLastName(reservationRequest.getLastName());
            newClient.setPhoneNumber(reservationRequest.getPhoneNumber());
            clientRepository.save(newClient);
            return newClient;
        }

        client.setFirstName(reservationRequest.getFirstName());
        client.setLastName(reservationRequest.getLastName());
        client.setPhoneNumber(reservationRequest.getPhoneNumber());
        clientRepository.save(client);

        return client;

    }

}
