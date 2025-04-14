package de.mindscan.furiousiron.indexer.main;

import java.nio.file.Path;

import picocli.CommandLine;

public class AllInOneIndexerMain {
	
	public static void main(String[] args) {
        long starttime = System.nanoTime();

        int exitCode = new CommandLine( new IndexerMainParameters() ).execute( args );

        long endTime = System.nanoTime();

        long deltatime = endTime - starttime;

        long seconds = deltatime / 1000000000L;
        long nanoseconds = deltatime % 1000000000L;
        
        System.out.println( String.format( "FullIndex took %d.%09d ", seconds, nanoseconds ) );
        
        System.exit( exitCode );
	}
	
	void run( Path crawlFolder, Path indexFolder ) {
		// Step #1: run the whole index
		IndexerMain indexer = new IndexerMain();
		indexer.run(crawlFolder, indexFolder);
		
		// Step #2: run the classifier
		ClassifierMain classifier = new ClassifierMain();
		classifier.run(indexFolder.resolve("cachedMetadata"), indexFolder);
		
		// Step #3: run the metadata indexer
		MetaIndexerMain metadataFileIndexer = new MetaIndexerMain();
		metadataFileIndexer.run(indexFolder.resolve("cachedMetadata"), indexFolder);
		
		// Step #4: run the HFB-Compiler on the Metadata trigrams.
		HFBCompilerMain hfbCompiler = new HFBCompilerMain();
		hfbCompiler.run(indexFolder.resolve("inverseMetadataTrigram.index"), indexFolder);
		
		// ### Later... I want to speedup the basic search, by using the  
		// Future Step #5: run the HFB-Compiler on the Content trigrams.
	}

}
