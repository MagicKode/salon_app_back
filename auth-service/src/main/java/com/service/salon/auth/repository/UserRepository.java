package com.service.salon.auth.repository;

import com.service.salon.auth.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Spring Data JPA автоматически сгенерирует SQL-запрос выборки по phoneNumber
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT first_name FROM users WHERE phone_number = :phone", nativeQuery = true)
    Optional<String> findNameByPhone(@Param("phone") String phone);

    // Проверка существования при регистрации
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserEntity> findByEmail(String email);
}
