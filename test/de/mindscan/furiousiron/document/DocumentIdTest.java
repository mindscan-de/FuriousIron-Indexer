package de.mindscan.furiousiron.document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class DocumentIdTest {

    @Test
    public void testGetDocumentKey_SetMyDocumentKey_returnsMyDocumentKey() throws Exception {
        // arrange
        String myDocumentKey = "myDocumentKey";

        DocumentId documentId = new DocumentId( myDocumentKey, Paths.get( "a.com/b/c.txt" ) );

        // act
        String documentKey = documentId.getDocumentKey();

        // assert
        assertThat( documentKey, equalTo( myDocumentKey ) );
    }

    @Test
    public void testGetDocumentKey_SetAnotherDocumentKey_returnsAnotherDocumentKey() throws Exception {
        // arrange
        String myDocumentKey = "anotherDocumentKey";

        DocumentId documentId = new DocumentId( myDocumentKey, Paths.get( "a.com/b/c.txt" ) );

        // act
        String documentKey = documentId.getDocumentKey();

        // assert
        assertThat( documentKey, equalTo( myDocumentKey ) );
    }

    @Test
    public void testGetDocumentLocationPath_SetMyLocationPath_returnsMyLocationPath() throws Exception {
        // arrange
        DocumentId documentId = new DocumentId( "myDocumentKey", Paths.get( "my/location.path.txt" ) );

        // act
        Path documentPath = documentId.getDocumentLocationPath();

        // assert
        assertThat( documentPath, equalTo( Paths.get( "my/location.path.txt" ) ) );
    }

    @Test
    public void testGetDocumentLocationPath_SetAnoherLocationPath_returnsAnotherLocationPath() throws Exception {
        // arrange
        DocumentId documentId = new DocumentId( "myDocumentKey", Paths.get( "another/location.path.txt" ) );

        // act
        Path documentPath = documentId.getDocumentLocationPath();

        // assert
        assertThat( documentPath, equalTo( Paths.get( "another/location.path.txt" ) ) );
    }

}
