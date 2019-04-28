package de.mindscan.furiousiron.index.trigram;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.hamcrest.Matchers;
import org.junit.Test;

public class TrigramSubPathCalculatorTest {

    @Test
    public void testConvertCharsToUHex_EnglishCharaters_allElementsHaveShortCode() {
        // arrange

        // act
        String[] result = TrigramSubPathCalculator.convertCharsToUHex( "abc" );

        // assert
        assertThat( result, Matchers.arrayContaining( "61", "62", "63" ) );
    }

    @Test
    public void testConvertCharsToUHex_GermanSpecialCharaters() {
        // arrange

        // act
        String[] result = TrigramSubPathCalculator.convertCharsToUHex( "ßäöü" );

        // assert
        assertThat( result, Matchers.arrayContaining( "u00df", "u00e4", "u00f6", "u00fc" ) );
    }

    @Test
    public void testConvertCharsToUHex_JapaneseHiraganaCharacters() {
        // arrange

        // act
        String[] result = TrigramSubPathCalculator.convertCharsToUHex( "あおういえ" );

        // assert
        assertThat( result, Matchers.arrayContaining( "u3042", "u304a", "u3046", "u3044", "u3048" ) );
    }

    @Test
    public void testConvertCharsToUHex_JapaneseKanjiCharacters() {
        // arrange

        // act
        String[] result = TrigramSubPathCalculator.convertCharsToUHex( "喫茶店" );

        // assert
        assertThat( result, Matchers.arrayContaining( "u55ab", "u8336", "u5e97" ) );
    }

    @Test
    public void testConvertCharsToUHex_RussianCharacters_expectRussianUnicodeblockInHex() {
        // arrange

        // act
        String[] result = TrigramSubPathCalculator.convertCharsToUHex( "АБВГД" );

        // assert
        assertThat( result, Matchers.arrayContaining( "u0410", "u0411", "u0412", "u0413", "u0414" ) );
    }

    // These will be difficult... later on, because could be ambiguous.
    @Test
    public void testConvertCharsToUHex_CJKUnifiedIdeogramsExtensionB_expectOneIdeogramWithTwoHexvalues() {
        // arrange

        // act
        String[] result = TrigramSubPathCalculator.convertCharsToUHex( "𢀀" );

        // assert
        assertThat( result, Matchers.arrayContaining( "ud848", "udc00" ) );
    }

    @Test
    public void testGetPathForTrigram_JapaneseAUO_expectTwoSubDirectoriesAndFullFilename() throws Exception {
        // Arrange
        Path basePath = Paths.get( "." );

        // Act
        Path pathForAUOTrigram = TrigramSubPathCalculator.getPathForTrigram( basePath, "あうお", ".suffix" );

        // Assert
        assertThat( pathForAUOTrigram.toString(), equalTo( ".\\u3042\\u3046\\u3042_u3046_u304a.suffix" ) );
    }

}
