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

import grevend.declarativefx.component.Component;
import javafx.scene.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @param <N>
 * @param <C>
 * @param <F>
 *
 * @since 0.6.1
 */
public final class PropertyAssertion<N extends Node, C extends Component<N>, F extends ComponentFixture<N, C>> {

    private final F fixture;
    private final String property;

    /**
     * @param fixture
     * @param property
     *
     * @since 0.6.1
     */
    @Contract(pure = true)
    public PropertyAssertion(@NotNull F fixture, @NotNull String property) {
        this.fixture = fixture;
        this.property = property;
    }

    /**
     * @return
     *
     * @since 0.6.1
     */
    @Contract(pure = true)
    public String property() {
        return this.property;
    }

    /**
     * @return
     *
     * @since 0.6.8
     */
    @Nullable
    public Object value() {
        return this.fixture.component().get(this.property);
    }

    /**
     * @param val
     *
     * @since 0.6.8
     */
    public void matches(@Nullable Object val) {
        if (val instanceof Verifier) {
            throw new IllegalArgumentException("Value of property '" + this.property +
                "' cannot be matched with Verifier <" + val + ">. Please use the verify method instead.");
        }
        if (!Objects.equals(this.value(), val)) {
            throw new AssertionException("'" + this.value() + "' does not match " + val);
        }
    }

    /**
     * @param verifier
     *
     * @since 0.6.8
     */
    public void verify(@NotNull Verifier verifier) {
        if (!verifier.verify(this.value())) {
            throw new AssertionException("'" + this.value() + "' failed verification <" + verifier + ">.");
        }
    }

}
