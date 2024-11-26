package com.breno.ToLyzo.controller;

import com.breno.ToLyzo.entity.ListaDesejoEntity;
import com.breno.ToLyzo.repository.ListaDesejoRepository;
import com.breno.ToLyzo.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listadesejo")
public class ListaDesejoController {

    @Autowired
    private ListaDesejoRepository listaDesejoRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody ListaDesejoEntity listaDesejoEntity, HttpServletRequest request) {
        Integer idUser = (Integer)request.getAttribute("idUser");

        if (idUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        listaDesejoEntity.setIdUser(idUser);

        var listaDesejo = this.listaDesejoRepository.save(listaDesejoEntity);
        return ResponseEntity.status(HttpStatus.OK).body(listaDesejo);
    }

    @GetMapping("/")
    public List<ListaDesejoEntity> listAll(HttpServletRequest request) {
        var userId = request.getAttribute("idUser");
        var listaDesejo = this.listaDesejoRepository.findUserByIdUser((Integer) userId);
        return listaDesejo;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody ListaDesejoEntity listaDesejoEntity, @PathVariable Integer id, HttpServletRequest request) {
        var listaDesejo = this.listaDesejoRepository.findById(id).orElse(null);

        if(listaDesejo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(!listaDesejo.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar esse item");
        }

        Utils.copyNonNullProperties(listaDesejoEntity, listaDesejo);

        var listaDesejoUpdated = this.listaDesejoRepository.save(listaDesejo);
        return ResponseEntity.ok().body(listaDesejoUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id, HttpServletRequest request) {
        var listaDesejo = this.listaDesejoRepository.findById(id).orElse(null);

        if(listaDesejo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(!listaDesejo.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para deletar esse item");
        }

        this.listaDesejoRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Item deletado");
    }

}
