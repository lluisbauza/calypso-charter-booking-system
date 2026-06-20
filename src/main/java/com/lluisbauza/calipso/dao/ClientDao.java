package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Client;

import java.sql.SQLException;
import java.util.List;

public class ClientDao implements CrudDao<Client> {

    public ClientDao(){}

    @Override
    public void create(Client client) throws SQLException, ClassNotFoundException {

    }

    @Override
    public Client read(int id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public void update(Client client) throws SQLException, ClassNotFoundException {

    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {

    }

    @Override
    public List<Client> listAll() throws SQLException, ClassNotFoundException {
        return List.of();
    }
}
