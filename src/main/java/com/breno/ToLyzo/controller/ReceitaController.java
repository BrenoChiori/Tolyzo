package com.breno.ToLyzo.controller;

import com.breno.ToLyzo.entity.ReceitaEntity;
import com.breno.ToLyzo.repository.ReceitaRepository;
import com.breno.ToLyzo.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receita")
public class ReceitaController {

    @Autowired
    private ReceitaRepository receitaRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody ReceitaEntity receitaEntity, HttpServletRequest request) {
        Integer idUser = (Integer)request.getAttribute("idUser");

        if (idUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        receitaEntity.setIdUser(idUser);

        var receita = this.receitaRepository.save(receitaEntity);
        return ResponseEntity.status(HttpStatus.OK).body(receita);
    }

    @GetMapping("/")
    public List<ReceitaEntity> listAll(HttpServletRequest request) {
        var userId = request.getAttribute("idUser");
        var receita = this.receitaRepository.findByIdUser((Integer) userId);
        return receita;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody ReceitaEntity receitaEntity, @PathVariable Integer id, HttpServletRequest request) {
        var receita = this.receitaRepository.findById(id).orElse(null);

        if(receita == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Receita não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(!receita.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar essa receita");
        }

        Utils.copyNonNullProperties(receitaEntity, receita);

        var receitaUpdated = this.receitaRepository.save(receita);
        return ResponseEntity.ok().body(receitaUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id, HttpServletRequest request) {
        var receita = this.receitaRepository.findById(id).orElse(null);

        if(receita == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Receita não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(!receita.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para deletar essa receita");
        }

        this.receitaRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Receita deletada");
    }
}
