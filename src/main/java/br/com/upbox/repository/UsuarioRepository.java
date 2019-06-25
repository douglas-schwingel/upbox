package br.com.upbox.repository;

import br.com.upbox.codec.UsuarioCodec;
import br.com.upbox.models.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


@Repository
public class UsuarioRepository {
    private static final Logger logger = Logger.getLogger(UsuarioRepository.class.getName());
    private static final String PARAM_BUSCA = "username";
    private static final String URL_MONGO = "localhost:27017";
    private static final String MONGO_DB = "upbox";

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
     * @return Retorna um json do usuario ou null se o usuário já estiver cadastrado
     * @apiNote Salva um usuário no banco caso ainda não exista nenhum
     * usuário com o mesmo username.
     */
    public String salva(Usuario usuario) {
        Document document = populaJsonString(usuario);
        if (verificaSeUsuarioJaExiste(usuario.getUsername())) return document.toJson()      ;
        conecta();
        collection.insertOne(usuario);
        logger.log(Level.INFO, "Salvando no banco: " + usuario.getNome());
        client.close();
        return document.toJson();
    }

    /**
     * @param username - Nome do usuário a ser encontrado
     * @return O Usuario que possui o username passado ou null caso não exista
     * @apiNote Busca um usuário no banco de acordo com seu username
     */
    public String busca(String username) {
        conecta();
        MongoCursor<Usuario> cursor = collection.find(Filters.eq(PARAM_BUSCA, username), Usuario.class).iterator();
        if (cursor.hasNext()) {
            Usuario resultado = cursor.next();
            client.close();
            Document document = populaJsonString(resultado);
            return document.toJson();
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
    public String remove(Usuario usuario) {
        if (!verificaSeUsuarioJaExiste(usuario.getUsername())) {
            logger.log(Level.INFO, "Usuário {0} não existe.", usuario.getUsername());
            return null;
        }
        conecta();
        logger.log(Level.INFO, "Apagando usuário {0}", usuario.getUsername());
        Usuario resultado = collection.findOneAndDelete(Filters.eq(PARAM_BUSCA, usuario.getUsername()));
        client.close();
        Document document = populaJsonString(resultado);
        return document.toJson();
    }

    /**
     * @param username - Username do usuario que vai ser atualizado
     * @param usuario  - Usuario com os dados novos
     * @return Usuario com os dados novos.
     * @apiNote Atualiza um usuário do banco baseado em outro
     */
    public String atualiza(String username, Usuario usuario) {
        if (!verificaSeUsuarioJaExiste(username)) {
            logger.log(Level.INFO, "Usuário {0} não encontrado", username);
            return null;
        }
        conecta();
        Bson filter = new Document(PARAM_BUSCA, username);
        Bson uptade = verificaDadosParaAtualizacao(usuario);
        Usuario usuarioAntigo = collection.findOneAndUpdate(filter, uptade);
        logger.log(Level.INFO, "Atualizado: {0}", usuarioAntigo.getUsername());
        Document document = populaJsonString(usuario);
        return document.toJson();
    }

    private Document verificaDadosParaAtualizacao(Usuario usuario) {
        Document document = new Document();

        if (usuario.getUsername() != null) document.put("username", usuario.getUsername());
        if (usuario.getUuid() != null) document.put("uuid", usuario.getUuid());
        if (usuario.getNome() != null) document.put("nome", usuario.getNome());
        if (usuario.getEmail() != null) document.put("email", usuario.getEmail());
        if (usuario.getSenha() != null) document.put("senha", usuario.getSenha());
        if (usuario.getId() != null) document.put("_id", usuario.getId());
//        if (usuario.getDataNascimento() != null) document.put("dataNascimento", usuario.getDataNascimento());
        return new Document("$set", document);
    }


    //    Métodos auxiliares

    private Document populaJsonString(Usuario usuario) {
        Document document = new Document();
        document.put("nome", usuario.getNome());
        document.put("email", usuario.getEmail());
        document.put("username", usuario.getUsername());
        document.put("senha", usuario.getSenha());
        document.put("uuid", usuario.getUuid().toString());
        return document;
    }
    private boolean verificaSeUsuarioJaExiste(String username) {
        if (busca(username) != null) {
            logger.log(Level.INFO, "Usuario já cadastrado: {0}", username);
            client.close();
            return true;
        }
        return false;
    }
}
