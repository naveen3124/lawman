package com.lawman.indexer;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField; 
import org.apache.lucene.index.IndexWriter;
import org.apache.tika.Tika;

public class LuceneIndexer {
    private final Tika tika;
    private final IndexWriter writer;
    public LuceneIndexer(Tika tika, IndexWriter writer) {
	this.tika = tika;
	this.writer = writer;
    }
    public void indexDocument(File file) throws Exception {
	Document document = new Document();
	document.add(new Field(
		    "filename", file.getName(),
		    TextField.TYPE_STORED));
	document.add(new Field(
		    "fulltext", tika.parseToString(file),
		    TextField.TYPE_NOT_STORED));
	writer.addDocument(document);
	System.out.println("document written successfully");
	writer.commit();
    }
}
