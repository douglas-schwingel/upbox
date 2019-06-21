package br.com.upbox.codec;

import br.com.upbox.models.Usuario;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class UsuarioCodec implements CollectibleCodec<Usuario> {

    private Codec<Document> codec;

    public UsuarioCodec(Codec<Document> codec) {
        this.codec = codec;
    }


    @Override
    public Usuario generateIdIfAbsentFromDocument(Usuario usuario) {
        return documentHasId(usuario) ? usuario.criarId() : usuario;
    }

    @Override
    public boolean documentHasId(Usuario usuario) {
        return usuario.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(Usuario usuario) {
        if(!documentHasId(usuario)) throw new RuntimeException("Usuário não tem ID");
        return new BsonString(usuario.getId().toHexString());
    }
//TODO falta terminar o codec!!
    @Override
    public Usuario decode(BsonReader reader, DecoderContext decoderContext) {
        return null;
    }

    @Override
    public void encode(BsonWriter writer, Usuario value, EncoderContext encoderContext) {

    }

    @Override
    public Class<Usuario> getEncoderClass() {
        return null;
    }
}
