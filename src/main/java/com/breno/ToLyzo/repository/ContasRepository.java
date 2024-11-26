package com.breno.ToLyzo.repository;

import com.breno.ToLyzo.entity.ContasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContasRepository extends JpaRepository<ContasEntity, Integer> {
    List<ContasEntity> findByIdUser(Integer idUser);
}
