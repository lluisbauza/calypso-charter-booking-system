package com.lluisbauza.calipso.dao;

import com.lluisbauza.calipso.model.Agency;

import java.sql.SQLException;
import java.util.List;

public interface CrudDao<T> {

    void create(T t) throws SQLException, ClassNotFoundException;
    T read(int id);
    void update(T t);
    void delete(int id);
    List<T> listAll();

}
