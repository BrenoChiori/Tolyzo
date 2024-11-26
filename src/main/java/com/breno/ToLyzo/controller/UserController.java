package com.breno.ToLyzo.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.breno.ToLyzo.entity.UserEntity;
import com.breno.ToLyzo.repository.UserRepository;
import com.breno.ToLyzo.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserEntity userEntity) {
        var user = this.userRepository.findByEmail(userEntity.getEmail());
        if(user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
        }

        var passwordHashed = BCrypt.withDefaults().hashToString(12, userEntity.getPassword().toCharArray());
        userEntity.setPassword(passwordHashed);
        var userCreated = this.userRepository.save(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody UserEntity userEntity, @PathVariable Integer id, HttpServletRequest request) {
        var user = this.userRepository.findById(id).orElse(null);
        var userEmailExist = this.userRepository.findByEmail(userEntity.getEmail());

        if(user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario não encontrado");
        }

        if(userEmailExist != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O email já está sendo utilizado");
        }

        Utils.copyNonNullProperties(userEntity, user);

        user.setId(id);

        var userUpdate = this.userRepository.save(user);
        return ResponseEntity.ok().body(userUpdate);
    }
}
