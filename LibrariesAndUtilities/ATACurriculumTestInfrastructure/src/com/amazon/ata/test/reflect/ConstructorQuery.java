package com.amazon.ata.test.reflect;

import com.amazon.ata.test.helper.AtaTestHelper;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Allows finding a {@code Constructor} using introspection by specifying constraints
 * (currently, just the containing class and the argument types, undorered) being sought.
 * <p>
 * Allows for specifying the containing class (with {@code inClass}), and the
 * argument types (with {@code withExactArgTypes}).
 *
 * Note: Just calling {@code ConstructorQuery.inClass(MyClass.class).findConstructors()} is
 * equivalent to {@code MyClass.class.getConstructors()}, but returns a {@code Set<Constructor>} rather
 * than {@code Constructor[]}.
 *
 * Examples:
 * <ul>
 *     <li><pre>
 * {@code
 *     Class<?> myClass = findMyClassSomehow();
 *     Constructor stringConstructor = ConstructorQuery.inClass(myClass)
 *                                                     .withExactArgs(ImmutableSet.of(String.class)
 *                                                     .findConstructor();
 * }
 *     </pre></li>
 *     <li><pre>
 * {@code
 *      Class<?> myClass = findMyClassSomehow();
 *      Set<Constructor<?>> constructors = ConstructorQuery.inClass(myClass)
 *                                                         .findConstructors();
 * }
 *     </pre></li>
 * </ul>
 * <p>
 * {@code findConstructorOrFail}:<br>
 * If no matching constructor is found, or if more than one constructor is found matching criteria,
 * then will call {@code fail()} with meaningful assertion failure message.
 * </p>
 * <p>
 * {@code findConstructor}:<br>
 * If no matching constructor is found, or if more than one constructor is found matching criteria,
 * then an appropriate {@code ConstructorQueryException} subclass is thrown.
 * </p>
 * <p>
 * {@code findConstructors}:<br>
 * Will return a {@code Set<Constructor<?>>} whether 0, 1 or many {@code Constructor}s are found.
 * </p>
 *
 * @see MethodInvoker for means to invoke constructors with reflection.
 */
public final class ConstructorQuery {
    private final Class<?> clazz;
    private final List<Class<?>> exactArgTypes;

    private ConstructorQuery(final Class<?> clazz, final List<Class<?>> exactArgTypes) {
        this.clazz = clazz;
        this.exactArgTypes = exactArgTypes;
    }

    private ConstructorQuery(final Class<?> clazz) {
        this(clazz, null);
    }

    /**
     * Creates a new {@code ConstructorQuery} by specifying the {@code Class} in which to
     * look for the constructor of interest
     * <p>
     * Providing a {@code null} type results in {@code IllegalArgumentException}.
     * </p>
     * @param clazz The class in which to look for the constructor(s) of interest
     * @return a new {@code ConstructorQuery} to build a query from
     */
    public static ConstructorQuery inClass(final Class clazz) {
        if (null == clazz) {
            throw new IllegalArgumentException("Cannot query constructor on a null type");
        }

        return new ConstructorQuery(clazz);
    }

    /**
     * Specifies filter for only no-arg constructor.
     * @return an updated {@code ConstructorQuery} with new filter applied.
     */
    public ConstructorQuery withNoArgs() {
        return withExactArgTypes(Collections.emptyList());
    }

    /**
     * Specifies filter with exact argument types, which can be repeated (hint: but don't try to
     * use a {@code Set} for that). The order of the arguments does NOT matter.
     * <p>
     *     Providing {@code null} {@code argTypes} or including a {@code null} in the {@code argTypes}
     *     collection will throw {@code IllegalArgumentException}.
     * </p>
     * @param argTypes The argument types to find constructor for
     * @return an updated {@code ConstructorQuery} with new filter applied.
     */
    public ConstructorQuery withExactArgTypes(final Collection<Class<?>> argTypes) {
        if (null != exactArgTypes) {
            throw new IllegalStateException("Exact arg types filter already exists: " + exactArgTypes);
        }
        if (null == argTypes) {
            throw new IllegalArgumentException("argTypes cannot be null");
        }
        if (argTypes.size() > MethodQuery.MAX_ARGUMENTS) {
            throw new IllegalArgumentException(
                String.format(
                    "withExactArgTypes will not accept more than %d arguments: %s",
                    MethodQuery.MAX_ARGUMENTS,
                    argTypes)
            );
        }
        for (Class<?> argType : argTypes) {
            if (null == argType) {
                throw new IllegalArgumentException("argTypes contained a null: " + argTypes);
            }
        }

        return new ConstructorQuery(this.clazz, new ArrayList<>(argTypes));
    }

    /**
     * Returns the unique constructor matching the specified filters, if one exists.
     *
     * <p>
     * If no constructor is found, or if too many constructors found, will {@code fail()}
     * (JUnit) with meaningful message.
     * </p>
     *
     * @return the unique {@code Constructor} matching the criteria
     */
    public Constructor findConstructorOrFail() {
        Constructor<?> constructor = null;

        try {
            constructor = findConstructor();
        } catch (ConstructorQueryException e) {
            AtaTestHelper.failTestWithException(e, e.getMessage());
        }

        return constructor;
    }

    /**
     * Returns the unique constructor matching the specified filters, if one exists.
     *
     * <ul>
     * <li>Throws {@code NoConstructorFoundException} if no matching constructor is found.</li>
     * <li>Throws {@code MultipleConstructorsFoundException} if more than one matching constructor is found.</li>
     * </ul>
     *
     * @return the unique {@code Constructor} matching the criteria
     */
    public Constructor<?> findConstructor() {
        Set<Constructor<?>> constructors = findConstructors();

        if (constructors.isEmpty()) {
            throw new NoConstructorFoundException(this);
        }
        if (constructors.size() > 1) {
            throw new MultipleConstructorsFoundException(this, constructors);
        }

        return constructors.iterator().next();
    }

    /**
     * Returns all constructors matching the specified filters if any exist, empty {@code Set} otherwise.
     *
     * @return the set of matching constructors if any exist, empty set otherwise
     */
    public Set<Constructor<?>> findConstructors() {
        Set<Constructor<?>> constructors;

        if (null == exactArgTypes) {
            constructors = Sets.newHashSet(clazz.getConstructors());
        } else if (exactArgTypes.isEmpty()) {
            try {
                constructors = ImmutableSet.of(clazz.getConstructor());
            } catch (NoSuchMethodException e) {
                constructors = Collections.emptySet();
            }
        } else {
            constructors = new HashSet<>();
            for (List<Class<?>> argTypePermutation : Collections2.permutations(exactArgTypes)) {
                try {
                    Constructor<?> constructor = clazz.getConstructor(argTypePermutation.toArray(new Class[0]));
                    constructors.add(constructor);
                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
        }
        return constructors;
    }

    Class<?> getContainingClass() {
        return clazz;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{ConstructorQuery | class: ").append(clazz);
        if (null != exactArgTypes) {
            sb.append(", exactArgTypes: ").append(exactArgTypes);
        }

        return sb.append("}").toString();
    }
}
