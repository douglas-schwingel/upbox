package br.com.upbox.controller;

import br.com.upbox.models.Usuario;
import br.com.upbox.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping(value = "/{username}")
    public Usuario buscaUsuario(@NotNull @PathVariable("username") String username) {
        return repository.busca(username);
    }

    @PostMapping
    public Usuario salva(@RequestBody @NotNull Usuario usuario) {
        return repository.salva(usuario);
    }

    @DeleteMapping
    public Usuario remove(@RequestBody @NotNull Usuario usuario) {
        return repository.remove(usuario);
    }


}
