package com.breno.ToLyzo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity(name = "tb_despesa")
public class DespesaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String descricao;
    private Double valor;
    private String categoria;
    private Integer idUser;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataDespesa;

    @CreationTimestamp
    private LocalDateTime createdAt;
}