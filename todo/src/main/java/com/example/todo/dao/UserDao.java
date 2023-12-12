package com.example.todo.dao;

import com.example.todo.models.Users;

import java.util.List;

public interface UserDao {

    List<Users> getUsers();

    void eliminar(Long id);

    void registrar(Users users);

    Users login(Users users);
}
