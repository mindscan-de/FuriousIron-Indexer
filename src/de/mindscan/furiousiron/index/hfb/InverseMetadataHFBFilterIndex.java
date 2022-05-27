/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.furiousiron.index.hfb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.mindscan.furiousiron.hfb.HFBFilterBank;
import de.mindscan.furiousiron.hfb.io.HFBFilterBankWriterV1Impl;
import de.mindscan.furiousiron.hfb.options.HFBFilterWriteOption;
import de.mindscan.furiousiron.index.trigram.TrigramSubPathCalculator;

/**
 * 
 */
public class InverseMetadataHFBFilterIndex {

    private static final String HFB_INVERSE_METADATA_INDEX = "hfbInverseMetadataFilters.index";
    private static final String FILE_DOT_SUFFIX = HFBFilterBankWriterV1Impl.FILE_DOT_SUFFIX;

    private final Path inverseTrigramsHFBFiltersPath;

    private final HFBFilterBankWriterV1Impl writer = new HFBFilterBankWriterV1Impl();

    /**
     * @param indexFolder
     */
    public InverseMetadataHFBFilterIndex( Path indexFolder ) {
        this.inverseTrigramsHFBFiltersPath = indexFolder.resolve( HFB_INVERSE_METADATA_INDEX );
    }

    public void addHFBFilterForMetadata( String trigram, HFBFilterBank filterBank ) {
        try {
            Path hfbPath = TrigramSubPathCalculator.getPathForTrigram( inverseTrigramsHFBFiltersPath, trigram, FILE_DOT_SUFFIX );

            createTargetDirectoryIfNotExist( hfbPath.getParent() );

            writer.write( filterBank, hfbPath.toAbsolutePath().toString(), HFBFilterWriteOption.ORDER_BY_RANDOM, HFBFilterWriteOption.ORDER_BY_EFFICIENCY,
                            HFBFilterWriteOption.SAVE_THREE_FILTERBANKS );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTargetDirectoryIfNotExist( Path path ) {
        if (!Files.isDirectory( path )) {
            try {
                Files.createDirectories( path );
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
