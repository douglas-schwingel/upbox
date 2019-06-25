package br.com.upbox.controller;

import br.com.upbox.models.Usuario;
import br.com.upbox.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@Api(tags = "Usuario", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequestMapping("/usuario")
public class UsuarioController {
    private static final Logger logger = Logger.getLogger(UsuarioController.class.getName());

    @Autowired
    private UsuarioRepository repository;

    @ApiOperation(value = "Busca Usuario")
    @GetMapping(value = "/{username}")
    public Usuario buscaUsuario(@NotNull @PathVariable("username") String username) {
        return repository.busca(username);
    }

    @ApiOperation(value = "Salva Usuario")
    @PostMapping
    public String salvaUsuario(@RequestBody @NotNull String json) {
        Usuario usuario = null;
        logger.log(Level.INFO, "Json recebido: {0}", json);
        try {
            usuario = new ObjectMapper().readValue(json, Usuario.class);
            logger.log(Level.INFO, "Usuario mapeado: {0}", usuario.getUsername());
        } catch (IOException e) {
            e.printStackTrace();

        }
        return repository.salva(usuario);
    }

    @ApiOperation(value = "Remove Usuario")
    @DeleteMapping
    public Usuario removeUsuario(@RequestBody @NotNull Usuario usuario) {
        return repository.remove(usuario);
    }

    @ApiOperation(value = "Atualiza Usuario")
    @PatchMapping("/{username}")
    public Usuario atualizaUsuario(@NotNull@PathVariable("username") String username,
                                @RequestBody @NotNull Usuario usuario) {return repository.atualiza(username, usuario);}


}
