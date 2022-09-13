package com.amazon.ata.test.assertions;

import com.google.common.annotations.VisibleForTesting;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.opentest4j.AssertionFailedError;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.amazon.ata.test.assertions.IntrospectionAssertions.annotationsIncludeMock;
import static com.amazon.ata.test.assertions.IntrospectionAssertions.assertClassContainsMemberMethodNames;
import static com.amazon.ata.test.assertions.IntrospectionAssertions.assertClassContainsMemberVariableTypes;
import static com.amazon.ata.test.assertions.IntrospectionAssertions.assertClassDoesNotContainMemberMethodNames;
import static com.amazon.ata.test.assertions.IntrospectionAssertions.assertDirectlyExtends;
import static com.amazon.ata.test.assertions.IntrospectionAssertions.assertDoesNotImplementInterface;
import static com.amazon.ata.test.assertions.IntrospectionAssertions.assertImplementsInterface;
import static com.amazon.ata.test.assertions.IntrospectionAssertions.assertMemberMocked;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntrospectionAssertionsTest {

    private static class TestSuperclass {}
    private static class TestSubclass extends TestSuperclass { }

    private static class MemberlessClass { }

    private static class OneMemberClass {
        private int member1;

        public int getMember1() { return member1; }
    }

    private static class MultiMemberClass {
        private int member1;
        private List<String> member2;
        private InputStream member3;

        public int getMember1() { return member1; }
        public List<String> getMember2() { return member2; }
        public InputStream getMember3() { return member3; }
    }

    private static class RepeatedMemberTypeClass {
        private int member1;
        private List<String> member2;
        private List<String> member3;

        public int getMember1() { return member1; }
        public List<String> getMember2() { return member2; }
        public List<String> getMember3() { return member3; }
    }

    private static class OverloadedMethodsClass {
        public int overloadedMethod() { return 0; }
        public int overloadedMethod(int i) { return i; }
        public int overloadedMethod(int i, int j) { return i + j; }
        public String otherMethod() { return "something else"; }
    }

    private static interface IntrospectionTestable {
        void aMethod();
    }

    private static class ImplementerOfIntrospectionTestable implements IntrospectionTestable {
        public void aMethod() {}
    }

    private static class TestClassWithMock {
        @VisibleForTesting
        @Mock
        private IntrospectionTestable mockedField;

        @VisibleForTesting
        private String nonMockedField;

        private Integer noAnnotationAtAllField;
    }

    @Test
    void assertDirectlyExtends_subclassIsSubclassOfSuperclass_noAssertFires() {
        // GIVEN - subclass is subclass of expected superclass

        // WHEN + THEN - no assert failure
        assertDirectlyExtends(TestSubclass.class, TestSuperclass.class);
    }

    @Test
    void assertDirectlyExtends_subclassIsSuperclassOfSuperclass_assertFires() {
        // GIVEN - subclass is *super*class of expected superclass
        // WHEN + THEN - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertDirectlyExtends(TestSuperclass.class, TestSubclass.class));
    }

    @Test
    void assertDirectlyExtends_subclassUnrelatedToSuperclass_assertFires() {
        // GIVEN - subclass and superclass are unrelated
        // WHEN + THEN - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertDirectlyExtends(TestSubclass.class, OneMemberClass.class));
    }

    @Test
    void assertImplementsInterface_subtypeDirectlyImplementsInterface_noAssertFires() {
        // GIVEN - subtype of an interface
        // WHEN + THEN - no assert failure
        assertImplementsInterface(ImplementerOfIntrospectionTestable.class, IntrospectionTestable.class);
    }

    @Test
    void assertImplementsInterface_subtypeIndirectlyImplementsInterface_noAssertFires() {
        // GIVEN - subtype of an interface, but indirectly so
        // WHEN + THEN - no assert failure
        assertImplementsInterface(RuntimeException.class, Serializable.class);
    }

    @Test
    void assertImplementsInterface_sameClassType_assertFires() {
        // GIVEN - same subtype for both arguments
        Class<?> subtype = ImplementerOfIntrospectionTestable.class;
        // WHEN + THEN - assert failure
        assertThrows(AssertionFailedError.class, () -> assertImplementsInterface(subtype, subtype));
    }

    @Test
    void assertImplementsInterface_sameInterfaceType_assertFires() {
        // GIVEN - same interface for both arguments
        Class<?> implementable = IntrospectionTestable.class;
        // WHEN + THEN - assert failure
        assertThrows(AssertionFailedError.class, () -> assertImplementsInterface(implementable, implementable));
    }

    @Test
    void assertImplementsInterface_unrelatedSubtypeAndInterface_assertFires() {
        // GIVEN
        // a class
        Class<?> clazz = ImplementerOfIntrospectionTestable.class;
        // an interface *not* implemented by the class
        Class<?> unrelatedInterface = Runnable.class;

        // WHEN + THEN - assert failure
        assertThrows(AssertionFailedError.class, () -> assertImplementsInterface(clazz, unrelatedInterface));
    }

    @Test
    void assertDoesNotImplementInterface_subtypeDirectlyImplementsInterface_assertFires() {
        // GIVEN - subtype of an interface
        // WHEN + THEN - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertDoesNotImplementInterface(ImplementerOfIntrospectionTestable.class,
                                                  IntrospectionTestable.class)
        );
    }

    @Test
    void assertDoesNotImplementInterface_subtypeIndirectlyImplementsInterface_assertFires() {
        // GIVEN - subtype of an interface, but indirectly so
        // WHEN + THEN - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertDoesNotImplementInterface(RuntimeException.class, Serializable.class)
        );
    }

    @Test
    void assertDoesNotImplementInterface_sameClassType_assertFires() {
        // GIVEN - same subtype for both arguments
        Class<?> subtype = ImplementerOfIntrospectionTestable.class;
        // WHEN + THEN - assert failure
        assertThrows(AssertionFailedError.class, () -> assertDoesNotImplementInterface(subtype, subtype));
    }

    @Test
    void assertDoesNotImplementInterface_sameInterfaceType_assertFires() {
        // GIVEN - same interface for both arguments
        Class<?> implementable = IntrospectionTestable.class;
        // WHEN + THEN - assert failure
        assertThrows(AssertionFailedError.class, () -> assertDoesNotImplementInterface(implementable, implementable));
    }

    @Test
    void assertDoesNotImplementInterface_unrelatedSubtypeAndInterface_noAssertFires() {
        // GIVEN
        // a class
        Class<?> clazz = ImplementerOfIntrospectionTestable.class;
        // an interface *not* implemented by the class
        Class<?> unrelatedInterface = Runnable.class;

        // WHEN + THEN - no assert failure
        assertDoesNotImplementInterface(clazz, unrelatedInterface);
    }

    @Test
    void assertClassContainsOnlyMemberVariableTypes_noTypesOnMemberLessClass_noAssertFire() {
        // GIVEN - memberless class
        Class<?> clazz = MemberlessClass.class;
        // empty array of member types
        String[] memberTypes = {};

        // WHEN + THEN - no assert failure
        assertClassContainsMemberVariableTypes(clazz, memberTypes);
    }

    @Test
    void assertClassContainsOnlyMemberVariableTypes_typeOnMemberLessClass_assertFire() {
        // GIVEN - memberless class
        Class<?> clazz = MemberlessClass.class;
        // non-empty array of member types
        String[] memberTypes = {"int"};

        // WHEN + THEN - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertClassContainsMemberVariableTypes(clazz, memberTypes));
    }

    @Test
    void assertClassContainsOnlyMemberVariableTypes_singleTypeMatchesClassMember_noAssertFire() {
        // GIVEN - single member class
        Class<?> clazz = OneMemberClass.class;
        // matching array of member types
        String[] memberTypes = {"int"};

        // WHEN + THEN - no assert failure
        assertClassContainsMemberVariableTypes(clazz, memberTypes);
    }

    @Test
    void assertClassContainsOnlyMemberVariableTypes_singleTypeDoesNotMatchClassMember_assertFire() {
        // GIVEN - single member class
        Class<?> clazz = OneMemberClass.class;
        // mismatching array of member types
        String[] memberTypes = {"java.lang.String"};

        // WHEN + THEN - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertClassContainsMemberVariableTypes(clazz, memberTypes));
    }

    @Test
    void assertClassContainsOnlyMemberVariableTypes_typesMatchClassMembers_noAssertFire() {
        // GIVEN - multi member class
        Class<?> clazz = MultiMemberClass.class;
        // matching array of member types
        String[] memberTypes = {"java.io.InputStream", "int", "java.util.List"};

        // WHEN + THEN - no assert failure
        assertClassContainsMemberVariableTypes(clazz, memberTypes);
    }

    @Test
    void assertClassContainsOnlyMemberVariableTypes_repeatedTypesMatchClassMembers_noAssertFire() {
        // GIVEN - class with more than one member of same type
        Class<?> clazz = RepeatedMemberTypeClass.class;
        // matching array of member types
        String[] memberTypes = {"java.util.List", "int", "java.util.List"};

        // WHEN + THEN - no assert failure
        assertClassContainsMemberVariableTypes(clazz, memberTypes);
    }

    @Test
    void assertClassContainsOnlyMemberVariableTypes_typesDoNotMatchClassMembers_assertFire() {
        // GIVEN - multi member class
        Class<?> clazz = MultiMemberClass.class;
        // mismatching array of member types
        String[] memberTypes = {"int", "java.util.List", "java.lang.String"};

        // WHEN + THEN - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertClassContainsMemberVariableTypes(clazz, memberTypes));
    }

    @Test
    void assertClassContainsOnlyMemberVariableTypes_moreRepeatedTypeThanInClass_assertFire() {
        // GIVEN - class with more than one member of same type
        Class<?> clazz = RepeatedMemberTypeClass.class;
        // array with more instances of repeated member type than are in class
        String[] memberTypes = {"java.util.List", "int", "java.util.List", "java.util.List"};

        // WHEN + THEN - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertClassContainsMemberVariableTypes(clazz, memberTypes));
    }


    @Test
    void assertClassContainsMemberMethodNames_singleMethodInClass_noAssertFire() {
        // GIVEN - class with single method
        Class<?> clazz = OneMemberClass.class;
        // array with same method name
        String[] methodNames = {"getMember1"};

        // WHEN + THEN - no assert failure
        assertClassContainsMemberMethodNames(clazz, methodNames);
    }

    @Test
    void assertClassContainsMemberMethodNames_multipleMethodsInClass_noAssertFire() {
        // GIVEN - class with multiple methods
        Class<?> clazz = MultiMemberClass.class;
        // array with matching method names
        String[] methodNames = {"getMember1", "getMember2", "getMember3"};

        // WHEN + THEN - no assert failure
        assertClassContainsMemberMethodNames(clazz, methodNames);
    }

    @Test
    void assertClassContainsMemberMethodNames_overloadedMethodsInClass_noAssertFire() {
        // GIVEN - class with overloaded methods
        Class<?> clazz = OverloadedMethodsClass.class;
        // array with matching method names
        String[] methodNames = {"overloadedMethod", "overloadedMethod", "overloadedMethod", "otherMethod"};

        // WHEN + THEN - no assert failure
        assertClassContainsMemberMethodNames(clazz, methodNames);
    }

    @Test
    void assertClassContainsMemberMethodNames_methodNotInClass_assertFire() {
        // GIVEN - class with single method
        Class<?> clazz = OneMemberClass.class;
        // array with different method name
        String[] methodNames = {"andNowForSomethingCompletelyDifferent"};

        // WHEN + THEN - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertClassContainsMemberMethodNames(clazz, methodNames));
    }

    @Test
    void assertClassContainsMemberMethodNames_moreOverloadedMethodsThanInClass_assertFire() {
        // GIVEN - class with overloaded methods
        Class<?> clazz = OverloadedMethodsClass.class;
        // array with more instances of overloaded method than in class
        String[] methodNames = {"overloadedMethod", "overloadedMethod", "overloadedMethod", "overloadedMethod"};

        // WHEN + THEN - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertClassContainsMemberMethodNames(clazz, methodNames));
    }


    @Test
    void assertClassDoesNotContainMemberMethodNames_methodNotInClass_noAssertFire() {
        // GIVEN - class with single method
        Class<?> clazz = OneMemberClass.class;
        // array with different method name
        String[] methodNames = {"andNowForSomethingCompletelyDifferent"};

        // WHEN + THEN - no assert fire
        assertClassDoesNotContainMemberMethodNames(clazz, methodNames);
    }

    @Test
    void assertClassDoesNotContainMemberMethodNames_methodInClass_assertFire() {
        // GIVEN - class with single method
        Class<?> clazz = OneMemberClass.class;
        // array with matching method name
        String[] methodNames = {"getMember1"};

        // WHEN + THEN - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertClassDoesNotContainMemberMethodNames(clazz, methodNames));
    }

    @Test
    void assertClassDoesNotContainMemberMethodNames_multipleMethodsNoneInClass_noAssertFire() {
        // GIVEN - class with multiple methods
        Class<?> clazz = MultiMemberClass.class;
        // methods not in class
        String[] methodNames = {"nothingHere", "nope", "getSomethingElse"};

        // WHEN + THEN - no assert falure
        assertClassDoesNotContainMemberMethodNames(clazz, methodNames);
    }

    @Test
    void assertClassDoesNotContainMemberMethodNames_multipleMethodsOneInClass_assertFire() {
        // GIVEN - class with multiple methods
        Class<?> clazz = MultiMemberClass.class;
        // methods not in class + one that is
        String[] methodNames = {"nothingHere", "nope", "getSomethingElse", "getMember2"};

        // WHEN + THEN - assert falure
        assertThrows(
            AssertionFailedError.class,
            () -> assertClassDoesNotContainMemberMethodNames(clazz, methodNames));
    }

    @Test
    void assertMemberMocked_onMockedField_noAssertFires() throws NoSuchFieldException {
        // GIVEN
        // test class
        Class<?> testClass = TestClassWithMock.class;
        // field type
        Class<?> mockedFieldType = IntrospectionTestable.class;
        // field that is mocked
        Field mockedField = testClass.getDeclaredField("mockedField");

        // WHEN + THEN - no assert failure
        assertMemberMocked(mockedField, testClass, mockedFieldType);
    }

    @Test
    void assertMemberMocked_onNullField_assertFires() {
        // GIVEN
        // test class
        Class<?> testClass = TestClassWithMock.class;
        // field type
        Class<?> mockedFieldType = IntrospectionTestable.class;

        // WHEN + THEN - assert failure
        assertThrows(AssertionFailedError.class, () -> assertMemberMocked(null, testClass, mockedFieldType));
    }

    @Test
    void assertMemberMocked_onNonMockedField_assertFires() throws NoSuchFieldException {
        // GIVEN
        // test class
        Class<?> testClass = TestClassWithMock.class;
        // field type
        Class<?> mockedFieldType = IntrospectionTestable.class;
        // field that isn't mocked
        Field nonMockedField = testClass.getDeclaredField("nonMockedField");

        // WHEN + THEN - assert failure
        assertThrows(AssertionFailedError.class, () -> assertMemberMocked(nonMockedField, testClass, mockedFieldType));
    }

    @Test
    void annotationsIncludeMock_onOnlyMockAnnotation_returnsTrue() throws NoSuchFieldException {
        // GIVEN - annotations array including Mock
        Annotation[] annotations = TestClassWithMock.class.getDeclaredField("mockedField").getAnnotations();

        // WHEN
        boolean result = annotationsIncludeMock(annotations);

        // THEN
        assertTrue(
            result,
            String.format("Should have found @Mock annotation in array: %s", Arrays.toString(annotations))
        );
    }

    @Test
    void annotationsIncludeMock_onAnnotationsOtherThanMock_returnsFalse() throws NoSuchFieldException {
        // GIVEN - annotations array excluding Mock
        Annotation[] annotations = TestClassWithMock.class.getDeclaredField("nonMockedField").getAnnotations();

        // WHEN
        boolean result = annotationsIncludeMock(annotations);

        // THEN
        assertFalse(
            result,
            String.format("Should *not* have found @Mock annotation in array: %s", Arrays.toString(annotations))
        );
    }

    @Test
    void annotationsIncludeMock_onEmptyArray_returnsFalse() throws NoSuchFieldException {
        // GIVEN - annotations array that should be empty
        Annotation[] annotations =
            TestClassWithMock.class.getDeclaredField("noAnnotationAtAllField").getAnnotations();

        // WHEN
        boolean result = annotationsIncludeMock(annotations);

        // THEN
        assertFalse(
            result,
            String.format("Should *not* have found @Mock annotation in empty array: %s", Arrays.toString(annotations))
        );
    }
}
