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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author David Greven
 * @since 0.7.0
 */
public class TransitionBuilder {

    private final BindingAssertion bindingAssertion;
    private Object previous;

    @Contract(pure = true)
    protected TransitionBuilder(@Nullable Object from, @NotNull BindingAssertion bindingAssertion) {
        this.previous = from;
        this.bindingAssertion = bindingAssertion;
    }

    @Contract(pure = true)
    protected TransitionBuilder(@NotNull Verifier verifier, @NotNull BindingAssertion bindingAssertion) {
        this.previous = verifier;
        this.bindingAssertion = bindingAssertion;
    }

    @NotNull
    public TransitionBuilder to(@Nullable Object to) {
        if (this.previous instanceof Verifier && to instanceof Verifier) {
            this.bindingAssertion.change((Verifier) this.previous, (Verifier) to);
        } else if (this.previous instanceof Verifier) {
            this.bindingAssertion.change((Verifier) this.previous, to);
        } else if (to instanceof Verifier) {
            this.bindingAssertion.change(this.previous, (Verifier) to);
        } else {
            this.bindingAssertion.change(this.previous, to);
        }
        this.previous = to;
        return this;
    }

    @NotNull
    public TransitionBuilder to(@NotNull Verifier verifier) {
        return this.to((Object) verifier);
    }

    public void verify() {
        this.bindingAssertion.verify();
    }

}
