package br.com.fintracker.infra.security;

public class UserContext {
    private static final ThreadLocal<Long> userId = new ThreadLocal<>();

    @Autowired
    private UsuarioRepository usuarioRepository;

    public static void setUserId(Long id) {
        userId.set(id);
    }

    public static Long getUserId() {
        return userId.get();
    }

    public static Usuario getUsuario () {
        return usuarioRepository.findById(getUserId());
    }

    public static void clear() {
        userId.remove();
    }
}
