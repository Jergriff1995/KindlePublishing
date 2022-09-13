package com.amazon.ata.test.reflect;

import com.amazon.ata.test.helper.AtaTestHelper;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Allows finding a Java {@code Class} object using introspection by specifying
 * constraints on the class being sought (e.g. name/partial name, containing package).
 * <p>
 * Allows for:
 * <ul>
 *     <li>
 *         finding exactly one class (and {@code fail()}-ing if unique class not found--
 *         {@code findClassOrFail()}
 *     </li>
 *     <li>
 *         finding exactly one class (exception thrown if unique class not found) --
 *         {@code findClass()}. This can be used to check for absence of a class, for example.
 *     </li>
 *     <li>searching for a {@code Set} of matching classes -- {@code findClasses()}</li>
 * </ul>
 * <p>
 * Specify the filters you need to find the class of interest:
 * <ul>
 *     <li>
 *         {@code inExactPackage}/{@code inContainingPackage}: (static, one of these must be
 *         called first to initialize a {@code ClassQuery}. Specify a Java package that the
 *         class should be found inside (either the exact package, or a package that
 *         might contain a subpackage that contains the class. Can only be called once
 *         per query.
 *     </li>
 *     <li>
 *         {@code withExactSimpleName}:
 *     </li>
 *     <li>
 *         {@code withSimpleNameContaining}:
 *     </li>
 * </ul>
 *
 * Examples:
 * <ul>
 *     <li><pre>
 * {@code
 *     Class<?> zooClass = ClassQuery.inExactPackage("com.amazon.ata.parks")
 *                                   .withExactSimpleName("Zoo")
 *                                   .findClass();
 * }
 *     </pre></li>
 *     <li><pre>
 * {@code
 *     Set<Class<?>> allAtaTestClasses = ClassQuery.inContainingPackage("com.amazon.ata")
 *                                                 .withSimpleNameContaining("Test")
 *                                                 .findClasses();
 * }
 *     </pre></li>
 * </ul>
 *
 * <p>
 * {@code findClassOrFail}:<br>
 * If no class is found, or if more than one class is found matching the criteria,
 * then will call {@code fail()} with meaningful assertion failure message.
 * </p>
 * <p>
 * {@code findClass}:<br>
 * If no class is found, or if more than one class is found matching the criteria,
 * then an {@code IllegalStateException} is thrown.
 * </p>
 * <p>
 * {@code findClasses}:<br>
 * Will return a {@code Set<Class<?>>}, whether 0, 1, or many {@code Class}es are found.
 * </p>
 *
 * @see com.amazon.ata.test.assertions.IntrospectionAssertions for assertions on
 * classes after the class of interest is returned.
 *
 * For further improvements (e.g. {@code subtypeOf}), see https://sim.amazon.com/issues/ATAENG-1614
 */
public final class ClassQuery {
    private static final Pattern VALID_PACKAGE_PATTERN = Pattern.compile("[^.]+\\.[^.]+");
    private final String packageName;
    private final boolean packageNameIsExact;
    // anticipating support for specifying supertype(s) in filters. See ATAENG-1614
    private final Set<Class<?>> superTypes;
    private final String exactSimpleName;
    private final Set<String> classNameContainingSubstrings;
    private Class<?> subTypeOf = Object.class;

    private ClassQuery(
            final String packageName,
            final boolean packageNameIsExact,
            final Set<Class<?>> superTypes,
            final String exactSimpleName,
            final Set<String> classNameContainingSubstrings) {

        this.packageName = packageName;
        this.packageNameIsExact = packageNameIsExact;
        if (null == superTypes) {
            this.superTypes = new HashSet<>();
        } else {
            this.superTypes = superTypes;
        }
        this.exactSimpleName = exactSimpleName;
        if (null == classNameContainingSubstrings) {
            this.classNameContainingSubstrings = new HashSet<>();
        } else {
            this.classNameContainingSubstrings = classNameContainingSubstrings;
        }
    }

    private ClassQuery(final String packageName, final boolean packageNameIsExact) {
        this(packageName, packageNameIsExact, null, null, null);
    }

    /**
     * Creates a new {@code ClassQuery} by specifying the exact package in which to look
     * for the class(es) of interest.
     * <p>
     *     This method can only be called once per query.
     * </p>
     * <p>
     *     Providing a {@code null} or empty package name results in {@code IllegalArgumentException}.
     * </p>
     * @param exactPackageName The exact package in which to look for classes
     * @return a new {@code ClassQuery} to build a query from
     */
    public static ClassQuery inExactPackage(final String exactPackageName) {
        if (null == exactPackageName || exactPackageName.isEmpty()) {
            throw new IllegalArgumentException("Cannot query a class with null or empty exact package name");
        }
        validatePackageName(exactPackageName);

        return new ClassQuery(exactPackageName, true);
    }

    /**
     * Creates a new {@code ClassQuery} by specifying a super-package in which to look
     * for the class(es) of interest.
     * <p>
     *     This method can only be called once per query.
     * </p>
     * <p>
     *     Providing a {@code null} or empty package name results in {@code IllegalArgumentException}.
     * </p>
     * @param containingPackageName The package in which to look for classes (including in
     *                              sub-packages
     * @return a new {@code ClassQuery} to build a query from
     */
    public static ClassQuery inContainingPackage(final String containingPackageName) {
        if (null == containingPackageName || containingPackageName.isEmpty()) {
            throw new IllegalArgumentException("Cannot query a class with null or empty containing package name");
        }
        validatePackageName(containingPackageName);

        return new ClassQuery(containingPackageName, false);
    }

    /**
     * Applies a filter specifying the exact class name.
     * <p>
     *     This method can only be called once per query. Multiple calls will result in
     *     {@code IllegalArgumentException}. Similarly, cannot be called if
     *     {@code withSimpleNameContaining} has already been called.
     * </p>
     * <p>
     *     Passing in {@code null} or empty class name will result in {@code IllegalArgumentException}.
     * </p>
     * @param simpleName The exact class name to look for
     * @return an updated {@code ClassQuery} with new filter applied
     */
    public ClassQuery withExactSimpleName(final String simpleName) {
        ensureClassNameConstraintsNotAlreadySet();
        if (null == simpleName || simpleName.isEmpty()) {
            throw new IllegalArgumentException("Cannot provide null or empty class name");
        }

        return new ClassQuery(
            this.packageName,
            this.packageNameIsExact,
            this.superTypes,
            simpleName,
            this.classNameContainingSubstrings
        );
    }

    /**
     * Applies a filter specifying a substring of class name to look for.
     * <p>
     *     This filter *is* case-sensitive.
     * </p>
     * <p>
     *     This method can be called any number of times and each name filter will be
     *     AND-ed together so that all provided substrings must be found in class name.
     * </p>
     * <p>
     *     This method cannot be called after {@code withExactSimpleName}; this will result in
     *     {@code IllegalStateException}.
     * </p>
     * <p>
     *     Passing in {@code null} or empty class name will result in {@code IllegalArgumentException}.
     * </p>
     * @param nameContainingSubstring The substring of the class name to look for
     * @return an updated {@code ClassQuery} with new filter applied
     */
    public ClassQuery withSimpleNameContaining(final String nameContainingSubstring) {
        ensureExactClassNameNotAlreadySet();
        if (null == nameContainingSubstring || nameContainingSubstring.isEmpty()) {
            throw new IllegalArgumentException("Cannot provide null or empty class name substring");
        }

        Set<String> newClassNameContainingSubstrings = new HashSet<>(classNameContainingSubstrings);
        newClassNameContainingSubstrings.add(nameContainingSubstring);

        return new ClassQuery(
            this.packageName,
            this.packageNameIsExact,
            this.superTypes,
            this.exactSimpleName,
            newClassNameContainingSubstrings
        );
    }

    public ClassQuery withSubTypeOf(Class<?> subTypeOfClass) {
        this.subTypeOf = subTypeOfClass;
        return this;
    }

    /**
     * Returns the unique class matching the specified filters, if one exists.
     * <p>
     *     If no class is found, or if too many classes are found, will {@code fail()} (JUnit)
     *     with meaningful message.
     * </p>
     *
     * @return the unique {@code Class<>} matching the criteria
     */
    public Class<?> findClassOrFail() {
        Class<?> result = null;

        try {
            result = findClass();
        } catch (ClassQueryException e) {
            AtaTestHelper.failTestWithException(e, e.getMessage());
        }

        return result;
    }

    /**
     * Returns the unique class matching the specified filters, if one exists.
     * <ul>
     *     <li>Throws {@code NoClassFoundException} if no matching class is found.</li>
     *     <li>Throws {@code MultipleClassesFoundException} if more than one class is found.</li>
     * </ul>
     *
     * @return the unique {@code Class<>} matching the criteria
     */
    public Class<?> findClass() {
        Set<Class<?>> classes = findClasses();
        if (classes.size() == 0) {
            throw new NoClassFoundException(this);
        }
        if (classes.size() > 1) {
            throw new MultipleClassesFoundException(this, classes);
        }

        return classes.iterator().next();
    }

    /**
     * Returns all classes matching the specified filters if any exist, empty {@code Set} otherwise.
     *
     * @return the set of matching classes if any exist, empty set otherwise.
     */
    public Set<Class<?>> findClasses() {
        Reflections reflections = new Reflections(packageName,
            // By setting "excludeObjectClass" to false we are able to find direct subclasses of Object
            new SubTypesScanner(false));
        if (reflections.getStore().keySet().isEmpty()) {
            // The Scanner did not get added to the reflections store as the package name does not exist, return empty.
            // Related to open bug: https://github.com/ronmamo/reflections/issues/273
            return Collections.emptySet();
        }
        return reflections.getSubTypesOf(subTypeOf).stream()
            .filter(clazz -> !packageNameIsExact || packageName.equals(clazz.getPackage().getName()))
            .filter(this::classHasAllSupertypes)
            .filter(this::classNameMatchesConstraints)
            .collect(Collectors.toSet());
    }

    private boolean classHasAllSupertypes(Class<?> clazz) {
        for (Class<?> superType : superTypes) {
            if (!superType.isAssignableFrom(clazz)) {
                return false;
            }
        }
        return true;
    }

    private boolean classNameMatchesConstraints(Class<?> clazz) {
        if (null != exactSimpleName) {
            return clazz.getSimpleName().equals(exactSimpleName);
        }

        for (String classNameContainingSubstring : classNameContainingSubstrings) {
            if (!clazz.getSimpleName().contains(classNameContainingSubstring)) {
                return false;
            }
        }

        return true;
    }

    private void ensureClassNameConstraintsNotAlreadySet() {
        ensureExactClassNameNotAlreadySet();
        if (!classNameContainingSubstrings.isEmpty()) {
            throw new IllegalStateException(
                String.format("Name filter(s) already exist(s): %s", classNameContainingSubstrings.toString())
            );
        }
    }

    private void ensureExactClassNameNotAlreadySet() {
        if (null != exactSimpleName) {
            throw new IllegalStateException(
                String.format("Exact simple name filter already exists: '%s'", exactSimpleName)
            );
        }
    }

    private static void validatePackageName(final String candidatePackageName) {
        if (!VALID_PACKAGE_PATTERN.matcher(candidatePackageName).find()) {
            throw new IllegalArgumentException(
                String.format("Cannot specify a top-level package filter: '%s'", candidatePackageName)
            );
        }
    }

    /* allow exceptions access to the containing package */
    String getPackageName() {
        return packageName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{ClassQuery | ")
            .append(String.format("packageName: %s (include subpackages: %b)", packageName, !packageNameIsExact));

        if (null != exactSimpleName) {
            sb.append(String.format(", searching for exactSimpleName: '%s'", exactSimpleName));
        } else if (null != classNameContainingSubstrings && !classNameContainingSubstrings.isEmpty()) {
            sb.append(String.format(", filtering on classNameContainingSubstrings: [%s]",
                classNameContainingSubstrings.toString()));
        }

        sb.append(String.format(", scanning for subTypes of: '%s'", subTypeOf));

        return sb.append("}").toString();
    }
}
