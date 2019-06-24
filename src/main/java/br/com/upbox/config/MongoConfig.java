package br.com.upbox.config;

import br.com.upbox.codec.UsuarioCodec;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
        UsuarioCodec usuarioCodec = new UsuarioCodec(codec);

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(usuarioCodec));

        MongoClientOptions options = MongoClientOptions.builder().codecRegistry(codecRegistry).build();
        return new MongoClient("mongodb://admin:upb0x4dm1n@ds343127.mlab.com:43127/heroku_d42tsbq0", options);
    }
}
