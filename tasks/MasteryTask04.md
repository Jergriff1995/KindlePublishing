### Mastery Task 4: Make a run(nable) for it

Now that we have functionality that allows clients to submit books for publishing by placing them in the
`BookPublishRequestManager`, we need to implement the functionality that actually processes the requests to get some
books published!

**Milestone 1: Implement BookPublishTask**

Currently, the only way that any code in our service gets executed is via a request from a client. However, we don’t
want to require our clients to make *another* request to execute the publishing of their book! So how do we get our
books published? We can accomplish this by starting a new thread to execute a `Runnable` that publishes a book. To
simplify things a bit for you, we have provided logic that will start a thread and regularly execute a `Runnable`.
You will only be responsible for implementing the `Runnable` class that contains the publishing logic.

The `BookPublisher` we have provided is responsible for scheduling the execution of the `Runnable` repeatedly. If you
open up the `BookPublisher` class and take a look a the `start` method, you’ll see that a `Runnable` is being scheduled
to execute every second. You don’t need to call the `start` method, it is called when your service starts
up and currently is executing the `NoOpTask` that was also provided to you.

You are responsible for writing a new class `BookPublishTask`, that implements `Runnable` and processes a publish
request from the `BookPublishRequestManager`. If the `BookPublishRequestManager` has no publishing requests the
`BookPublishTask` should return immediately without taking action. You will also need to update `CatalogDao` with new
methods for the `BookPublishTask` to publish new books to our Kindle catalog.

Refer to the 'Asynchronous Book Publishing' section of the design doc for `BookPublishTask`'s sequence diagram and
implementation notes. Take special care to consider what happens and what steps to take if an exception is thrown in
`BookPublishTask`'s `run` method!

In order to switch the `BookPublisher` to start scheduling your new `BookPublishTask` instead of the `NoOpTask`, you
will need to update the Dagger code that passes a `NoOpTask` to the `BookPublisher` constructor. Once you’ve made this
switch, you can delete `NoOpTask` and its test class.

To test, submit a book publish request by calling `SubmitBookForPublishing`. You should then get back a
`publishingRecordId`, which you’ll use to check the status by calling `GetPublishingStatus`. You should be able to see
the different states that the request has gone through in the publish status history. Once you see that the publish
request has hit the SUCCESSFUL state, you can call `GetBook` with the `bookId` to see the new or updated book!

**Note**: there is a limitation to our current implementation that will affect your testing. We are maintaining all
publishing requests in memory in the `BookPublishRequestManager`, which means that when you restart your service any
requests that were in the `BookPublishRequestManager` previously are lost forever. This is not ideal, but we’ve kept
things simple for the sake of this project. To remove this limitation, we could use different technologies such as
[SQS](https://aws.amazon.com/sqs/) or persisting the requests in a datastore like DynamoDB, but that’s out of scope for
this project.

Run `MasteryTaskFourSubmitBookForPublishingTests` to validate your changes.

**Milestone 2: Make BookPublishRequestQueue and BookPublishRequest thread safe**

We now have a working `BookPublishTask` running which processes our `BookPublishRequest`s! Each request from a client
is processed in its own thread, therefore we have multiple threads writing to the
`Queue` in our `BookPublishRequestManager`, and another thread reading from it in order to publish. Whenever multiple
threads are accessing the same resource, you can have big trouble! You will want to think of ways to ensure that only
one thread at a time is writing to or reading from the shared resource. This will ensure that `BookPublishRequest`s
added to the `Queue` are in the correct order, and `BookPublishRequest`s are removed and processed in the correct order.

We need to update `BookPublishRequestManager` to be thread-safe so that it behaves as expected
even if multiple threads are accessing it. Sometimes you will have to write extra code to ensure thread-safety, but
Java also provides thread-safe implementations of data structures you can use. Java provides a thread-safe queue called
`ConcurrentLinkedQueue` which we can use instead of a `LinkedList`. Like `LinkedList`, it also implements the `Queue`
interface.

The `ConcurrentLinkedQueue` will ensure that when multiple threads are writing to the queue, the `BookPublishRequest`s
will be added in the  correct order, and that reads from the queue will then access `BookPublishRequest`s in the
correct order.  Update your service so that `BookPublishRequestManager` uses a `ConcurrentLinkedQueue` instead
of a `LinkedList`.

**Optional**: You can browse `ConcurrentLinkedQueue`’s [documentation](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html)
for more information about its implementation.

Dealing with multiple threads accessing a shared resource is only an issue when multiple threads can change the state
of the resource. If the shared resource can’t change, we don’t need to be concerned that one thread might not know
about another thread’s write. Therefore another tool we have to write thread-safe code is to make the shared resource
*immutable*.

A `BookPublishRequest` will never be a resource shared by multiple threads, but let's practice using our immutability
tool anyway. Update `BookPublishRequest` be immutable, and by doing so we'll prevent unintended subclassing!

**Exit checklist:**

* You’ve implemented `BookPublishTask`, a `Runnable` which processes a request from the `BookPublishRequestManger` to
  publish a books to the catalog.
* You’ve updated `BookPublishRequestManger` and `BookPublishRequest` to make them thread safe.
* You’ve added unit tests to cover your new code.
* `MasteryTaskFourSubmitBookForPublishingTests` pass
