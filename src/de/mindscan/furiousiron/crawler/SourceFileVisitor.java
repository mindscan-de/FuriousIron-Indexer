/**
 * 
 * MIT License
 *
 * Copyright (c) 2019 Maxim Gansert, Mindscan
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
package de.mindscan.furiousiron.crawler;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Consumer;

/**
 * 
 * @param <T> 
 */
public class SourceFileVisitor<T extends Path> implements FileVisitor<Path> {

    private PathMatcher javaSourceFileMatcher = FileSystems.getDefault().getPathMatcher( "glob:**.{java}" );
    private PathMatcher cSourceFileMatcher = FileSystems.getDefault().getPathMatcher( "glob:**.{c,cpp,h,hpp}" );
    private PathMatcher xtendSourceFileMatcher = FileSystems.getDefault().getPathMatcher( "glob:**.{xtend,xtext}" );
    private PathMatcher pythonSourceFileMatcher = FileSystems.getDefault().getPathMatcher( "glob:**.{py}" );
    private PathMatcher manifestFileMatcher = FileSystems.getDefault().getPathMatcher( "glob:**.{MF}" );
    private PathMatcher propertiesFileMatcher = FileSystems.getDefault().getPathMatcher( "glob:**.{properties,ini,persistence}" );
    private PathMatcher jsonFileMatcher = FileSystems.getDefault().getPathMatcher( "glob:**.{json}" );
    private PathMatcher textFileMatcher = FileSystems.getDefault().getPathMatcher( "glob:**.{txt,text,MD,md}" );
    private PathMatcher xmlFileMatcher = FileSystems.getDefault().getPathMatcher( "glob:**.{xml,pom}" );
    private PathMatcher htmlFileMatcher = FileSystems.getDefault().getPathMatcher( "glob:**.{htm,html}" );
    private PathMatcher zipFileMatcher = FileSystems.getDefault().getPathMatcher( "glob:**.{zip,jar}" );

    private Consumer<Path> pathCollector;

    /**
     * @param pathCollector
     */
    public SourceFileVisitor( Consumer<Path> pathCollector ) {
        this.pathCollector = pathCollector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
        if (javaSourceFileMatcher.matches( file )) {
            pathCollector.accept( file );
        }
        else if (cSourceFileMatcher.matches( file )) {
            pathCollector.accept( file );
        }
        else if (xtendSourceFileMatcher.matches( file )) {
            pathCollector.accept( file );
        }
        else if (pythonSourceFileMatcher.matches( file )) {
            pathCollector.accept( file );
        }
        else if (manifestFileMatcher.matches( file )) {
            pathCollector.accept( file );
        }
        else if (textFileMatcher.matches( file )) {
            pathCollector.accept( file );
        }
        else if (xmlFileMatcher.matches( file )) {
            pathCollector.accept( file );
        }
        else if (htmlFileMatcher.matches( file )) {
            pathCollector.accept( file );
        }
        else if (propertiesFileMatcher.matches( file )) {
            pathCollector.accept( file );
        }
        else if (jsonFileMatcher.matches( file )) {
            pathCollector.accept( file );
        }
        else if (zipFileMatcher.matches( file )) {
            // TODO: index archives - actually this extraction could be a pre-processing step. 
            // ATM Archives are slightly more difficult... We may have to spawn an additional new indexer and collect virtual paths too
        }

        return FileVisitResult.CONTINUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult visitFileFailed( Path file, IOException exc ) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult postVisitDirectory( Path dir, IOException exc ) throws IOException {
        return FileVisitResult.CONTINUE;
    }

}
