package org.lawman.command;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.tika.Tika;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import com.lawman.searcher.LuceneSearcher;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class SearchFileCommand implements Command {

    private static final String indexDir = "tmpIndex";
    private static final String cmdName = "SearchFile";

    private static LuceneSearcher searcher;
    private static boolean isInit = false;

    private String keyWords;

    static public String Init() {
	if (isInit) {
	    return cmdName;
	}
	Path indexPath = Paths.get(indexDir);

	try {
	    if (!Files.exists(indexPath)) {
		System.out.println("no index dir exists exiting!!");
		isInit = false;
		return cmdName;
	    }

	    IndexReader reader = DirectoryReader.open(FSDirectory.open(indexPath));
	    searcher = new LuceneSearcher(new IndexSearcher(reader),
	                                  new QueryParser("filename", new StandardAnalyzer()));

	    System.out.println("dadadaddaaa! " + indexPath);
	    isInit = true;
	} catch(Exception e) {
	    e.printStackTrace();
	} finally {
	}
	return cmdName;     
    }
    public SearchFileCommand() {
    }

    public String getKeyWords() {
          return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;	    
    }

    public String getIndexDir() {
	return indexDir;
    }

    @Override
	public String execute() {
	    if (!isInit) {
		System.out.println("Search file command is not initialized");
		return null;
	    }
            searcher.searchDocument(keyWords);
	    return null;
	}
}
