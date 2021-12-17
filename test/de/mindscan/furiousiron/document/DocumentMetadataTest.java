package de.mindscan.furiousiron.document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class DocumentMetadataTest {

    @Test
    public void testGetDocumentKey_setCtorSimpleDocumentKey_returnsSimpleDocumentKey() throws Exception {
        // arrange
        DocumentMetadata metadata = new DocumentMetadata( "simpleDocumentKey", null, null );

        // act
        String result = metadata.getDocumentKey();

        // assert
        assertThat( result, equalTo( "simpleDocumentKey" ) );
    }

    @Test
    public void testGetDocumentKey_setCtorAnotherDocumentKey_returnsAnotherDocumentKey() throws Exception {
        // arrange
        DocumentMetadata metadata = new DocumentMetadata( "anotherDocumentKey", null, null );

        // act
        String result = metadata.getDocumentKey();

        // assert
        assertThat( result, equalTo( "anotherDocumentKey" ) );
    }

    @Test
    public void testGetRelativePath_setASimplePath_returnsASimplePath() throws Exception {
        // arrange
        DocumentMetadata metadata = new DocumentMetadata( null, "a/simple/path.txt", null );

        // act
        String result = metadata.getRelativePath();

        // assert
        assertThat( result, equalTo( "a/simple/path.txt" ) );
    }

    @Test
    public void testGetRelativePath_setAnotherSimplePath_returnsAanotherSimplePath() throws Exception {
        // arrange
        DocumentMetadata metadata = new DocumentMetadata( null, "another/simple/path.txt", null );

        // act
        String result = metadata.getRelativePath();

        // assert
        assertThat( result, equalTo( "another/simple/path.txt" ) );
    }

}
