package de.mindscan.furiousiron.search;

import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class SearchTest {

    @Test
    // @Disabled
    public void testSearch_createkeystoreWordIsPartOfElasticSearch_expectOneDocumentHavingID9d4af74f72c76c97ad3ab9faa9a6d5a3() throws Exception {
        // Arrange
        Path indexfolder = Paths.get( "D:\\Analysis\\CrawlerProjects", "Indexed" );
        Search search = new Search( indexfolder );

        // Act
        Collection<SearchResultCandidates> result = search.search( "createkeystore" );

        // Assert
        assertThat( result.toString(), Matchers.containsString( "9d4af74f72c76c97ad3ab9faa9a6d5a3" ) );
    }

    @Test
    // @Disabled
    public void testSearch_PartialMatchingWordWhichIsPartOfElasticSearch_expectOneDocumentHavingID9d4af74f72c76c97ad3ab9faa9a6d5a3() throws Exception {
        // Arrange
        Path indexfolder = Paths.get( "D:\\Analysis\\CrawlerProjects", "Indexed" );
        Search search = new Search( indexfolder );

        // Act
        Collection<SearchResultCandidates> result = search.search( "reatekeystor" );

        // Assert
        assertThat( result.toString(), Matchers.containsString( "9d4af74f72c76c97ad3ab9faa9a6d5a3" ) );
    }

}
