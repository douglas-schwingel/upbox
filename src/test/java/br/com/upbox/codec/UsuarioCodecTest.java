package br.com.upbox.codec;

import br.com.upbox.models.Usuario;
import com.mongodb.MongoClient;
import com.sun.jna.platform.win32.OaIdl;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.aggregation.DateOperators;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.junit.Assert.*;

public class UsuarioCodecTest {

    private Codec<Document> codec;


    @Before
    public void setUp() {
        codec = MongoClient.getDefaultCodecRegistry().get(Document.class);

    }

    @Test
    public void deveRetornarUmUsuarioValido() {

    }


}