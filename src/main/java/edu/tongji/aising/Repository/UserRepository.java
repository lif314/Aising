package edu.tongji.aising.Repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findUsersByUserNameAndUserPasswd(String name, String password);
}

/**
 * Spring automatically implements this repository interface in a bean that
 * has the same name (with a change in the case
 * it is called userRepository).
 */