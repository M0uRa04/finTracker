package br.com.fintracker.infra.security;

import br.com.fintracker.model.usuario.Usuario;
import br.com.fintracker.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

public class UserContext {
    private static final ThreadLocal<Long> userId = new ThreadLocal<>();

    public static void setUserId(Long id) {
        userId.set(id);
    }

    public static Long getUserId() {
        return userId.get();
    }

    public static void clear() {
        userId.remove();
    }
}
