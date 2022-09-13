package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;

import javax.inject.Inject;
import java.util.List;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    public void validateBookExists(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null ) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }


    }



    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }
    public CatalogItemVersion markAsInactive(String bookId) throws BookNotFoundException{
        CatalogItemVersion book = this.getBookFromCatalog(bookId);
        book.setInactive(true);
        dynamoDbMapper.save(book);
        return book;
    }

    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook kindleFormattedBook){
        System.out.println("Create or Update called");
        //if bookId is null it is a new book.
        if(kindleFormattedBook.getBookId() == null) {
            System.out.println("Inside create or update");

            // create a new CatalogItemVersion object.
            CatalogItemVersion catalogItemVersion = new CatalogItemVersion();
            catalogItemVersion.setBookId(KindlePublishingUtils.generateBookId());
            System.out.println(catalogItemVersion.getBookId());

            // getnerate a book id
            // save new CatalogItemVersion into DynamoDB with version 1;
            catalogItemVersion.setVersion(1);
            catalogItemVersion.setInactive(false);
            catalogItemVersion.setTitle(kindleFormattedBook.getTitle());
            catalogItemVersion.setAuthor(kindleFormattedBook.getAuthor());
            catalogItemVersion.setText(kindleFormattedBook.getText());
            catalogItemVersion.setGenre(kindleFormattedBook.getGenre());
            dynamoDbMapper.save(catalogItemVersion);
            return catalogItemVersion;
        }

        //if bookId is not null

            //check if the ID exists in the database.
            CatalogItemVersion oldRecord = getBookFromCatalog(kindleFormattedBook.getBookId());

            // if it does not exists in the DB an exception will be thrown
            validateBookExists(kindleFormattedBook.getBookId());

            // if it exists mark the old CatalogItemVersion inactive
            oldRecord.setInactive(true);
            dynamoDbMapper.save(oldRecord);

            // create a new CatalogItemVersion with the same bookId
            CatalogItemVersion catalogItemVersion = new CatalogItemVersion();

            // give the CatalogItemVersion all the attributes from the KindleFormattedBook
            // increment version
            catalogItemVersion.setVersion(oldRecord.getVersion() + 1);
            catalogItemVersion.setBookId(kindleFormattedBook.getBookId());
            catalogItemVersion.setInactive(false);
            catalogItemVersion.setTitle(kindleFormattedBook.getTitle());
            catalogItemVersion.setAuthor(kindleFormattedBook.getAuthor());
            catalogItemVersion.setText(kindleFormattedBook.getText());
            catalogItemVersion.setGenre(kindleFormattedBook.getGenre());

            // save into the DB
            dynamoDbMapper.save(catalogItemVersion);
            return catalogItemVersion;

    }
}
