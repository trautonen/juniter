package org.eluder.juniter.core.util;

import java.util.HashSet;
import java.util.Set;

public class IdentityRegistry {

    private static final ThreadLocal<Set<Pair<Identity>>> REGISTRY = new ThreadLocal<Set<Pair<Identity>>>() {
        @Override
        protected Set<Pair<Identity>> initialValue() {
            return new HashSet<Pair<Identity>>();
        }
    };

    public static boolean isRegisteredIdentity(final Object o1, final Object o2) {
        Pair<Identity> identity = createIdentity(o1, o2);
        Pair<Identity> switchedIdentity = createSwitchedIdentity(identity);
        Set<Pair<Identity>> registry = REGISTRY.get();
        return (registry.contains(identity) || registry.contains(switchedIdentity));
    }

    public static void registerIdentity(final Object o1, final Object o2) {
        Pair<Identity> identity = createIdentity(o1, o2);
        Pair<Identity> switchedIdentity = createSwitchedIdentity(identity);
        Set<Pair<Identity>> registry = REGISTRY.get();
        registry.add(identity);
        registry.add(switchedIdentity);
    }

    public static void removeIdentity(final Object o1, final Object o2) {
        Pair<Identity> identity = createIdentity(o1, o2);
        Pair<Identity> switchedIdentity = createSwitchedIdentity(identity);
        Set<Pair<Identity>> registry = REGISTRY.get();
        registry.remove(identity);
        registry.remove(switchedIdentity);
    }

    private static Pair<Identity> createIdentity(final Object o1, final Object o2) {
        return new Pair<Identity>(new Identity(o1), new Identity(o2));
    }

    private static Pair<Identity> createSwitchedIdentity(final Pair<Identity> original) {
        return new Pair<Identity>(original.getRight(), original.getLeft());
    }

    private static class Pair<T> {
        private final T left;
        private final T right;

        public Pair(final T left, final T right) {
            if (left == null || right == null) {
                throw new IllegalArgumentException("Pair does not allow null values, either left or right value is null");
            }
            this.left = left;
            this.right = right;
        }

        public T getLeft() {
            return left;
        }

        public T getRight() {
            return right;
        }

        @Override
        public int hashCode() {
            final int prime = 17;
            int result = 1;
            result = prime * result + left.hashCode();
            result = prime * result + right.hashCode();
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Pair)) {
                return false;
            }
            @SuppressWarnings("unchecked")
            Pair<T> other = (Pair<T>) obj;
            if (!this.left.equals(other.left)) {
                return false;
            }
            if (!this.right.equals(other.right)) {
                return false;
            }

            return true;
        }
    }

    private static class Identity {
        private final int identity;
        private final Object value;

        public Identity(final Object value) {
            this.identity = System.identityHashCode(value);
            this.value = value;
        }

        @Override
        public int hashCode() {
            return identity;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Identity)) {
                return false;
            }
            Identity other = (Identity) obj;
            if (this.identity != other.identity) {
                return false;
            }
            return (this.value == other.value);
        }
    }
}
