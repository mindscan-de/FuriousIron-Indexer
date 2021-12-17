package de.mindscan.furiousiron.document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class DocumentKeyStrategyMD5Test {

    @Test
    public void testGenerateDocumentKey_aajava_returnsMD5Ofaajava() throws Exception {
        // arrange
        DocumentKeyStrategyMD5 strategyMD5 = new DocumentKeyStrategyMD5();

        // act
        String result = strategyMD5.generateDocumentKey( Paths.get( "a/a.java" ) );

        // assert
        assertThat( result, equalTo( "b299050fb1b506ef9ad13e6e55787a89" ) );
    }

    @Test
    public void testGenerateDocumentKey_abjava_returnsMD5Ofabjava() throws Exception {
        // arrange
        DocumentKeyStrategyMD5 strategyMD5 = new DocumentKeyStrategyMD5();

        // act
        String result = strategyMD5.generateDocumentKey( Paths.get( "a/b.java" ) );

        // assert
        assertThat( result, equalTo( "f4bda76da7d5e4eafa0e0c893c00262a" ) );
    }

}
