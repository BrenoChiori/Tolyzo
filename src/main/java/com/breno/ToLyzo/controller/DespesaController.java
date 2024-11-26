package com.breno.ToLyzo.controller;

import com.breno.ToLyzo.entity.DespesaEntity;
import com.breno.ToLyzo.repository.DespesaRepository;
import com.breno.ToLyzo.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/despesa")
public class DespesaController {

    @Autowired
    private DespesaRepository despesaRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody DespesaEntity despesaEntity, HttpServletRequest request) {
        Integer idUser = (Integer)request.getAttribute("idUser");

        if (idUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        despesaEntity.setIdUser(idUser);

        var despesa = this.despesaRepository.save(despesaEntity);
        return ResponseEntity.status(HttpStatus.OK).body(despesa);
    }

    @GetMapping("/")
    public List<DespesaEntity> listAll(HttpServletRequest request) {
        var userId = request.getAttribute("idUser");
        var despesa = this.despesaRepository.findByIdUser((Integer) userId);
        return despesa;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody DespesaEntity despesaEntity, @PathVariable Integer id, HttpServletRequest request) {
        var despesa = this.despesaRepository.findById(id).orElse(null);

        if(despesa == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Receita não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(!despesa.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar essa receita");
        }

        Utils.copyNonNullProperties(despesaEntity, despesa);

        var despesaUpdated = this.despesaRepository.save(despesa);
        return ResponseEntity.ok().body(despesaUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id, HttpServletRequest request) {
        var despesa = this.despesaRepository.findById(id).orElse(null);

        if(despesa == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Despesa não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(!despesa.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para deletar essa despesa");
        }

        this.despesaRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Despesa deletada");
    }
}
