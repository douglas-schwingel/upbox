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
import org.springframework.stereotype.Repository;

import java.util.logging.Level;
import java.util.logging.Logger;


@Repository
public class UsuarioRepository {
    private static final Logger logger = Logger.getLogger(UsuarioRepository.class.getName());

    private MongoClient client;
    private MongoCollection<Usuario> collection;


    public void conecta() {
        Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
        UsuarioCodec usuarioCodec = new UsuarioCodec(codec);

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(usuarioCodec));

        MongoClientOptions options = MongoClientOptions.builder().codecRegistry(codecRegistry).build();
        this.client = new MongoClient("localhost:27017", options);
        this.collection = client.getDatabase("upbox").getCollection("usuarios", Usuario.class);
    }


    /**
     *
     * @param usuario
     * @apiNote Salva um usuário no banco caso ainda não exista nenhum
     *          usuário com o mesmo username.
     * @return Retorna o usuário salvo no banco ou null se o usuário já estiver cadastrado
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
     *
     * @param username
     * @apiNote Busca um usuário no banco de acordo com seu username
     * @return O Usuario que possui o username passado ou null caso não exista
     */
    public Usuario busca(String username) {
        conecta();
        MongoCursor<Usuario> cursor = collection.find(Filters.eq("username", username), Usuario.class).iterator();
        if(cursor.hasNext()) {
            Usuario resultado = cursor.next();
            client.close();
            return resultado;
        }
        logger.log(Level.INFO, "Usuário " + username + " não encontrado");
        client.close();
        return null;
    }

    /**
     *
     * @param username
     * @apiNote Remove um usuário baseado no username passado
     * @return Usuario deletado ou null caso não exista nenhum usuario com o mesmo username no banco
     */
    public Usuario remove(String username) {
        if(!verificaSeUsuarioJaExiste(username)) {
            logger.log(Level.INFO, "Usuário " + username + " não existe.");
            return null;
        }
        conecta();
        logger.log(Level.INFO, "Apagando usuário " + username);
        Usuario resultado = collection.findOneAndDelete(Filters.eq("username", username));
        client.close();
        return resultado;
    }


//    Métodos auxiliares
    private boolean verificaSeUsuarioJaExiste(String username) {
        if(busca(username) != null) {
            logger.log(Level.INFO, "Usuario " + username + " já cadastrado");
            client.close();
            return true;
        }
        return false;
    }
}
