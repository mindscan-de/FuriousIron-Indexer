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

import java.nio.file.Path;

import de.mindscan.furiousiron.hfb.HFBFilterBank;
import de.mindscan.furiousiron.hfb.HFBFilterFactory;
import de.mindscan.furiousiron.hfb.io.HFBFilterBankWriterV1Impl;
import de.mindscan.furiousiron.index.trigram.TrigramSubPathCalculator;

/**
 * This is a read-only index for the metadata hfb filters. 
 */
public class SearchMetadataHFBFilterIndex {

    private static final String HFB_INVERSE_METADATA_INDEX = "hfbInverseMetadataFilters.index";
    private static final String HFB_FILE_EXTENSION = HFBFilterBankWriterV1Impl.FILE_DOT_SUFFIX;

    private final Path inverseTrigramsHFBFiltersPath;

    private final HFBFilterFactory hfbFactory = new HFBFilterFactory();

    public SearchMetadataHFBFilterIndex( Path indexFolder ) {
        this.inverseTrigramsHFBFiltersPath = indexFolder.resolve( HFB_INVERSE_METADATA_INDEX );
    }

    public static String getLocalIndexFolder() {
        return HFB_INVERSE_METADATA_INDEX;
    }

    public HFBFilterBank loadFilterBankForTrigram( String trigram ) {
        Path hfbPath = TrigramSubPathCalculator.getPathForTrigram( inverseTrigramsHFBFiltersPath, trigram, HFB_FILE_EXTENSION );

        // TODO: in case of problems, return a neutral filter, which filters nothing.
        // TODO: implement a neutral filter Bank.
        HFBFilterBank hfbFilterBank = hfbFactory.fromFile( hfbPath.toString() );

        return hfbFilterBank;
    }
}
