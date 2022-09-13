package com.amazon.ata.test.reflect;

import com.amazon.ata.test.helper.AtaTestHelper;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Allows finding a Java {@code Method} using introspection by specifying
 * constraints on the method being sought (e.g. name/partial name, return value,
 * method arguments).
 * <p>
 * Allows for:
 * <ul>
 *     <li>finding exactly one method (and {@code fail()}-ing if unique method not found --
 *         {@code findMethodOrFail()}</li>
 *     <li>finding exactly one method (exception thrown if unique method not found) --
 *         {@code findMethod()}. This can be used to check for absence of a method, for example.</li>
 *     <li>searching for a {@code Set} of matching methods -- {@code findMethods()}</li>
 * </ul>
 * <p>
 * Specify the filters you need to find the method of interest:
 * <ul>
 *     <li>{@code inType}: (static, must be called first to initialize a {@code MethodQuery}).
 *         Specify the containing type (class, interface, enum). Can only be called once
 *         per query.</li>
 *     <li>{@code withReturnType}/{@code withVoidReturnType}: filter by return type.
 *         These two methods are mutually exclusive, so only use one of them.
 *         Both methods can only be called once per query.</li>
 *     <li>{@code withExactName}/{@code withNameContaining}: filter by the method name,
 *         either by exact name, or by partial name. These two methods are mutually exclusive,
 *         so only use one of them. {@code withExactName} can only be called once.
 *         {@code withNameContaining} can be called any number of times, but ALL of the
 *         provided {@code String}s will need to be found in the method name to match.</li>
 *     <li>{@code withExactArgs}: specify the arguments of the method of interest. Order does *not*
 *         matter. To keep the permutations computation sane, will reject more than
 *         {@code MAX_ARGUMENTS} arguments. Can repeat arg types
 *         This method can only be called once per query.</li>
 * </ul>
 *
 * Examples:
 * <ul>
 * <li><pre>
 * {@code
 *     Class<?> zooClass = findTheZooClass();
 *     Method getElephantMethod = MethodQuery.inType(zooClass)
 *                                           .withReturnType(Elephant.class)
 *                                           .withNameContaining("get")
 *                                           .findMethod();
 * }
 * </pre></li>
 *
 * <li><pre>
 * {@code
 *     Class<?> zooClass = findTheZooClass();
 *     Set<Method> getSetters = MethodQuery.inType(zooClass)
 *                                         .withNameContaining("set")
 *                                         .withVoidReturnType()
 *                                         .findMethods();
 * }
 * </pre></li>
 * </ul>
 *
 * <p>
 * {@code findMethodOrFail}:<br>
 * If no method is found, or if more than one method is found matching the criteria,
 * then will call {@code fail()} with meaningful assertion failure message.
 * </p>
 * <p>
 * {@code findMethod}:<br>
 * If no method is found, or if more than one method is found matching the criteria,
 * then an appropriate {@code MethodQueryException} subclass is thrown.
 * </p>
 * <p>
 * {@code findMethods}:<br>
 * Will return a {@code Set<Method>}, whether 0, 1, or many {@code Method}s are found.
 * </p>
 *
 * @see com.amazon.ata.test.assertions.IntrospectionAssertions for assertions on
 * methods after the method of interest is returned.
 * @see MethodInvoker for means to invoke methods with reflection.
 *
 * For further improvements, see https://sim.amazon.com/issues/ATAENG-1614
 */
public final class MethodQuery {
    /** maximum allowed number of argument types to specify with {@code withExactArgTypes}. */
    public static final int MAX_ARGUMENTS = 8;

    private final Class<?> clazz;
    private final Class<?> returnType;
    private final String exactMethodName;
    private final Set<String> methodNameContainingSubstrings;
    private final List<Class<?>> exactArgTypes;

    private MethodQuery(
            final Class<?> clazz,
            final Class<?> returnType,
            final String exactMethodName,
            final Set<String> methodNameContainingSubstrings,
            final List<Class<?>> exactArgTypes) {

        this.clazz = clazz;
        this.returnType = returnType;
        this.exactMethodName = exactMethodName;
        if (null == methodNameContainingSubstrings) {
            this.methodNameContainingSubstrings = new HashSet<>();
        } else {
            this.methodNameContainingSubstrings = methodNameContainingSubstrings;
        }
        this.exactArgTypes = exactArgTypes;
    }

    private MethodQuery(final Class<?> clazz) {
        this(clazz, null, null, null, null);
    }

    /**
     * Creates a new {@code MethodQuery} by specifying the {@code Class} in which
     * to look for the method(s) of interest.
     * <p>
     * Providing a {@code null} type results in {@code IllegalArgumentException}.
     * </p>
     *
     * @param clazz The class in which to look for the method(s) of interest
     * @return a new {@code MethodQuery} to build a query from
     */
    public static MethodQuery inType(final Class<?> clazz) {
        if (null == clazz) {
            throw new IllegalArgumentException("Cannot query a method on a null type");
        }

        return new MethodQuery(clazz);
    }

    /**
     * Applies a filter based on return type of the method.
     * <p>
     * The return type must match exactly (cannot specify a supertype of the method's
     * return type and get a match).
     * </p>
     * <p>
     * This method can only be called once. Multiple calls will result in
     * {@code IllegalStateException}. Similarly, cannot be called if
     * {@code withVoidReturnType} has already been called.
     * </p>
     * <p>
     * Passing in {@code null} will result in {@code IllegalArgumentException}.
     * </p>
     *
     * @param returnTypeToUse The return type to use as filter in finding method
     * @return an updated {@code MethodQuery} with new filter applied
     */
    public MethodQuery withReturnType(final Class<?> returnTypeToUse) {
        ensureReturnTypeNotAlreadySet();
        if (null == returnTypeToUse) {
            throw new IllegalArgumentException();
        }

        return new MethodQuery(
            this.clazz, returnTypeToUse, this.exactMethodName, this.methodNameContainingSubstrings, this.exactArgTypes
        );
    }

    /**
     * Applies a filter specifying that return type of method is {@code void}.
     *
     * <p>
     * This method can only be called once. Multiple calls will result in
     * {@code IllegalStateException}. Similarly, cannot be called if
     * {@code withReturnType} has already been called.
     * </p>
     *
     * @return an updated {@code MethodQuery} with new filter applied
     */
    public MethodQuery withVoidReturnType() {
        ensureReturnTypeNotAlreadySet();

        return new MethodQuery(
            this.clazz, Void.TYPE, this.exactMethodName, this.methodNameContainingSubstrings, this.exactArgTypes
        );
    }

    /**
     * Applies a filter specifying exact method name.
     * <p>
     * This method can only be called once. Multiple calls will result in
     * {@code IllegalStateException}. Similarly, cannot be called if
     * {@code withNameContaining} has already been called.
     * </p>
     * <p>
     * Passing in {@code null} will result in {@code IllegalArgumentException}.
     * </p>
     *
     * @param methodName The exact method name to look for in the class
     * @return an updated {@code MethodQuery} with new filter applied
     */
    public MethodQuery withExactName(final String methodName) {
        ensureMethodNameConstraintsNotAlreadySet();
        if (null == methodName || methodName.isEmpty()) {
            throw new IllegalArgumentException("Cannot provide null or empty method name");
        }

        return new MethodQuery(
            this.clazz, this.returnType, methodName, this.methodNameContainingSubstrings, this.exactArgTypes
        );
    }

    /**
     * Applies a filter specifying that the method name contains the provided substring.
     * <p>
     * This filter *is* case-sensitive.
     * </p>
     * <p>
     * This method can be called any number of times and each name filter will be
     * AND-ed together so that all provided substrings must be found in method name.
     * </p>
     * <p>
     * This method cannot be called after {@code withExactName}; this will result in
     * {@code IllegalStateException}.
     * </p>
     *
     * @param methodNameSubstring The substring of the method name to search for
     * @return an updated {@code MethodQuery} with new filter applied
     */
    public MethodQuery withNameContaining(final String methodNameSubstring) {
        ensureExactMethodNameNotAlreadySet();
        if (null == methodNameSubstring || methodNameSubstring.isEmpty()) {
            throw new IllegalArgumentException("Cannot provide null or empty method name substring");
        }

        Set<String> newMethodNameContainingSubstrings = new HashSet<>(methodNameContainingSubstrings);
        newMethodNameContainingSubstrings.add(methodNameSubstring);

        return new MethodQuery(
            this.clazz, this.returnType, this.exactMethodName, newMethodNameContainingSubstrings, this.exactArgTypes
        );
    }

    /**
     * Applies a filter specifying the exact argument types for the method.
     *
     * <p>
     * The types must match exactly (providing a superclass of the argument type will
     * not result in a match).
     * </p>
     * <p>
     * The order of argument types does not matter.
     * </p>
     * <p>
     * If the method accepts more than one argument of the same type, then the
     * argument type must be repeated in the list provided.
     * </p>
     * <p>
     * If {@code argTypes} is {@code null}, throws {@code IllegalArgumentException}.
     * </p>
     * <p>
     * This method can only be called once. Multiple calls will result in
     * {@code IllegalStateException}.
     * </p>
     * <p>
     * To keep computation of argument permutations under control, passing in
     * more than {@code MAX_ARGUMENTS} will result in an {@code IllegalArgumentException}.
     * </p>
     *
     * @param argTypes {@code Collection<Class<?>>} of argument types. List to allow repeated
     *                                       arg types
     * @return an updated {@code MethodQuery} with new filter applied
     */
    public MethodQuery withExactArgTypes(final Collection<Class<?>> argTypes) {
        ensureExactArgTypesNotAlreadySet();
        if (null == argTypes) {
            throw new IllegalArgumentException("argTypes cannot be null");
        }
        if (argTypes.size() > MAX_ARGUMENTS) {
            throw new IllegalArgumentException(
                String.format(
                    "withExactArgTypes will not accept more than %d arguments: %s",
                    MAX_ARGUMENTS,
                    argTypes.toString())
            );
        }
        for (Class<?> argType : argTypes) {
            if (null == argType) {
                throw new IllegalArgumentException("argTypes contained a null: " + argTypes.toString());
            }
        }

        return new MethodQuery(
            this.clazz,
            this.returnType,
            this.exactMethodName,
            this.methodNameContainingSubstrings,
            new ArrayList<>(argTypes)
        );
    }

    /**
     * Returns the unique method matching the specified filters, if one exists.
     *
     * <p>
     * If no method is found, or if too many methods found, will {@code fail()}
     * (JUnit) with meaningful message.
     * </p>
     *
     * @return the unique {@code Method} matching the criteria
     */
    public Method findMethodOrFail() {
        Method result = null;

        try {
            result = findMethod();
        } catch (MethodQueryException e) {
            AtaTestHelper.failTestWithException(e, e.getMessage());
        }

        return result;
    }

    /**
     * Returns the unique method matching the specified filters, if one exists.
     *
     * <ul>
     * <li>Throws {@code NoMethodFoundException} if no matching method is found.</li>
     * <li>Throws {@code MultipleMethodsFoundException} if more than one matching method is found.</li>
     * </ul>
     *
     * @return the unique {@code Method} matching the criteria
     */
    public Method findMethod() {
        Set<Method> methods = findMethods();
        if (methods.size() == 0) {
            throw new NoMethodFoundException(this);
        }
        if (methods.size() > 1) {
            throw new MultipleMethodsFoundException(this, methods);
        }

        return methods.iterator().next();
    }

    /**
     * Returns all methods matching the specified filters if any exist, empty {@code Set} otherwise.
     *
     * @return the set of matching methods if any exist, empty set otherwise
     */
    public Set<Method> findMethods() {
        Set<Method> matchingMethods = new HashSet<>();

        // for each set of filters, findMethods() and union with methods found so far
        for (Set<Predicate<? super Method>> currFilterSet : getFilterPermutations()) {
            Predicate<? super Method>[] filterArray = (Predicate []) currFilterSet.toArray(new Predicate[0]);
            matchingMethods.addAll(ReflectionUtils.getMethods(clazz, filterArray));
        }

        return filterOutTestMethods(matchingMethods);
    }

    /*
     * generates all possible sets of predicates needed, due to allowing arg types to
     * be specified in any order.
     */
    private List<Set<Predicate<? super Method>>> getFilterPermutations() {
        // List of all sets of filters to try
        List<Set<Predicate<? super Method>>> filterPermutations = new ArrayList<>();
        // the filters that will be the same for every call to ReflectionUtils.findMethods()
        Set<Predicate<? super Method>> fixedFilters = allFixedFilters();

        if (null == exactArgTypes) {
            return ImmutableList.of(fixedFilters);
        }

        // iterate through all permutations of arg types, generating a full set of
        // predicates for each one so we can try each one with ReflectionUtils.findMethods()
        for (List<Class<?>> argTypePermutation : Collections2.permutations(exactArgTypes)) {
            Class<?>[] argTypesArray = (Class []) argTypePermutation.toArray(new Class[0]);

            Set<Predicate<? super Method>> filterSet = new HashSet<>(fixedFilters);
            filterSet.add(ReflectionUtils.withParameters(argTypesArray));
            filterPermutations.add(filterSet);
        }

        return filterPermutations;
    }

    /* pulls all of the non-arg-type filters together into a Set */
    private Set<Predicate<? super Method>> allFixedFilters() {
        Set<Predicate<? super Method>> fixedFilters = new HashSet<>();

        if (null != returnType) {
            fixedFilters.add(returnTypeFilter());
        }

        if (null != exactMethodName) {
            fixedFilters.add(exactMethodNameFilter());
        }

        if (null != methodNameContainingSubstrings) {
            for (String nameSubstring : methodNameContainingSubstrings) {
                fixedFilters.add(methodNameSubstringFilter(nameSubstring));
            }
        }

        return fixedFilters;
    }

    // compute individual ReflectionUtils-friendly predicates representing the filters

    private Predicate<? super Method> returnTypeFilter() {
        return ReflectionUtils.withReturnType(returnType);
    }

    private Predicate<? super Method> exactMethodNameFilter() {
        return ReflectionUtils.withName(exactMethodName);
    }

    private Predicate<? super Method> methodNameSubstringFilter(final String nameSubstring) {
        return m -> m.getName().contains(nameSubstring);
    }

    private Set<Method> filterOutTestMethods(final Set<Method> methods) {
        return methods.stream()
            // when running unit tests, JaCoCo seems to inject this method...ignore it
            .filter(m -> !m.getName().equals("$jacocoInit"))
            .collect(Collectors.toSet());
    }

    // throw exceptions if attempt is made to overwrite filters -- consider these bugs

    private void ensureReturnTypeNotAlreadySet() {
        if (null != this.returnType) {
            throw new IllegalStateException("Return type filter already exists: " + returnType.toString());
        }
    }

    private void ensureMethodNameConstraintsNotAlreadySet() {
        ensureExactMethodNameNotAlreadySet();
        if (!methodNameContainingSubstrings.isEmpty()) {
            throw new IllegalStateException(
                "Name filter(s) already exist(s): " + methodNameContainingSubstrings.toString()
            );
        }
    }

    private void ensureExactMethodNameNotAlreadySet() {
        if (null != exactMethodName) {
            throw new IllegalStateException(String.format("Exact name filter already exists: '%s'", exactMethodName));
        }
    }

    private void ensureExactArgTypesNotAlreadySet() {
        if (null != exactArgTypes) {
            throw new IllegalStateException("Exact arg types filter already exists: " + exactArgTypes.toString());
        }
    }

    /* allow exceptions to access type for error message purposes */
    Class<?> getContainingType() {
        return clazz;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{MethodQuery | ")
            .append("class: ").append(clazz);

        if (null != returnType) {
            sb.append(String.format(", returnType: %s", returnType.toString()));
        }

        if (null != exactMethodName) {
            sb.append(String.format(", exactMethodName: %s", exactMethodName));
        } else if (null != methodNameContainingSubstrings && !methodNameContainingSubstrings.isEmpty()) {
            sb.append(
                String.format(", methodNameContainingSubstrings: [%s]", methodNameContainingSubstrings.toString())
            );
        }

        if (null != exactArgTypes) {
            sb.append(String.format(", exactArgTypes: [%s]", exactArgTypes.toString()));
        }

        return sb.append("}").toString();
    }
}
