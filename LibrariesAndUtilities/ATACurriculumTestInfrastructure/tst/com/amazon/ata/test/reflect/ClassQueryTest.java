package com.amazon.ata.test.reflect;

import com.amazon.ata.test.reflect.testpackage.AnotherClassInTestPackage;
import com.amazon.ata.test.reflect.testpackage.ClassInTestPackage;
import com.amazon.ata.test.reflect.testpackage.ExceptionInTestPackage;
import com.amazon.ata.test.reflect.testpackage.anothersubpackage.ClassInTestPackageAnotherSubpackage;
import com.amazon.ata.test.reflect.testpackage.subpackage.ClassInTestPackageSubpackage;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassQueryTest {
    private static final String TEST_PACKAGE_NAME = ClassInTestPackage.class.getPackage().getName();
    private static final String TEST_PACKAGE_SUBPACKAGE_NAME =
        ClassInTestPackageSubpackage.class.getPackage().getName();

    @Test
    void inExactPackage_withPackageName_filtersOnlyByPackage() {
        // GIVEN
        Set<Class<?>> expectedClasses = ImmutableSet.of(
            ClassInTestPackage.class,
            AnotherClassInTestPackage.class,
            com.amazon.ata.test.reflect.testpackage.DupedSimpleName.class
        );

        // WHEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME);

        // THEN
        assertClassQueryReturnsClasses(classQuery, expectedClasses, "filtering only by exact package");
    }

    @Test
    void inExactPackage_withNonExistentPackageName_findsNoClass() {
        // GIVEN
        // bogus package name
        String nonexistentPackage = "you.better.not.name.your.package.this";

        // WHEN
        ClassQuery classQuery = ClassQuery.inExactPackage(nonexistentPackage);

        // THEN
        assertTrue(
            classQuery.findClasses().isEmpty(),
            String.format(
                "Expected not to find any classes in package '%s', but found {%s}",
                nonexistentPackage,
                classQuery.findClasses().toString())
        );
    }

    @Test
    void inExactPackage_withNull_throwsException() {
        // GIVEN
        // WHEN + THEN - null package name results in exception
        assertThrows(IllegalArgumentException.class, () -> ClassQuery.inExactPackage(null));
    }

    @Test
    void inExactPackage_withEmptyString_throwsException() {
        // GIVEN
        // WHEN + THEN - empty package name results in exception
        assertThrows(IllegalArgumentException.class, () -> ClassQuery.inExactPackage(""));
    }

    @Test
    void inExactPackage_withTopLevelPackage_throwsException() {
        // GIVEN
        // too broad of a package name
        String tooHighLevelPackage = "com";

        // WHEN + THEN - too high level package throws exception
        assertThrows(
            IllegalArgumentException.class,
            () -> ClassQuery.inExactPackage(tooHighLevelPackage),
            String.format("Expected exception when trying to find class in package '%s'", tooHighLevelPackage)
        );
    }

    @Test
    void inContainingPackage_withExactPackageName_findsClassesInPackage() {
        // GIVEN
        Set<Class<?>> expectedClasses = ImmutableSet.of(ClassInTestPackageSubpackage.class);

        // WHEN
        ClassQuery classQuery = ClassQuery.inContainingPackage(TEST_PACKAGE_SUBPACKAGE_NAME);

        // THEN
        assertClassQueryReturnsClasses(
            classQuery,
            expectedClasses,
            "filtering by containing package and specifying exact package"
        );
    }

    @Test
    void inContainingPackage_withPackageName_findsClassesInPackageAndAcrossSubpackages() {
        // GIVEN
        Set<Class<?>> expectedClasses = ImmutableSet.of(
            ClassInTestPackage.class,
            AnotherClassInTestPackage.class,
            ClassInTestPackageSubpackage.class,
            ClassInTestPackageAnotherSubpackage.class,
            com.amazon.ata.test.reflect.testpackage.DupedSimpleName.class,
            com.amazon.ata.test.reflect.testpackage.anothersubpackage.DupedSimpleName.class
        );

        // WHEN
        ClassQuery classQuery = ClassQuery.inContainingPackage(TEST_PACKAGE_NAME);

        // THEN
        assertClassQueryReturnsClasses(classQuery, expectedClasses, "filtering by containing package");
    }

    @Test
    void inContainingPackage_withNull_throwsException() {
        // GIVEN
        // WHEN + THEN - null package name results in exception
        assertThrows(IllegalArgumentException.class, () -> ClassQuery.inContainingPackage(null));
    }

    @Test
    void inContainingPackage_withEmptyString_throwsException() {
        // GIVEN
        // WHEN + THEN - empty package name results in exception
        assertThrows(IllegalArgumentException.class, () -> ClassQuery.inContainingPackage(""));
    }

    @Test
    void inContainingPackage_withTopLevelPackage_throwsException() {
        // GIVEN
        // too broad of a package name
        String tooHighLevelPackage = "com";

        // WHEN + THEN - too high level package throws exception
        assertThrows(
            IllegalArgumentException.class,
            () -> ClassQuery.inContainingPackage(tooHighLevelPackage),
            String.format("Expected exception when trying to find class in package '%s'", tooHighLevelPackage)
        );
    }

    @Test
    void withExactSimpleName_withMatchingName_findsClass() {
        // GIVEN
        // WHEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withExactSimpleName(ClassInTestPackage.class.getSimpleName());

        // THEN
        assertClassQueryReturnsClass(
            classQuery,
            ClassInTestPackage.class,
            "filtering by exact class name"
        );
    }

    @Test
    void withExactSimpleName_withCloseButNonMatchingName_doesNotMatchClass() {
        // GIVEN
        String className = ClassInTestPackage.class.getSimpleName();
        String incorrectClassName = className.substring(0, className.length() - 2);

        // WHEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withExactSimpleName(incorrectClassName);

        // THEN
        assertTrue(
            classQuery.findClasses().isEmpty(),
            String.format(
                "Expected withExactSimpleName(%s) not to match any classes, but matches were returned: {%s}",
                incorrectClassName,
                classQuery.findClasses()
            )
        );
    }

    @Test
    void withExactSimpleName_withNull_throwsException() {
        // GIVEN
        // WHEN + THEN - null class name results in exception
        assertThrows(
            IllegalArgumentException.class,
            () -> ClassQuery.inContainingPackage(TEST_PACKAGE_NAME).withExactSimpleName(null)
        );
    }

    @Test
    void withExactSimpleName_withEmptyName_throwsException() {
        // GIVEN
        // WHEN + THEN - empty class name results in exception
        assertThrows(
            IllegalArgumentException.class,
            () -> ClassQuery.inContainingPackage(TEST_PACKAGE_NAME).withExactSimpleName("")
        );
    }

    @Test
    void withExactSimpleName_calledMultipleTimes_throwsException() {
        // GIVEN - class query with name already specified
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withExactSimpleName(ClassInTestPackage.class.getSimpleName());

        // WHEN + THEN - calling again throws exception
        assertThrows(
            IllegalStateException.class,
            () -> classQuery.withExactSimpleName(AnotherClassInTestPackage.class.getSimpleName())
        );
    }

    @Test
    void withExactSimpleName_calledAfterWithSimpleNameContaining_throwsException() {
        // GIVEN - class query with name already specified
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withSimpleNameContaining("Class");

        // WHEN + THEN - calling throws exception
        assertThrows(
            IllegalStateException.class,
            () -> classQuery.withExactSimpleName(ClassInTestPackage.class.getSimpleName())
        );
    }

    @Test
    void withSimpleNameContaining_withExactName_matchesClass() {
        // GIVEN
        String className = AnotherClassInTestPackage.class.getSimpleName();

        // WHEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withSimpleNameContaining(className);

        // THEN
        assertClassQueryReturnsClass(
            classQuery,
            AnotherClassInTestPackage.class,
            String.format("when filtering for name containing exact name, '%s'", className)
        );
    }

    @Test
    void withSimpleNameContaining_withBeginningSubstring_matchesClass() {
        // GIVEN
        Class<?> clazz = AnotherClassInTestPackage.class;
        String className = clazz.getSimpleName();
        String classNameSubstring = className.substring(0, className.length() - 3);

        // WHEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withSimpleNameContaining(classNameSubstring);

        // THEN
        assertClassQueryReturnsClass(
            classQuery,
            clazz,
            String.format("when filtering for name containing beginning substring, '%s'", classNameSubstring)
        );
    }

    @Test
    void withSimpleNameContaining_withMiddleSubstring_matchesClass() {
        // GIVEN
        Class<?> clazz = AnotherClassInTestPackage.class;
        String className = clazz.getSimpleName();
        String classNameSubstring = className.substring(3, className.length() - 2);

        // WHEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withSimpleNameContaining(classNameSubstring);

        // THEN
        assertClassQueryReturnsClass(
            classQuery,
            clazz,
            String.format("when filtering for name containing middle substring, '%s'", classNameSubstring)
        );
    }

    @Test
    void withSimpleNameContaining_withSubstringMatchingMultipleClasses_matchesClasses() {
        // GIVEN
        String classNameSubstring = "Subpackage";
        Set<Class<?>> expectedClasses =
            ImmutableSet.of(ClassInTestPackageSubpackage.class, ClassInTestPackageAnotherSubpackage.class);

        // WHEN
        ClassQuery classQuery = ClassQuery.inContainingPackage(TEST_PACKAGE_NAME)
            .withSimpleNameContaining(classNameSubstring);

        // THEN
        assertClassQueryReturnsClasses(
            classQuery,
            expectedClasses,
            String.format("filtering for name containing '%s'", classNameSubstring)
        );
    }

    @Test
    void withSimpleNameContaining_calledMultipleTimes_filtersByAllSubstrings() {
        // GIVEN
        Class<?> expectedClass = ClassInTestPackageAnotherSubpackage.class;

        // WHEN
        ClassQuery classQuery = ClassQuery.inContainingPackage(TEST_PACKAGE_NAME)
            .withSimpleNameContaining("Another")
            .withSimpleNameContaining("Subpackage");

        // THEN
        assertClassQueryReturnsClass(
            classQuery,
            expectedClass,
            "filtering applying multiple simple name containing filters"
        );
    }

    @Test
    void withSimpleNameContaining_withNull_throwsException() {
        // GIVEN
        // WHEN + THEN - null class name results in exception
        assertThrows(
            IllegalArgumentException.class,
            () -> ClassQuery.inContainingPackage(TEST_PACKAGE_NAME).withSimpleNameContaining(null)
        );
    }

    @Test
    void withSimpleNameContaining_withEmptyClassName_throwsException() {
        // GIVEN
        // WHEN + THEN - empty class name results in exception
        assertThrows(
            IllegalArgumentException.class,
            () -> ClassQuery.inContainingPackage(TEST_PACKAGE_NAME).withSimpleNameContaining("")
        );
    }

    @Test
    void withSimpleNameContaining_calledAfterWithExactSimpleName_throwsException() {
        // GIVEN - class query with exact name already specified
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withExactSimpleName(ClassInTestPackage.class.getSimpleName());

        // WHEN + THEN - calling again throws exception
        assertThrows(
            IllegalStateException.class,
            () -> classQuery.withSimpleNameContaining(AnotherClassInTestPackage.class.getSimpleName())
        );
    }

    @Test
    void findClass_withSubTypeOfProvided_searchesForSubtypesOfProvidedClass() {
        // GIVEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withExactSimpleName("ExceptionInTestPackage")
            .withSubTypeOf(RuntimeException.class);

        // WHEN
        Class<?> clazz = classQuery.findClass();

        // THEN
        assertExpectedClass(ExceptionInTestPackage.class, clazz,
            "specifying subTypeOf RuntimeException",
            classQuery);
    }

    @Test
    void findClasses_withSubTypeOfInDifferentPackage_callsReflectionsWithProvidedSubtype() {
        // GIVEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME) // restrict package
            // search for a class name in the package that happens to extend RuntimeException (not Object)
            .withExactSimpleName("ExceptionInTestPackage")
            // but we're explicitly scanning subtypes of java.lang.Object
            // Reflections won't search transitively outside TEST_PACKAGE_NAME even though everything rolls up Object
            .withSubTypeOf(Object.class);

        // WHEN
        Set<Class<?>> classes = classQuery.findClasses();

        // THEN
        assertTrue(
            classes.isEmpty(),
            String.format(
                "Expected no classes to be returned when searching for a class that extends java.lang" +
                    ".RuntimeException, '%s' when scanning subtypes of java.lang.Object but found: %s.%nClass " +
                    "query was: %s",
                "ExceptionInTestPackage",
                classes,
                classQuery)
        );
    }

    @Test
    void findClassOrFail_whenZeroClassesMatch_assertFires() {
        // GIVEN
        // WHEN + THEN - no match assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> ClassQuery.inExactPackage(TEST_PACKAGE_NAME).withExactSimpleName("WhoPutThisHere").findClassOrFail()
        );
    }

    @Test
    void findClassOrFail_whenOneClassMatches_returnsClass() {
        // GIVEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withSimpleNameContaining("Another");

        // WHEN
        Class<?> clazz = classQuery.findClassOrFail();

        // THEN
        assertExpectedClass(AnotherClassInTestPackage.class, clazz, "filtering to one class",classQuery);
    }

    @Test
    void findClassOrFail_whenMultipleClassesMatch_assertFires() {
        // GIVEN
        // WHEN + THEN - too many matches assert failure
        assertThrows(AssertionFailedError.class, () -> ClassQuery.inExactPackage(TEST_PACKAGE_NAME).findClassOrFail());
    }

    @Test
    void findClass_whenZeroClassesMatch_assertFires() {
        // GIVEN
        // WHEN + THEN - no match assert failure
        assertThrows(
            NoClassFoundException.class,
            () -> ClassQuery.inExactPackage(TEST_PACKAGE_NAME).withExactSimpleName("WhoPutThisHere").findClass()
        );
    }

    @Test
    void findClass_whenOneClassMatches_returnsClass() {
        // GIVEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withSimpleNameContaining("Another");

        // WHEN
        Class<?> clazz = classQuery.findClass();

        // THEN
        assertExpectedClass(AnotherClassInTestPackage.class, clazz, "filtering to one class", classQuery);
    }

    @Test
    void findClass_whenMultipleClassesMatch_assertFires() {
        // GIVEN
        // WHEN + THEN - too many matches assert failure
        assertThrows(
            MultipleClassesFoundException.class,
            () -> ClassQuery.inExactPackage(TEST_PACKAGE_NAME).findClass()
        );
    }

    @Test
    void findClasses_whenZeroClassesMatch_returnsEmptySet() {
        // GIVEN
        String nonExistentClassName = "AtaWouldNeverNameAClassThis";
        ClassQuery classQuery = ClassQuery.inExactPackage("com.amazon.ata")
            .withExactSimpleName(nonExistentClassName);

        // WHEN
        Set<Class<?>> classes = classQuery.findClasses();

        // THEN
        assertTrue(
            classes.isEmpty(),
            String.format(
                "Expected no classes to be returned when searching for '%s', but found: %s.%nClass query was: %s",
                nonExistentClassName,
                classes,
                classQuery)
        );
    }

    @Test
    void findClasses_whenMultipleClassesMatch_returnsMultipleClasses() {
        // GIVEN
        Set<Class<?>> expectedClasses = Sets.newHashSet(
            ClassInTestPackage.class,
            AnotherClassInTestPackage.class,
            ClassInTestPackageSubpackage.class,
            ClassInTestPackageAnotherSubpackage.class
        );
        ClassQuery classQuery = ClassQuery.inContainingPackage("com.amazon.ata")
            .withSimpleNameContaining("TestPackage");

        // WHEN
        Set<Class<?>> classes = classQuery.findClasses();

        // THEN
        assertExpectedClasses(
            expectedClasses,
            classes,
            "finding all classes under testpackage",
            classQuery
        );
    }

    @Test
    void findClasses_byExactPackageAndExactName_appliesAllFilters() {
        // GIVEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withExactSimpleName(ClassInTestPackage.class.getSimpleName());
        Set<Class<?>> expectedClasses = ImmutableSet.of(ClassInTestPackage.class);

        // WHEN
        Set<Class<?>> classes = classQuery.findClasses();

        // THEN
        assertExpectedClasses(
            expectedClasses,
            classes,
            "finding by exact package and exact class name",
            classQuery
        );
    }

    @Test
    void findClasses_byExactPackageAndNameContaining_appliesAllFilters() {
        // GIVEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withSimpleNameContaining("Package");
        Set<Class<?>> expectedClasses = ImmutableSet.of(
            ClassInTestPackage.class,
            AnotherClassInTestPackage.class
        );

        // WHEN
        Set<Class<?>> classes = classQuery.findClasses();

        // THEN
        assertExpectedClasses(
            expectedClasses,
            classes,
            "finding by exact package and class name containing",
            classQuery
        );
    }

    @Test
    void findClasses_byExactPackageAndMultipleNameContaining_appliesAllFilters() {
        // GIVEN
        ClassQuery classQuery = ClassQuery.inExactPackage(TEST_PACKAGE_NAME)
            .withSimpleNameContaining("Class")
            .withSimpleNameContaining("Package");
        Set<Class<?>> expectedClasses = ImmutableSet.of(
            ClassInTestPackage.class,
            AnotherClassInTestPackage.class
        );

        // WHEN
        Set<Class<?>> classes = classQuery.findClasses();

        // THEN
        assertExpectedClasses(
            expectedClasses,
            classes,
            "finding by exact package and class name containing",
            classQuery
        );
    }

    @Test
    void findClasses_byContainingPackageAndExactName_appliesAllFilters() {
        // GIVEN
        ClassQuery classQuery = ClassQuery.inContainingPackage(TEST_PACKAGE_NAME)
            .withExactSimpleName("DupedSimpleName");
        Set<Class<?>> expectedClasses = ImmutableSet.of(
            com.amazon.ata.test.reflect.testpackage.DupedSimpleName.class,
            com.amazon.ata.test.reflect.testpackage.anothersubpackage.DupedSimpleName.class
        );

        // WHEN
        Set<Class<?>> classes = classQuery.findClasses();

        // THEN
        assertExpectedClasses(
            expectedClasses,
            classes,
            "finding by containing package and exact class name",
            classQuery
        );
    }

    @Test
    void findClasses_byContainingPackageAndNameContaining_appliesAllFilters() {
        // GIVEN
        ClassQuery classQuery = ClassQuery.inContainingPackage(TEST_PACKAGE_NAME)
            .withSimpleNameContaining("Duped");
        Set<Class<?>> expectedClasses = ImmutableSet.of(
            com.amazon.ata.test.reflect.testpackage.DupedSimpleName.class,
            com.amazon.ata.test.reflect.testpackage.anothersubpackage.DupedSimpleName.class
        );

        // WHEN
        Set<Class<?>> classes = classQuery.findClasses();

        // THEN
        assertExpectedClasses(
            expectedClasses,
            classes,
            "finding by containing package and class name containing",
            classQuery
        );
    }

    @Test
    void findClasses_byContainingPackageAndMultipleNameContaining_appliesAllFilters() {
        // GIVEN
        ClassQuery classQuery = ClassQuery.inContainingPackage(TEST_PACKAGE_NAME)
            .withSimpleNameContaining("TestPackage")
            .withSimpleNameContaining("Another")
            .withSimpleNameContaining("Subpackage");
        Set<Class<?>> expectedClasses = ImmutableSet.of(ClassInTestPackageAnotherSubpackage.class);

        // WHEN
        Set<Class<?>> classes = classQuery.findClasses();

        // THEN
        assertExpectedClasses(
            expectedClasses,
            classes,
            "finding by containing package and class name containing",
            classQuery
        );
    }

    private void assertClassQueryReturnsClass(ClassQuery classQuery, Class<?> expectedClass, String whenCondition) {
        Set<Class<?>> classes = classQuery.findClasses();

        assertEquals(
            1,
            classes.size(),
            String.format(
                "Expected findClasses (when %s) to return just one class (%s), but instead found {%s}.%n" +
                    "Class query was: %s",
                whenCondition,
                expectedClass.toString(),
                classes.toString(),
                classQuery.toString())
        );

        Class<?> clazz = classes.iterator().next();
        assertExpectedClass(expectedClass, clazz, whenCondition, classQuery);
    }

    private void assertExpectedClass(
            Class<?> expectedClass,
            Class<?> actualClass,
            String whenCondition,
            ClassQuery classQueryThatWasApplied) {

        assertEquals(
            expectedClass,
            actualClass,
            String.format(
                "Expected finding class (when %s) to return %s, but found %s instead.%nClass query was: %s",
                whenCondition,
                expectedClass,
                actualClass,
                classQueryThatWasApplied)
        );
    }

    private void assertClassQueryReturnsClasses(
            ClassQuery classQuery,
            Set<Class<?>> expectedClasses,
            String whenCondition) {

        Set<Class<?>> classes = classQuery.findClasses();
        assertExpectedClasses(expectedClasses, classes, whenCondition, classQuery);
    }

    private void assertExpectedClasses(
            Set<Class<?>> expectedClasses,
            Set<Class<?>> actualClasses,
            String whenCondition,
            ClassQuery classQueryThatWasApplied) {

        assertEquals(
            expectedClasses,
            actualClasses,
            String.format(
                "Expected finding classes (when %s) to return exactly {%s}, but found {%s}.%nClass query was: %s",
                whenCondition,
                expectedClasses,
                actualClasses,
                classQueryThatWasApplied
            )
        );
    }
}
