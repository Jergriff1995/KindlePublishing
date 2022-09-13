package com.amazon.ata.test.junit;

import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.TestTag;
import org.junit.platform.engine.UniqueId;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of TestDescriptor so that we can unit test classes that use TestIdentifier. TestIdentifier is final
 * and cannot be mocked. A TestIdentifier can be created from a TestDescriptor, via TestIdentifier.from.
 */
public class FakeTestDescriptor implements TestDescriptor {
    private String id;
    private Type type;

    public FakeTestDescriptor(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public UniqueId getUniqueId() {
        return UniqueId.parse(String.format("[test:%s]", id));
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getDisplayName() {
        return id;
    }

    @Override
    public Set<TestTag> getTags() {
        return new HashSet<>();
    }

    @Override
    public Optional<TestSource> getSource() {
        return Optional.empty();
    }

    @Override
    public Optional<TestDescriptor> getParent() {
        return Optional.empty();
    }

    @Override
    public void setParent(TestDescriptor testDescriptor) {

    }

    @Override
    public Set<? extends TestDescriptor> getChildren() {
        return new HashSet<>();
    }

    @Override
    public void addChild(TestDescriptor testDescriptor) {

    }

    @Override
    public void removeChild(TestDescriptor testDescriptor) {

    }

    @Override
    public void removeFromHierarchy() {

    }

    @Override
    public Optional<? extends TestDescriptor> findByUniqueId(UniqueId uniqueId) {
        return Optional.empty();
    }
}
