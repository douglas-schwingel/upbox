package br.com.upbox.repository;

import br.com.upbox.models.Usuario;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.junit.Assert.*;

public class UsuarioRepositoryTest {

    private UsuarioRepository repository;
    private Usuario usuario;

    @Before
    public void setUṕ() {
        repository = new UsuarioRepository();
        usuario = new Usuario();
        usuario.setNome("Isadora Rizzato");
        usuario.setUuid(UUID.randomUUID());
        usuario.setDataNascimento(LocalDate.of(1997, Month.APRIL, 17));
        usuario.setEmail("isadorarpoh@gmail.com");
        usuario.setUsername("mimipohlmann");
        usuario.setSenha("123mudar");
    }

//    @Test
//    public void deveSalverOUsuarioNoBanco() {
//        repository.salva(usuario);
//    }

    @Test
    public void deveRetornarOUsuarioIsadoraDoBanco() {
        Usuario mimipohlmann = repository.busca("mimipohlmann");

        assertTrue(mimipohlmann.getEmail().equals("isadorarpoh@gmail.com"));
        assertEquals("Isadora Rizzato", mimipohlmann.getNome());
    }



}