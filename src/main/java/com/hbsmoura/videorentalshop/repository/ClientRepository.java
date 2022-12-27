package com.hbsmoura.videorentalshop.repository;

import com.hbsmoura.videorentalshop.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Page<Client> findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(String textName, String textUsername, Pageable pageable);
}
