package edu.awieclawski.daos;

import edu.awieclawski.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}