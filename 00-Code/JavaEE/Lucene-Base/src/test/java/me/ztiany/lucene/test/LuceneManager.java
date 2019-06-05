package me.ztiany.lucene.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

/*
 * 索引维护入门程序
 *  - 添加
 *  - 删除
 *  - 修改
 *  - 查询
 */
public class LuceneManager {

    private IndexWriter getIndexWriter() throws Exception {
        Directory directory = FSDirectory.open(new File("LuceneIndex"));
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        return new IndexWriter(directory, config);
    }


    //IndexReader  IndexSearcher
    private IndexSearcher getIndexSearcher() throws Exception {
        // 第一步：创建一个Directory对象，也就是索引库存放的位置。
        Directory directory = FSDirectory.open(new File("LuceneIndex"));// 磁盘
        // 第二步：创建一个indexReader对象，需要指定Directory对象。
        IndexReader indexReader = DirectoryReader.open(directory);
        // 第三步：创建一个IndexSearcher对象，需要指定IndexReader对象
        return new IndexSearcher(indexReader);
    }

    //打印查询的结果
    @SuppressWarnings("all")
    private void printResult(IndexSearcher indexSearcher, Query query) throws Exception {
        TopDocs topDocs = indexSearcher.search(query, 20);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        System.out.println("total count: " + scoreDocs.length);
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
            // 文件名称
            String fileName = document.get("fileName");
            System.out.println(fileName);
            // 文件内容
            String fileContent = document.get("fileContent");
            System.out.println(fileContent);
            // 文件大小
            String fileSize = document.get("fileSize");
            System.out.println(fileSize);
            // 文件路径
            String filePath = document.get("filePath");
            System.out.println(filePath);
            System.out.println("------------");
        }
    }


    //全删除
    @Test
    public void testAllDelete() throws Exception {
        IndexWriter indexWriter = getIndexWriter();
        indexWriter.deleteAll();
        indexWriter.close();
    }

    //根据条件删除
    @Test
    public void testDelete() throws Exception {
        IndexWriter indexWriter = getIndexWriter();
        Query query = new TermQuery(new Term("fileName", "apache"));
        indexWriter.deleteDocuments(query);
        indexWriter.close();
    }

    //修改
    @Test
    public void testUpdate() throws Exception {
        IndexWriter indexWriter = getIndexWriter();
        Document doc = new Document();
        doc.add(new TextField("fileN", "测试文件名", Store.YES));
        doc.add(new TextField("fileC", "测试文件内容", Store.YES));
        indexWriter.updateDocument(new Term("fileName", "lucene"), doc, new IKAnalyzer());
        indexWriter.close();
    }

    //查询所有
    @Test
    public void testMatchAllDocsQuery() throws Exception {
        IndexSearcher indexSearcher = getIndexSearcher();
        Query query = new MatchAllDocsQuery();
        System.out.println(query);
        printResult(indexSearcher, query);
        indexSearcher.getIndexReader().close();
    }

    //根据数值范围查询
    @Test
    public void testNumericRangeQuery() throws Exception {
        IndexSearcher indexSearcher = getIndexSearcher();
        Query query = NumericRangeQuery.newLongRange("fileSize", 47L, 200L, false, true);
        System.out.println(query);
        printResult(indexSearcher, query);
        indexSearcher.getIndexReader().close();
    }

    //可以组合查询条件
    @Test
    public void testBooleanQuery() throws Exception {
        IndexSearcher indexSearcher = getIndexSearcher();

        BooleanQuery booleanQuery = new BooleanQuery();

        Query query1 = new TermQuery(new Term("fileName", "apache"));
        Query query2 = new TermQuery(new Term("fileName", "lucene"));

        booleanQuery.add(query1, Occur.MUST);
        booleanQuery.add(query2, Occur.SHOULD);
        System.out.println(booleanQuery);
        printResult(indexSearcher, booleanQuery);
        //关闭资源
        indexSearcher.getIndexReader().close();
    }

    /*
     Lucene已经给我们提供了很多Query查询器，如PhraseQuery,SpanQuery,那为什么还要提供QueryParser呢？或者说设计QueryParser的目的是什么？
     QueryParser的目的就是让你从众多的Query实现类中脱离出来，因为Query实现类太多了，你有时候会茫然了，我到底该使用哪个Query实现类来完成我的查询需求呢，
     所以Lucene制定了一套Query语法，根据你传入的Query语法字符串帮你把它转换成Query对象，你不用关心底层是使用什么Query实现类。
     */
    //条件解释的对象查询
    @Test
    public void testQueryParser() throws Exception {
        IndexSearcher indexSearcher = getIndexSearcher();
        //参数1： 默认查询的域，如果parse方法只传入一个值(比如parse("lucene")，那么就使用默认的域，否则默认域就被忽略
        //参数2：采用的分析器
        QueryParser queryParser = new QueryParser("fileName", new IKAnalyzer());
        // *:*   域：值
        Query query = queryParser.parse("fileContent:CPU");

        printResult(indexSearcher, query);
        indexSearcher.getIndexReader().close();
    }

    //条件解析的对象查询，多个默认域
    @Test
    public void testMultiFieldQueryParser() throws Exception {
        IndexSearcher indexSearcher = getIndexSearcher();
        String[] fields = {"fileName", "fileContent"};
        //参数1： 默认查询的域
        //参数2：采用的分析器
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, new IKAnalyzer());
        // *:*   域：值
        Query query = queryParser.parse("lucene is apache");

        printResult(indexSearcher, query);
        //关闭资源
        indexSearcher.getIndexReader().close();
    }


}
