package de.mindscan.furiousiron.indexer;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Test;

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
