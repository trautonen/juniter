package org.eluder.juniter.core.util;

/*
 * #[license]
 * juniter-core
 * %%
 * Copyright (C) 2012 Tapio Rautonen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * %[license]
 */

import java.util.HashSet;
import java.util.Set;

/**
 * Identity registry holds identities of pairs. The registry can be used to detect circular
 * references in reflection assert and avoid eternal loops.
 */
public final class IdentityRegistry {

    private final Set<Pair<Identity>> registry = new HashSet<Pair<Identity>>(32);

    public boolean isRegisteredIdentity(final Object o1, final Object o2) {
        Pair<Identity> identity = createIdentity(o1, o2);
        Pair<Identity> switchedIdentity = createSwitchedIdentity(identity);
        return (registry.contains(identity) || registry.contains(switchedIdentity));
    }

    public void registerIdentity(final Object o1, final Object o2) {
        Pair<Identity> identity = createIdentity(o1, o2);
        Pair<Identity> switchedIdentity = createSwitchedIdentity(identity);
        registry.add(identity);
        registry.add(switchedIdentity);
    }

    public void unregisterIdentity(final Object o1, final Object o2) {
        Pair<Identity> identity = createIdentity(o1, o2);
        Pair<Identity> switchedIdentity = createSwitchedIdentity(identity);
        registry.remove(identity);
        registry.remove(switchedIdentity);
    }

    private Pair<Identity> createIdentity(final Object o1, final Object o2) {
        return new Pair<Identity>(new Identity(o1), new Identity(o2));
    }

    private Pair<Identity> createSwitchedIdentity(final Pair<Identity> original) {
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
