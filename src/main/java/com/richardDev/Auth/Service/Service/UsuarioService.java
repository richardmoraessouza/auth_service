package com.richardDev.Auth.Service.Service;

import com.richardDev.Auth.Service.Entity.UsuarioEntity;
import com.richardDev.Auth.Service.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    public BCryptPasswordEncoder encoder;

    public UsuarioEntity salvar(UsuarioEntity usuario) {
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    public boolean login (String email, String senha) {
        UsuarioEntity user = repository.findByEmail(email);
        if (user == null) return false;

        return encoder.matches(senha, user.getPassword());
    }
}
