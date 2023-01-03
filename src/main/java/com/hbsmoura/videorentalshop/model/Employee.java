package com.hbsmoura.videorentalshop.model;

import com.hbsmoura.videorentalshop.enums.EnumUserRole;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * The Employee class represents de model entity for the shop employees.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Employee extends User{

    private boolean manager;

    @Override
    public Collection<EnumUserRole> getAuthorities() {
        if (manager) return Arrays.asList(EnumUserRole.ROLE_EMPLOYEE, EnumUserRole.ROLE_MANAGER);
        return Collections.singletonList(EnumUserRole.ROLE_EMPLOYEE);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Employee employee = (Employee) o;
        return getId() != null && Objects.equals(getId(), employee.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Employee{" + super.toString() +
                ", " + "manager=" + manager + '}';
    }
}
