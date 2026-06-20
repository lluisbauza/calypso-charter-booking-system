package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Boat;

import java.sql.SQLException;
import java.util.List;

public class BoatDao implements CrudDao<Boat> {

    public BoatDao() {}

    @Override
    public void create(Boat boat) throws SQLException, ClassNotFoundException {

    }

    @Override
    public Boat read(int id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public void update(Boat boat) throws SQLException, ClassNotFoundException {

    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException {

    }

    @Override
    public List<Boat> listAll() throws SQLException, ClassNotFoundException {
        return List.of();
    }
}
