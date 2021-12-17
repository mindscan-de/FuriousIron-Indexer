package de.mindscan.furiousiron.document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class DocumentIdFactoryTest {

    @Test
    public void testCreateDocumentIDFromDocumentKey_setMyDocumentKey_DocumentIdHasMyDocumentKeySet() throws Exception {
        // arrange
        DocumentId documentId = DocumentIdFactory.createDocumentIDFromDocumentKey( "myDocumentKey" );

        // act
        String result = documentId.getDocumentKey();

        // assert
        assertThat( result, equalTo( "myDocumentKey" ) );
    }

    @Test
    public void testCreateDocumentIDFromDocumentKey_setAnotherDocumentKey_DocumentIdHasAnotherDocumentKeySet() throws Exception {
        // arrange
        DocumentId documentId = DocumentIdFactory.createDocumentIDFromDocumentKey( "anotherDocumentKey" );

        // act
        String result = documentId.getDocumentKey();

        // assert
        assertThat( result, equalTo( "anotherDocumentKey" ) );
    }

    @Test
    public void testCreateDocumentID_ProvideDocumentAndBaseFolder_DocumentIdHasRelativePathWithStrippedBasefolder() throws Exception {
        // arrange
        DocumentId documentId = DocumentIdFactory.createDocumentID( Paths.get( "/basefolder/a/b/path.txt" ), Paths.get( "/basefolder" ) );

        // act
        String result = documentId.getDocumentLocation();

        // assert
        assertThat( result, equalTo( Paths.get( "a/b/path.txt" ).toString() ) );
    }

    @Test
    public void testCreateDocumentID_ProvideDocumentAndBaseFolder_DocumentIdHasRelativePathObjectWithStrippedBasefolder() throws Exception {
        // arrange
        DocumentId documentId = DocumentIdFactory.createDocumentID( Paths.get( "/basefolder/a/b/path.txt" ), Paths.get( "/basefolder" ) );

        // act
        Path result = documentId.getDocumentLocationPath();

        // assert
        assertThat( result, equalTo( Paths.get( "a/b/path.txt" ) ) );
    }

}
