package edu.greenriver.sdev.capstone.fsmp.repository;

import edu.greenriver.sdev.capstone.fsmp.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

    Optional<Administrator> getAdministratorByUsername(String username);

}
