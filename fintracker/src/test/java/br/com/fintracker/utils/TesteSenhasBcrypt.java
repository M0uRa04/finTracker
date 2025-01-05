package br.com.fintracker.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TesteSenhasBcrypt {

    public static void main(String[] args) {

        String senha = "senha1234";

        String senhaIncryptada = new BCryptPasswordEncoder().encode(senha);

        System.out.println(senhaIncryptada);
    }
}
