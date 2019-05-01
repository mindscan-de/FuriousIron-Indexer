package de.mindscan.furiousiron.search;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.junit.Test;

public class SearchTest {

    @Test
    public void testSearch_createkeystoreWordIsPartOfElasticSearch_expectOneDocumentHavingID9d4af74f72c76c97ad3ab9faa9a6d5a3() throws Exception {
        // Arrange
        Path indexfolder = Paths.get( "D:\\Analysis\\CrawlerProjects", "Indexed" );
        Search search = new Search( indexfolder );

        // Act
        Collection<SearchResultCandidates> result = search.search( "createkeystore" );

        // Assert
        System.out.println( result );
        assertThat( result, hasSize( 1 ) );
    }

}
