package com.lawman.searcher;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField; 
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneSearcher {
    private final QueryParser parser;
    private final IndexSearcher searcher ;
    public LuceneSearcher(IndexSearcher searcher, QueryParser parser) {
	this.parser = parser;
	this.searcher = searcher ;
    }
    public void searchDocument(String keyWords) {
	try {
	    Query query = parser.parse(keyWords);
	    ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;

	    System.out.println("Found " + hits.length + " hits." + keyWords);
	    for(int i=0;i<hits.length;++i) {
		Document d = searcher.doc(hits[i].doc);
		System.out.println(d.get("filename"));
	    }
	} catch(ParseException e) {
	    e.printStackTrace();
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }
}
