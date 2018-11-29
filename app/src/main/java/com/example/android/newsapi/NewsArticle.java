package com.example.android.newsapi;

public class NewsArticle {
    /** Title of the article */
    private String mArticleTitle;

    /** Article Author's name */
    private String mArticleAuthor;

    /** Date the article was published */
    private String mArticleDate;

    /** Website URL of the article */
    private String mArticleURL;


    public NewsArticle (String articleTitle, String articleAuthor, String articleDate, String articleURL) {
        mArticleTitle = articleTitle;
        mArticleAuthor = articleAuthor;
        mArticleDate = articleDate;
        mArticleURL = articleURL;
    }

    public String getArticleTitle() {
        return mArticleTitle;
    }

    public String getArticleAuthor() {
        return mArticleAuthor;
    }

    public String getArticleDate() {
        return mArticleDate;
    }

    public String getArticleURL() {
        return mArticleURL;
    }
}
