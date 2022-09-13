# ATACurriculumTestInfrastructure
This package contains the infrastructure needed to run our project tests - the core types used, the junit runners and
execution listeners needed, and the integration test base classes. It also includes a series of utility classes to make
testing ATA projects and activities easier. These utilities range from PlantUml assertion classes to classes aiding in
the ability to test code that doesn't exist yet.

## Migration Note
* We are moving to a pattern where the `FooAssertion` classes call `FooHelper` classes, which have methods to determine
  if something needing to be tested is true. The `FooAssertion` class then wraps this in an assert method. If you need
  to determine if something exists in a test and not necessarily assert, please migrate the assertion methods to use
  this pattern. An example of this is the `assertClassDiagramIncludesExtendsRelationship` in the
  `PlantUmlClassDiagramAssertions` class.
