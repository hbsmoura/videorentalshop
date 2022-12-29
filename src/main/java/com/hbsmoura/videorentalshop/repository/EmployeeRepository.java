package com.hbsmoura.videorentalshop.repository;

import com.hbsmoura.videorentalshop.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Page<Employee> findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(
            String text, String text1, Pageable pageable
    );
}
