package com.breno.ToLyzo.controller;

import com.breno.ToLyzo.entity.ContasEntity;
import com.breno.ToLyzo.entity.ReceitaEntity;
import com.breno.ToLyzo.repository.ContasRepository;
import com.breno.ToLyzo.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContasController {

    @Autowired
    private ContasRepository contasRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody ContasEntity contasEntity, HttpServletRequest request) {
        Integer idUser = (Integer)request.getAttribute("idUser");

        if (idUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        contasEntity.setIdUser(idUser);

        var conta = this.contasRepository.save(contasEntity);
        return ResponseEntity.status(HttpStatus.OK).body(conta);
    }

    @GetMapping("/")
    public List<ContasEntity> listAll(HttpServletRequest request) {
        var userId = request.getAttribute("idUser");
        var conta = this.contasRepository.findByIdUser((Integer) userId);
        return conta;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody ContasEntity contasEntity, @PathVariable Integer id, HttpServletRequest request) {
        var conta = this.contasRepository.findById(id).orElse(null);

        if(conta == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conta não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(!conta.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar essa conta");
        }

        Utils.copyNonNullProperties(contasEntity, conta);

        var contaUpdated = this.contasRepository.save(conta);
        return ResponseEntity.ok().body(contaUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id, HttpServletRequest request) {
        var conta = this.contasRepository.findById(id).orElse(null);

        if(conta == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conta não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(!conta.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para deletar essa conta");
        }

        this.contasRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Conta deletada");
    }
}
