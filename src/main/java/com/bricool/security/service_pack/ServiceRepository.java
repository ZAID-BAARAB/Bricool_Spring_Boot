package com.bricool.security.service_pack;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<MyService, Integer> {
    List<MyService> findByCategory(String category);
    Optional<MyService> findByIdAndServiceProvider(Integer serviceId, Integer userId);
}