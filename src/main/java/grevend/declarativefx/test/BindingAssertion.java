/*
 * MIT License
 *
 * Copyright (c) 2020 David Greven
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package grevend.declarativefx.test;

import grevend.declarativefx.bindable.Bindable;
import grevend.declarativefx.util.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author David Greven
 * @since 0.7.0
 */
public final class BindingAssertion {

    private final Bindable bindable;
    private final Collection<TransitionVerifier> transitions;
    private final Collection<Pair<Object, Object>> values;
    private CountVerifier counter = CountVerifier.any();
    private Object previous;
    private int count = 0;
    private boolean first = true;

    @Contract(pure = true)
    public BindingAssertion(@NotNull Bindable bindable) {
        this.bindable = bindable;
        this.transitions = new ArrayList<>();
        this.values = new ArrayList<>();
        this.bindable.subscribe(val -> {
            if (this.first) {
                this.first = false;
            } else {
                values.add(new Pair<>(previous, val));
                count++;
            }
            previous = val;
        });
    }

    @NotNull
    @Contract("_ -> this")
    public BindingAssertion changes(CountVerifier verifier) {
        this.counter = verifier;
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public BindingAssertion changes(int times) {
        this.counter = CountVerifier.times(times);
        return this;
    }

    @NotNull
    @Contract("_, _ -> this")
    public BindingAssertion change(@Nullable Object from, @Nullable Object to) {
        this.transitions.add(TransitionVerifier.transition(from, to));
        return this;
    }

    @NotNull
    @Contract("_, _ -> this")
    public BindingAssertion change(@NotNull Verifier from, @Nullable Object to) {
        this.transitions.add(TransitionVerifier.transition(from, to));
        return this;
    }

    @NotNull
    @Contract("_, _ -> this")
    public BindingAssertion change(@Nullable Object from, @NotNull Verifier to) {
        this.transitions.add(TransitionVerifier.transition(from, to));
        return this;
    }

    @NotNull
    @Contract("_, _ -> this")
    public BindingAssertion change(@NotNull Verifier from, @NotNull Verifier to) {
        this.transitions.add(TransitionVerifier.transition(from, to));
        return this;
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public TransitionBuilder from(@Nullable Object from) {
        return new TransitionBuilder(from, this);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public TransitionBuilder from(@NotNull Verifier verifier) {
        return new TransitionBuilder(verifier, this);
    }

    public void verify() {
        if (!this.counter.verify(this.count)) {
            throw new AssertionException(
                "Times of change " + this.count + " failed verification <" + this.counter + ">.");
        } else if (this.transitions.size() > this.values.size()) {
            throw new AssertionException("Amount of required transitions does not match given values.");
        }
        var verifierIter = this.transitions.iterator();
        var valuesIter = this.values.iterator();
        while (verifierIter.hasNext() && valuesIter.hasNext()) {
            var transitionValue = valuesIter.next();
            var verifier = verifierIter.next();
            if (!verifier.verify(transitionValue.getA(), transitionValue.getB())) {
                throw new AssertionException("Value of property transition '" + transitionValue.getA() +
                    "' -> '" + transitionValue.getB() + "' failed verification <" + verifier + ">.");
            }
        }
    }

}
