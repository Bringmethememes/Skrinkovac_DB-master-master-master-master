package com.example.demo.repo;

import com.example.demo.domain.Locker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface LockerRepo extends JpaRepository<Locker,Long>, JpaSpecificationExecutor<Locker> {
    Locker findByNumber(Integer number);
    Locker findAllById (Long id);

}
