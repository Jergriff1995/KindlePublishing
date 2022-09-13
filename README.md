# Unit 6 Project: Kindle Publishing Service

### Ambiguity, Complexity, and Scope

Ambiguity will be increasing from prior unit's projects as you make your way through the Unit 6 project
mastery tasks. We’ll assume you’ll be using the design document to understand the requirements and
how to implement each API, so the tasks will contain fewer details. After walking you through
something once, we won’t be mentioning it again in each subsequent task. For example, we used Dagger
in Unit 4, and will again in Unit 6. We’ll walk you through it a bit in mastery task 2, but then
you’ll be responsible for knowing that this is something you’ll need to think about and update in
each subsequent task.

We’ll also be increasing complexity. We'll be working with threads for the first time, and write a
new style of API to take advantage of them. We'll also be integrating caching into our project.

Scope will increase slightly. As in last unit, we'll be implementing the entire service, but now
you'll be responsible for writing the Activity classes as well.

You'll be surrounded by other participants in the same situation as you, so remember to collaborate:
rely on each other for assistance, and share your own knowledge.

## The Problem: Amazon Kindle Publishing

The Amazon Kindle store provides millions of ebooks to our customers. The process of publishing an
ebook to the kindle catalog is currently an extremely manual process, which causes a long wait time
to add a book to the catalog.

As a member of the Amazon Kindle team, you will be launching a new service that allows our
publishing department to convert books into a digital format.

The overview, architecture, and implementation are covered in the [design document here](DESIGN_DOCUMENT.md). Almost all major pieces of software at Amazon first go through an intensive design
review to answer the question "Are we building the right thing for our customer?".

By working on the preparedness and mastery tasks for this project, you will finish the
implementation of the service described in the document.

Carefully read the design document and refer back to it while working on the tasks.

## Project Preparedness Tasks

Up to this point, the services we have developed in projects have had synchronous APIs. This
means that a client makes a request to the service, all of the work required to fulfill this request
is done, and then a response is returned to the client. In an asynchronous API, a client makes a
request, the service returns a response immediately, and the service completes the work after the
client disconnects. This is helpful when the work that needs to be done will take a long amount of
time. A client will only wait so long for a response, so it is helpful to quickly return a
successful response acknowledging the work is under way. The service will then continue to work on
the request as it continues to receive other, new requests. The service is working on these requests
concurrently - we can think of this as multi-tasking for now. We’ll spend a lot more time in 
this unit and future units digging into the concept of concurrency much more deeply.

Examine the folders in the starter project to be sure you are familiar with their contents.  Everything you need to start the project and create/populate your DynamoDB tables has been provided.

#### Remember: U.P.E.R.

Note:  You may need to add ***Mock***s to some tests as you progress through the Mastery Tasks.

&nbsp;

## Project Mastery Tasks

### [Mastery Task 1: Killing me softly](tasks/MasteryTask01.md)

### [Mastery Task 2: Submit to the process](tasks/MasteryTask02.md)

### [Mastery Task 3: Query, query on the wall, don’t load one, get them all!](tasks/MasteryTask03.md)

### [Mastery Task 4: Make a run(nable) for it](tasks/MasteryTask04.md)

&nbsp;

