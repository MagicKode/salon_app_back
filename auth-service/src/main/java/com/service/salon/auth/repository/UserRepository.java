package com.service.salon.auth.repository;

import com.service.salon.auth.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Spring Data JPA автоматически сгенерирует SQL-запрос выборки по username
    Optional<UserEntity> findByUsername(String username);

    // Проверка существования при регистрации
    boolean existsByUsername(String username);

}
