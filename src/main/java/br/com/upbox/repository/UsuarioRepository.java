package br.com.upbox.repository;

import br.com.upbox.codec.UsuarioCodec;
import br.com.upbox.models.Usuario;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.logging.Level;
import java.util.logging.Logger;


@Repository
public class UsuarioRepository {
    private static final Logger logger = Logger.getLogger(UsuarioRepository.class.getName());
    private static final String PARAM_BUSCA = "username";
    private static final String URL_MONGO = "mongodb://admin:upb0x4dm1n@ds343127.mlab.com:43127/heroku_d42tsbq0";
    private static final String MONGO_DB = "heroku_d42tsbq0";

    private MongoClient client;
    private MongoCollection<Usuario> collection;


    private void conecta() {
        Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
        UsuarioCodec usuarioCodec = new UsuarioCodec(codec);

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(usuarioCodec));

        MongoClientOptions options = MongoClientOptions.builder().codecRegistry(codecRegistry).build();
        this.client = new MongoClient(URL_MONGO, options);
        this.collection = client.getDatabase(MONGO_DB).getCollection("usuarios", Usuario.class);
    }


    /**
     * @param usuario - usuario a ser salvo
     * @return Retorna o usuário salvo no banco ou null se o usuário já estiver cadastrado
     * @apiNote Salva um usuário no banco caso ainda não exista nenhum
     * usuário com o mesmo username.
     */
    public Usuario salva(Usuario usuario) {
        if (verificaSeUsuarioJaExiste(usuario.getUsername())) return null;
        conecta();
        collection.insertOne(usuario);
        logger.log(Level.INFO, "Salvando no banco: " + usuario.getNome());
        client.close();
        return usuario;
    }

    /**
     * @param username - Nome do usuário a ser encontrado
     * @return O Usuario que possui o username passado ou null caso não exista
     * @apiNote Busca um usuário no banco de acordo com seu username
     */
    public Usuario busca(String username) {
        conecta();
        MongoCursor<Usuario> cursor = collection.find(Filters.eq(PARAM_BUSCA, username), Usuario.class).iterator();
        if (cursor.hasNext()) {
            Usuario resultado = cursor.next();
            client.close();
            return resultado;
        }
        logger.log(Level.INFO, "Usuário {0} não encontrado", username);
        client.close();
        return null;
    }

    /**
     * @param usuario - usuario a ser removido
     * @return Usuario deletado ou null caso não exista nenhum usuario com o mesmo username no banco
     * @apiNote Remove um usuário baseado no username passado
     */
    public Usuario remove(Usuario usuario) {
        if (!verificaSeUsuarioJaExiste(usuario.getUsername())) {
            logger.log(Level.INFO, "Usuário {0} não existe.", usuario.getUsername());
            return null;
        }
        conecta();
        logger.log(Level.INFO, "Apagando usuário {0}", usuario.getUsername());
        Usuario resultado = collection.findOneAndDelete(Filters.eq(PARAM_BUSCA, usuario.getUsername()));
        client.close();
        return resultado;
    }

    /**
     * @param username - username do usuario a ser removido
     * @return Usuario deletado ou null caso não exista nenhum usuario com o mesmo username no banco
     * @apiNote Remove um usuário baseado no username passado
     */
    public Usuario remove(String username) {
        if (!verificaSeUsuarioJaExiste(username)) {
            logger.log(Level.INFO, "Usuário {0} não existe.", username);
            return null;
        }
        conecta();
        logger.log(Level.INFO, "Apagando usuário {0}", username);
        Usuario resultado = collection.findOneAndDelete(Filters.eq(PARAM_BUSCA, username));
        client.close();
        return resultado;
    }


    /**
     * @param username - Username do usuario que vai ser atualizado
     * @param usuario  - Usuario com os dados novos
     * @return Usuario que foi alterado com seus campos anteriores à alteração.
     * @apiNote Atualiza um usuário do banco baseado em outro
     */
    public Usuario atualiza(String username, Usuario usuario) {
        if (!verificaSeUsuarioJaExiste(username)) {
            logger.log(Level.INFO, "Usuário {0} não encontrado", username);
            return null;
        }
        conecta();
        Bson filter = new Document(PARAM_BUSCA, username);
        Bson uptade = verificaDadosParaAtualizacao(usuario);
        Usuario usuarioAntigo = collection.findOneAndUpdate(filter, uptade);
        logger.log(Level.INFO, "Atualizado: {0}", usuarioAntigo.getUsername());
        return usuarioAntigo;
    }

    private Document verificaDadosParaAtualizacao(Usuario usuario) {
        Document document = new Document();

        if (usuario.getUsername() != null) document.put("username", usuario.getUsername());
        if (usuario.getUuid() != null) document.put("uuid", usuario.getUuid());
        if (usuario.getNome() != null) document.put("nome", usuario.getNome());
        if (usuario.getEmail() != null) document.put("email", usuario.getEmail());
        if (usuario.getSenha() != null) document.put("senha", usuario.getSenha());
        if (usuario.getId() != null) document.put("_id", usuario.getId());
        if (usuario.getDataNascimento() != null) document.put("dataNascimento", usuario.getDataNascimento());
        return new Document("$set", document);
    }


    //    Métodos auxiliares
    private boolean verificaSeUsuarioJaExiste(String username) {
        if (busca(username) != null) {
            logger.log(Level.INFO, "Usuario já cadastrado: {0}", username);
            client.close();
            return true;
        }
        return false;
    }
}
