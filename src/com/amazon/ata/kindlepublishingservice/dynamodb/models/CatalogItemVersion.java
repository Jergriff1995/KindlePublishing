package com.amazon.ata.kindlepublishingservice.dynamodb.models;

import com.amazon.ata.recommendationsservice.types.BookGenre;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

import java.util.Objects;

@DynamoDBTable(tableName = "CatalogItemVersions")
public class CatalogItemVersion {
    private String bookId;
    private int version;
    private boolean inactive;
    private String title;
    private String author;
    private String text;
    private BookGenre genre;

    @DynamoDBHashKey(attributeName = "bookId")
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @DynamoDBRangeKey(attributeName = "version")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @DynamoDBAttribute(attributeName = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute(attributeName = "author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @DynamoDBAttribute(attributeName = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = "genre")
    public BookGenre getGenre() {
        return genre;
    }

    public void setGenre(BookGenre genre) {
        this.genre = genre;
    }

    @DynamoDBAttribute(attributeName = "inactive")
    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean active) {
        inactive = active;
    }

    @Override
    public String toString() {
        return "CatalogItemVersion{" +
                "bookId='" + bookId + '\'' +
                ", version=" + version +
                ", inactive=" + inactive +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                ", genre=" + genre +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogItemVersion)) {
            return false;
        }
        CatalogItemVersion that = (CatalogItemVersion) o;
        return getVersion() == that.getVersion() &&
                isInactive() == that.isInactive() &&
                Objects.equals(getBookId(), that.getBookId()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getAuthor(), that.getAuthor()) &&
                Objects.equals(getText(), that.getText()) &&
                getGenre() == that.getGenre();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookId(),
                getVersion(),
                isInactive(),
                getTitle(),
                getAuthor(),
                getText(),
                getGenre());
    }
}
