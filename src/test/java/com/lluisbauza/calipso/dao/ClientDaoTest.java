package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientDaoTest {

    private ClientDao clientDao;
    private int createdId;
    private String testClientMail;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        clientDao = new ClientDao();
        Client client = new Client("admin@calipso.com", "+34666777888", "Admin");
        clientDao.create(client);
        createdId = clientDao.findByMail("admin@calipso.com").getIdClient();
    }

    @Test
    void create_shouldInsertClient() throws SQLException, ClassNotFoundException {

        testClientMail = "info@calipso.com";

        Client client = new Client(testClientMail, "+34999888777", "Manager");
        clientDao.create(client);

        Client clientNew = clientDao.findByMail(testClientMail);

        assertNotNull(clientNew);
        assertEquals(testClientMail, clientNew.getMail());
        assertEquals("+34999888777", clientNew.getPhone());
        assertEquals("Manager", clientNew.getName());

    }

    @Test
    void read_shouldReturnClient() throws SQLException, ClassNotFoundException {

        Client client = clientDao.read(createdId);

        assertNotNull(client);

        assertEquals(createdId, client.getIdClient());
        assertEquals("+34666777888", client.getPhone());
        assertEquals("Admin", client.getName());

    }

    @Test
    void update_shouldModifyNameAndMailAndPhone() throws SQLException, ClassNotFoundException {

        Client client = clientDao.read(createdId);

        client.setMail("manager@calipso.com");
        client.setPhone("+34999888777");
        client.setName("Manager");

        clientDao.update(client);

        client = clientDao.read(createdId);

        assertNotNull(client);
        assertEquals("manager@calipso.com", client.getMail());
        assertEquals("+34999888777", client.getPhone());
        assertEquals("Manager", client.getName());

    }

    @Test
    void delete_shouldRemoveClient() throws SQLException, ClassNotFoundException {

        Client client = clientDao.read(createdId);

        assertNotNull(client);

        clientDao.delete(createdId);

        Client nullClient = clientDao.read(createdId);

        assertNull(nullClient);

        createdId = 0;

    }

    @Test
    void listAll_shouldCreateList() throws SQLException, ClassNotFoundException {
        List<Client> clients = clientDao.listAll();
        boolean found = false;

        for (Client client : clients) {
            if (client.getMail().equals("admin@calipso.com")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
        assertFalse(clients.isEmpty());

    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException {
        if (createdId != 0) {
            clientDao.delete(createdId);
        }

        if (testClientMail != null) {
            Client client = clientDao.findByMail(testClientMail);

            if (client != null) {
                clientDao.delete(client.getIdClient());
            }
        }
    }


}
