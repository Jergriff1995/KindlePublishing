### Mastery Task 3: Query, query on the wall, don’t load just one, get them all!

Next we’ll work on the operation that allows a client to check in on the state of their publishing request,
`GetPublishingStatus`. When a book is submitted for publishing via our `SubmitBookForPublishing` operation,
we write to DynamoDB that the request had been queued. We store an item in the table with the
keys: `{partition: publishingstatus.123, sort: QUEUED}`. When the actual publishing logic is implemented it will create
a new item in DynamoDB when the request starts to be processed: `{partition: publishingstatus.123, sort: IN_PROGRESS}`.
Finally, when publishing has completed, the publishing logic will again write to DynamoDB:
`{partition: publishingstatus.123, sort: SUCCEEDED}`.

A call to `GetPublishingStatus` should retrieve each of the items from DynamoDB that correspond to the provided
`publishingStatusId`. If no items are found, the provided `PublishingStatusNotFoundException` should be thrown.

Create the `GetPublishingStatusActivity` class and implement the `GetPublishingStatus` operation using the design
documentation’s implementation notes and sequence  diagram. When populating the response, you will need to convert
the list of `PublishingStatusItem`s to a list of  `PublishingStatusRecord`s. Follow the pattern used in
`GetBookActivity` when converting the list of `BookRecommendation`s.

Since we haven't implemented the publishing logic, we can't easily generate test data to retrieve with our new API. We
have populated your PublishingStatus table with a few complete publishing status histories to help out with your
testing. The table below contains `publishingStatusId`s that you can use for the described test case.

|test case	|publishingStatusId	|
|---	|---	|
|successful publishing process	|publishingstatus.bdd319cb-05eb-494b-983f-6e1b983c4c46	|
|failed publishing process	|publishingstatus.4bd41646-b1b2-4627-8304-5180c9b54e00	|
|successful new version published	|publishingstatus.2bc206a1-5b41-4782-a260-976c0a291825	|

Run `MasteryTaskThreeTests` workflow to validate your changes.

**Exit Checklist:**

* You've implemented `GetPublishingStatus`'s functionality
* `MasteryTaskThreeTests` passes
