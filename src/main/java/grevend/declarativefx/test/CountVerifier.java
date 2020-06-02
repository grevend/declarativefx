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

import java.util.function.IntPredicate;

/**
 * @author David Greven
 * @since 0.7.0
 */
public final class CountVerifier {

    private final IntPredicate predicate;
    private final String representation;

    @Contract(pure = true)
    private CountVerifier(@NotNull IntPredicate predicate, @NotNull String representation) {
        this.predicate = predicate;
        this.representation = representation;
    }

    @NotNull
    @Contract(pure = true)
    public static CountVerifier any() {
        return verifier(val -> val > 0, "any[count > 0]");
    }

    @NotNull
    @Contract(pure = true)
    public static CountVerifier never() {
        return verifier(val -> val <= 0, "never[count <= 0]");
    }

    @NotNull
    @Contract(pure = true)
    public static CountVerifier times(int times) {
        return verifier(val -> val == times, "times[count == times]");
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    private static CountVerifier verifier(@NotNull IntPredicate predicate, @NotNull String representation) {
        return new CountVerifier(predicate, representation);
    }

    public boolean verify(final int count) {
        return this.predicate.test(count);
    }

    @NotNull
    @Override
    @Contract(pure = true)
    public String toString() {
        return this.representation;
    }

}
