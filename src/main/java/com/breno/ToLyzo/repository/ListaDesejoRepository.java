package com.breno.ToLyzo.repository;

import com.breno.ToLyzo.entity.ListaDesejoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListaDesejoRepository extends JpaRepository<ListaDesejoEntity, Integer> {
    List<ListaDesejoEntity> findUserByIdUser(Integer idUser);
}
