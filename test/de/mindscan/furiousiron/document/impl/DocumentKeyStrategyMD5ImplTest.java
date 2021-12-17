package de.mindscan.furiousiron.document.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class DocumentKeyStrategyMD5ImplTest {

    @Test
    public void testGenerateDocumentKey_aajava_returnsMD5Base16Ofaajava() throws Exception {
        // arrange
        DocumentKeyStrategyMD5Impl strategyMD5 = new DocumentKeyStrategyMD5Impl();

        // act
        String result = strategyMD5.generateDocumentKey( Paths.get( "a/a.java" ) );

        // assert
        assertThat( result, equalTo( "b299050fb1b506ef9ad13e6e55787a89" ) );
    }

    @Test
    public void testGenerateDocumentKey_aajavaBase16_returnsMD5Base16Ofaajava() throws Exception {
        // arrange
        DocumentKeyStrategyMD5Impl strategyMD5 = new DocumentKeyStrategyMD5Impl( 16 );

        // act
        String result = strategyMD5.generateDocumentKey( Paths.get( "a/a.java" ) );

        // assert
        assertThat( result, equalTo( "b299050fb1b506ef9ad13e6e55787a89" ) );
    }

    @Test
    public void testGenerateDocumentKey_aajavaBase16_returnsMD5Base36Ofaajava() throws Exception {
        // arrange
        DocumentKeyStrategyMD5Impl strategyMD5 = new DocumentKeyStrategyMD5Impl( 36 );

        // act
        String result = strategyMD5.generateDocumentKey( Paths.get( "a/a.java" ) );

        // assert
        assertThat( result, equalTo( "akn5e5uxxt5hw2a2h7tg3m5xl" ) );
    }

    @Test
    public void testGenerateDocumentKey_abjava_returnsMD5Base16Ofabjava() throws Exception {
        // arrange
        DocumentKeyStrategyMD5Impl strategyMD5 = new DocumentKeyStrategyMD5Impl();

        // act
        String result = strategyMD5.generateDocumentKey( Paths.get( "a/b.java" ) );

        // assert
        assertThat( result, equalTo( "f4bda76da7d5e4eafa0e0c893c00262a" ) );
    }

    @Test
    public void testGenerateDocumentKey_abjavaBase16_returnsMD5Base16Ofabjava() throws Exception {
        // arrange
        DocumentKeyStrategyMD5Impl strategyMD5 = new DocumentKeyStrategyMD5Impl( 16 );

        // act
        String result = strategyMD5.generateDocumentKey( Paths.get( "a/b.java" ) );

        // assert
        assertThat( result, equalTo( "f4bda76da7d5e4eafa0e0c893c00262a" ) );
    }

    @Test
    public void testGenerateDocumentKey_abjavaBase36_returnsMD5Base36Ofabjava() throws Exception {
        // arrange
        DocumentKeyStrategyMD5Impl strategyMD5 = new DocumentKeyStrategyMD5Impl( 36 );

        // act
        String result = strategyMD5.generateDocumentKey( Paths.get( "a/b.java" ) );

        // assert
        assertThat( result, equalTo( "ehm2ftofhuunkjum7606qoilm" ) );
    }

}
