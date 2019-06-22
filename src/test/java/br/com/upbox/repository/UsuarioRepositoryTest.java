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
    public void setUp() {
        repository = new UsuarioRepository();
        usuario = new Usuario();
        usuario.setNome("Isadora Rizzato");
        usuario.setUuid(UUID.randomUUID());
        usuario.setDataNascimento(LocalDate.of(1997, Month.APRIL, 17));
        usuario.setEmail("isadorarpoh@gmail.com");
        usuario.setUsername("mimipohlmann");
        usuario.setSenha("123mudar");
    }

    @Test
    public void deveSalvarOUsuarioNoBanco() {
        Usuario resultado = repository.salva(usuario);
        assertNull(resultado);
    }

    @Test
    public void deveRetornarOUsuarioIsadoraDoBanco() {
        Usuario mimipohlmann = repository.busca("mimipohlmann");

        assertEquals("isadorarpoh@gmail.com", mimipohlmann.getEmail());
        assertEquals("Isadora Rizzato", mimipohlmann.getNome());
    }

    @Test
    public void deveSalvarUmUsuarioNoBancoEDepoisApagarEle() {
        var joseSilva = new Usuario();
        joseSilva.setNome("José da Silva");
        joseSilva.setUuid(UUID.randomUUID());
        joseSilva.setDataNascimento(LocalDate.of(1960, Month.APRIL, 15));
        joseSilva.setEmail("josesilva@gmail.com");
        joseSilva.setUsername("josesilva");
        joseSilva.setSenha("123mudar");

        Usuario resultadoSalva = repository.salva(joseSilva);
        assertNotNull(resultadoSalva);
        assertEquals(LocalDate.of(1960, Month.APRIL, 15), resultadoSalva.getDataNascimento());

        Usuario resultadoRemove = repository.remove(joseSilva);
        assertNotNull(resultadoRemove);
        assertEquals("José da Silva", resultadoRemove.getNome());
    }

    @Test
    public void deveAtualizarOUsuarioDeAcordoComOPassado() {
        Usuario novo = new Usuario();

        novo.setNome("Julio Juras");
        novo.setUuid(UUID.randomUUID());
        novo.setDataNascimento(LocalDate.of(2000, Month.JUNE, 5));
        novo.setEmail("julio@juras.com");
        novo.setUsername("jurasjulio");
        novo.setSenha("123mudar");

        repository.salva(novo);

        assertNotNull(repository.busca(novo.getUsername()));

        Usuario atualizacao = new Usuario();
        atualizacao.setUsername("juliojurei");
        atualizacao.setSenha("agoravai");

        repository.atualiza(novo.getUsername(), atualizacao);
        assertNull(repository.busca("jurasjulio"));
        assertNotNull(repository.busca("juliojurei"));
        assertEquals("julio@juras.com", repository.busca("juliojurei").getEmail());
        repository.remove("juliojurei");
        assertNull(repository.busca("juliojurei"));
    }



}