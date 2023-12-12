package com.example.todo.dao;

import com.example.todo.models.Users;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//inyeccion de dependencias para la conexion de base de datos
@Repository // hace referencia al acceso del repositorio de la base de datos
@Transactional // hace referencia a las consultas
public class UserImplementDao implements UserDao{

    @PersistenceContext // tambien sirve para lo relaciona a la base de datos
    EntityManager entityManager; // para eso agregamos las dependencias a mano de jdbc y mysql

    @Override
    @Transactional
    public List<Users> getUsers() {
        String query = "FROM Users";
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void eliminar(Long id) {
       Users user = entityManager.find(Users.class, id);
       entityManager.remove(user);
    }

    @Override
    public void registrar(Users users) {
        entityManager.merge(users);
    }

    @Override
    public Users login(Users users) {

        String query = "FROM Users WHERE email = :email ";
        List<Users> lista =  entityManager.createQuery(query)
                .setParameter("email", users.getEmail())
                .getResultList();

        if (lista.isEmpty()) {
            return null;
        }
        String passwordHashed = lista.get(0).getPassword();

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        if(argon2.verify(passwordHashed, users .getPassword())) {
            return lista.get(0);
        } else  return null;



    }
}
