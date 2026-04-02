package com.richardDev.Auth.Service.Controller;

import com.richardDev.Auth.Service.Entity.UsuarioEntity;
import com.richardDev.Auth.Service.Repository.UsuarioRepository;
import com.richardDev.Auth.Service.Service.UsuarioService;
import com.richardDev.Auth.Service.Service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth_service")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioService service;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public List<UsuarioEntity> listar() {
        return repository.findAll();
    }

    @PostMapping("/cadastrar")
    public UsuarioEntity salvar(@RequestBody UsuarioEntity usuario) {
        return service.salvar(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioEntity dados) {
        try {
            var usuario = repository.findByEmail(dados.getEmail());

            if (usuario != null && encoder.matches(dados.getPassword(), usuario.getPassword())) {
                var token = tokenService.gerarToken(usuario);
                return ResponseEntity.ok(token);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno");
        }
    }
}