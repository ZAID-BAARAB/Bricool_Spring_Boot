package com.bricool.security.service_pack;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<MyService, Integer> {
    List<MyService> findByCategory(String category);
    Optional<MyService> findByIdAndServiceProvider(Integer serviceId, Integer userId);
    @Query(value = "SELECT * FROM service ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<MyService> findRandomServices(@Param("limit") int limit);

    @Query(value = "SELECT * FROM service WHERE category = :category ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<MyService> findLimitedServicesByCategory(@Param("category") String category, @Param("limit") int limit);

    List<MyService> findByServiceProvider(Integer serviceProvider);
}