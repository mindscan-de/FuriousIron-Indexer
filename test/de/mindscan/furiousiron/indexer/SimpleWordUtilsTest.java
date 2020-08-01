package de.mindscan.furiousiron.indexer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.Collection;

import org.junit.jupiter.api.Test;

public class SimpleWordUtilsTest {

    @Test
    public void testGetUniqueTrigramsFromWord_BlaFiveTimes_expectThreeUniqueTrigrams() {
        // Arrange

        // Act
        Collection<String> result = SimpleWordUtils.getUniqueTrigramsFromWord( "blablablablabla" );

        // Assert
        assertThat( result, containsInAnyOrder( "bla", "lab", "abl" ) );
    }

    @Test
    public void testGetUniqueTrigramsFromWord_Copyright_expectSevenUniqueTrigrams() {
        // Arrange

        // Act
        Collection<String> result = SimpleWordUtils.getUniqueTrigramsFromWord( "copyright" );

        // Assert
        assertThat( result, containsInAnyOrder( "cop", "opy", "pyr", "yri", "rig", "igh", "ght" ) );
    }

}
