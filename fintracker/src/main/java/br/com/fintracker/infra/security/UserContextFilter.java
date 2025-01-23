package br.com.fintracker.infra.security;

import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserContextFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepository repository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            var usuario = (Usuario) repository.findByEmail(email);
            UserContext.setUserId(usuario.getId());
            //System.out.println("ID DO USUÁRIO NO CONTEXTO ATUAL: " + UserContext.getUserId());
        } catch (Exception ex) {
            UserContext.clear();
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
