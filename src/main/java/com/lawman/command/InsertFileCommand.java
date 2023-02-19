package org.lawman.command;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.tika.Tika;

import com.lawman.indexer.LuceneIndexer;

public class InsertFileCommand implements Command {

    private static final String indexDir = "tmpIndex";
    private static final String cmdName = "InsertFile";

    private static LuceneIndexer indexer;
    private static boolean isInit = false;

    private String fileName;

    public static String Init() {
	if (isInit) {
	    return cmdName;
	}
	Path indexPath = Paths.get(indexDir);
	try {
	    if (!Files.exists(indexPath)) {
		Files.createDirectory(indexPath);
		System.out.println("Directory created");
	    } else {
		System.out.println("Directory already exists");
	    }
	    IndexWriter iwriter = new IndexWriter(FSDirectory.open(indexPath),
		    new IndexWriterConfig(new StandardAnalyzer()));
	    indexer = new LuceneIndexer(new Tika(), iwriter);
	    isInit = true;
	} catch(Exception e) {
	    e.printStackTrace();
	} finally {
	}
	return cmdName;     
    }
    public InsertFileCommand() {
    }

    public String getFileName() {
          return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;	    
    }

    public String getIndexDir() {
	return indexDir;
    }

    @Override
	public String execute() {
	    try {
		indexer.indexDocument(new File(fileName));
	    }  catch (Exception e) {
		e.printStackTrace();
	    } finally {
	    }
	    return null;
	}
}
