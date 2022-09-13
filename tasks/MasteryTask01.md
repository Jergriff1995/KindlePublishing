### Mastery Task 1: Killing me softly

#### Milestone 1: Create class and sequence diagrams

One of the action items from the design review was to add a class diagram with the existing classes in the service.
Create a class diagram of the classes in the following packages and how they relate to each other:

* `com.amazon.ata.kindlepublishingservice.activity` (except ExecuteTctActivity.java)
* `com.amazon.ata.kindlepublishingservice.clients`
* `com.amazon.ata.kindlepublishingservice.dao`
* `com.amazon.ata.kindlepublishingservice.dynamob.models`
* `com.amazon.ata.kindlepublishingservice.exceptions`
* `com.amazon.ata.kindlepublishingservice.metrics` (except MetricsConstants.java)

The diagram does **not** need to include any classes in the following packages:

* `com.amazon.ata.kindlepublishingservice.converters`
* `com.amazon.ata.kindlepublishingservice.dagger`
* `com.amazon.ata.kindlepublishingservice.publishing`

The DynamoDB model classes in the diagram **must** include all fields and types used to model the DynamoDB table, and
which fields will represent the partition and (if any) sort key. You do not need to provide any other annotations, but
be sure to indicate the Java type. In each provided model, we’ve chosen to use an enum to represent the value of an
attribute. You’ll see this annotation: `@DynamoDBTypeConvertedEnum` on the getter of the attribute. This tells the
DynamoDBMapper to convert the enum to a string value when storing it in the table, but allows your Java code to
restrict the attribute to a set of allowed values. Use the enum type in your diagram.

Use the `src/resources/mastery-task1-kindle-publishing-CD.puml` to document the class diagram.

Next, create a sequence diagram for the API we'll be working on in milestone 2, `RemoveBookFromCatalog`. You can use
the existing sequence diagrams in the design document as a starter. Include any error handling the APIs are expected to
perform, as well as their interactions with other classes.

Update `src/resources/mastery-task1-remove-book-SD.puml` with a sequence diagram for your planned implementation of the
`RemoveBookFromCatalog` operation.

**Recall:** [We can use PlantUML’s `alt` syntax](http://wiki.plantuml.net/site/sequence-diagram#grouping_message) to
represent if/else cases for validation.

**Recall:** We can add the `@DynamoDBHashKey` and `@DynamoDBRangeKey` annotations to the class diagram. Here’s an
[example format](https://plantuml.corp.amazon.com/plantuml/form/encoded.html#encoded=SoWkIImgAStDuKhEIImkLd3ApyzMgEPoSAdCIypDTt7oI2pEy4wjL4W2YdkcA5Wf19SKPUQb8nG49UQbfu9KbAKM5MVcvm6LUEQLfAQd5d7LSZcavgK0pGO0).


#### Milestone 2: Implement RemoveBookFromCatalog

We already got a head start on this. You’ll need to now add some logic to do a soft delete. We don’t want to
lose previous versions of the book that we have sold to customers. Instead, we’ll mark the current version as inactive
so that it can never be returned by the `GetBook` operation, essentially deleted.

We’ll need to update our `CatalogDao` to implement this “delete” functionality and use that in our `Activity` class.

When writing unit tests for your new logic in `CatalogDao`, we encourage you to use
[ArgumentCaptor](https://site.mockito.org/javadoc/current/org/mockito/ArgumentCaptor.html)s. To see one in action in
the project take a look at the `getBookFromCatalog_oneVersion_returnVersion1` unit test in the
`CatalogDaoTest` class.

We’ve generated some catalog data and put it in your CatalogItemVersions DynamoDb table. You can find a book there to
remove, or feel free to add any additional test data.

Run `MasteryTaskOneDesignTests` to make sure all tests for this task are passing.

**Exit Checklist:**

* Your've created your `RemoveBookFromCatalog` sequence diagram
* You’ve implemented `RemoveBookFromCatalog`’s functionality
* `MasteryTaskOneDesignTests` passes
