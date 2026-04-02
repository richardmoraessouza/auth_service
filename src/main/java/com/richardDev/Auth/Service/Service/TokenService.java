package com.richardDev.Auth.Service.Service;

import com.richardDev.Auth.Service.Entity.UsuarioEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

@Service
public class TokenService {
    @Value("${CHAVE_SECRETA}")

    private String secret;

    public String gerarToken(UsuarioEntity usuario) {
        return Jwts.builder()
                .setIssuer("Auth-Service")
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Token válido por 1 hora
                .signWith(hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }
}
