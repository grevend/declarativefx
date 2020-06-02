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

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * @author David Greven
 * @since 0.7.0
 */
public final class TransitionVerifier {

    private final BiPredicate<Object, Object> predicate;
    private final String representation;

    @Contract(pure = true)
    private TransitionVerifier(@NotNull BiPredicate<Object, Object> predicate, @NotNull String representation) {
        this.predicate = predicate;
        this.representation = representation;
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static TransitionVerifier transition(@Nullable Object from, @Nullable Object to) {
        return new TransitionVerifier((a, b) -> Objects.equals(a, from) && Objects.equals(b, to),
            "transition[from = " + from + " -> to = " + to + "]");
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static TransitionVerifier transition(@NotNull Verifier from, @Nullable Object to) {
        return new TransitionVerifier((a, b) -> from.verify(a) && Objects.equals(b, to),
            "transition[" + from + " from -> to = " + to + "]");
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static TransitionVerifier transition(@Nullable Object from, @NotNull Verifier to) {
        return new TransitionVerifier((a, b) -> Objects.equals(a, from) && to.verify(b),
            "transition[from " + from + " -> " + to + " to]");
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static TransitionVerifier transition(@NotNull Verifier from, @NotNull Verifier to) {
        return new TransitionVerifier((a, b) -> from.verify(a) && to.verify(b),
            "transition[" + from + " from -> " + to + " to]");
    }

    public boolean verify(@Nullable final Object from, @Nullable final Object to) {
        return this.predicate.test(from, to);
    }

    @NotNull
    @Override
    @Contract(pure = true)
    public String toString() {
        return this.representation;
    }

}
