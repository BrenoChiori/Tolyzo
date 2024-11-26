package com.breno.ToLyzo.repository;

import com.breno.ToLyzo.entity.DespesaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DespesaRepository extends JpaRepository<DespesaEntity, Integer> {
    List<DespesaEntity> findByIdUser(Integer idUser);
}