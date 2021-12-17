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

    @Test
    public void testGetFileSize_Ctor_returnsDefaultValueZero() throws Exception {
        // arrange
        DocumentMetadata metadata = new DocumentMetadata( "simpleDocumentKey", "another/simple/path.txt", "simpleFilename.txt" );

        // act
        long result = metadata.getFileSize();

        // assert
        assertThat( result, equalTo( 0L ) );
    }

    @Test
    public void testSetFileSize_SetToLengthOne_returnsFilesizeOne() throws Exception {
        // arrange
        DocumentMetadata metadata = new DocumentMetadata( "simpleDocumentKey", "another/simple/path.txt", "simpleFilename.txt" );

        // act
        metadata.setFileSize( 1L );

        // assert
        long result = metadata.getFileSize();
        assertThat( result, equalTo( 1L ) );
    }

    @Test
    public void testSetNumberOfLines_Ctor_returnsDefaultZero() throws Exception {
        // arrange
        DocumentMetadata metadata = new DocumentMetadata( "simpleDocumentKey", "another/simple/path.txt", "simpleFilename.txt" );

        // act
        long result = metadata.getNumberOfLines();

        // assert
        assertThat( result, equalTo( 0L ) );
    }

    @Test
    public void testSetNumberOfLines_SetToNumberOfLinesToOneThousand_returnsOneThousand() throws Exception {
        // arrange
        DocumentMetadata metadata = new DocumentMetadata( "simpleDocumentKey", "another/simple/path.txt", "simpleFilename.txt" );

        // act
        metadata.setNumberOfLines( 1000L );

        // assert
        long result = metadata.getNumberOfLines();
        assertThat( result, equalTo( 1000L ) );
    }

}
