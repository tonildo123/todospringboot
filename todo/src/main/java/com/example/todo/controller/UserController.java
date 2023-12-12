package com.example.todo.controller;

import com.example.todo.dao.UserDao;
import com.example.todo.models.Users;
import com.example.todo.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController // se agrega para identificar un controlador
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/users", method = RequestMethod.GET) // nombre de la url ejemplo https://localhost:8080/users
    public List<Users> getUsuarios() {
        return userDao.getUsers();
    }

    @RequestMapping(value = "api/users/{id}", method = RequestMethod.GET) // nombre de la url ejemplo https://localhost:8080/users
    public Users getUsuarioById(@PathVariable Long id) {
        Users usuario = new Users();
        usuario.setId(id);
        usuario.setName("Tony Diaz");
        usuario.setEmail("tony81191@gmail.com");
        usuario.setPhone("3814757398");
        usuario.setPassword("Carlos8664*");

        return usuario;
    }
    @RequestMapping(value = "api/users/put/{id}", method = RequestMethod.PUT) // nombre de la url ejemplo https://localhost:8080/users
    public Users putUsuario() {
        Users usuario = new Users();
        usuario.setName("Tony Diaz");
        usuario.setEmail("tony81191@gmail.com");
        usuario.setPhone("3814757398");
        usuario.setPassword("Carlos8664*");

        return usuario;
    }
    @RequestMapping(value = "api/users/delete/{id}", method = RequestMethod.DELETE) // nombre de la url ejemplo https://localhost:8080/users
    public Users deleteUsuario(@PathVariable Long id) {

        userDao.eliminar(id);
        return null;
    }
    @RequestMapping(value = "api/users/create", method = RequestMethod.POST) // nombre de la url ejemplo https://localhost:8080/users
    public void postUsuario(@RequestBody Users users) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 1024, 1, users.getPassword());
        users.setPassword(hash);
        userDao.registrar(users);;
    }

    @RequestMapping(value = "api/login", method = RequestMethod.POST) // nombre de la url ejemplo https://localhost:8080/users
    public String postLogin(@RequestBody Users users) {
        Users user = userDao.login(users);

        if(user != null){
            String token = jwtUtil.create(user.getId().toString(), user.getEmail());
            return token;

        }else {
            return "NOOK";
        }
    }

}
