package com.breno.ToLyzo.repository;

import com.breno.ToLyzo.entity.ReceitaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceitaRepository extends JpaRepository<ReceitaEntity, Integer> {
    List<ReceitaEntity> findByIdUser(Integer idUser);
}