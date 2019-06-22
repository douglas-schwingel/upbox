package br.com.upbox.repository;

import br.com.upbox.codec.UsuarioCodec;
import br.com.upbox.exception.UsuarioException;
import br.com.upbox.models.Usuario;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.stereotype.Repository;

import java.util.UUID;
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

    public void salva(Usuario usuario) {
        conecta();
        collection.insertOne(usuario);
        logger.log(Level.INFO, "Salvando no banco: " + usuario.getNome());
        client.close();
    }

    public Usuario busca(String username) {
        conecta();
        MongoCursor<Usuario> cursor = collection.find().iterator();
        while(cursor.hasNext()) {
            Usuario resultado = cursor.next();
            if(resultado.getUsername().equals(username)) return resultado;
        }
        throw new UsuarioException("Usuário não encontrado");
    }

}
