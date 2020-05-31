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

package grevend.declarativefx.test.verifier;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author David Greven
 * @since 0.6.7
 */
public class Verifier {

    final Predicate<Object> predicate;
    final Class<?> clazz;

    @Contract(pure = true)
    private Verifier(@NotNull Predicate<Object> predicate, @NotNull Class<?> clazz) {
        this.predicate = predicate;
        this.clazz = clazz;
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier some() {
        return verifier(Objects::nonNull);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier none() {
        return verifier(Objects::isNull);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier any() {
        return verifier(val -> true);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier type(@NotNull Class<?> type) {
        return verifier(type::isInstance);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier bool() {
        return verifier(val -> Objects.equals(val, true) || Objects.equals(val, false));
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier numeric() {
        return verifier(Number.class::isInstance);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier string() {
        return verifier(String.class::isInstance);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier collection() {
        return verifier(Collection.class::isInstance);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier array() {
        return verifier(val -> val.getClass().isArray());
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier enumeration() {
        return verifier(val -> val.getClass().isEnum());
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier iterable() {
        return verifier(Iterable.class::isInstance);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier annotation() {
        return verifier(val -> val.getClass().isAnnotation());
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(byte min) {
        return verifier(val -> val >= min, Byte.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(byte max) {
        return verifier(val -> val <= max, Byte.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(byte min, byte max) {
        return verifier(val -> val >= min && val <= max, Byte.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(short min) {
        return verifier(val -> val >= min, Short.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(short max) {
        return verifier(val -> val <= max, Short.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(short min, short max) {
        return verifier(val -> val >= min && val <= max, Short.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(int min) {
        return verifier(val -> val >= min, Integer.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(int max) {
        return verifier(val -> val <= max, Integer.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(int min, int max) {
        return verifier(val -> val >= min && val <= max, Integer.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(long min) {
        return verifier(val -> val >= min, Long.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(long max) {
        return verifier(val -> val <= max, Long.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(long min, long max) {
        return verifier(val -> val >= min && val <= max, Long.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(float min) {
        return verifier(val -> val >= min, Float.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(float max) {
        return verifier(val -> val <= max, Float.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(float min, float max) {
        return verifier(val -> val >= min && val <= max, Float.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(double min) {
        return verifier(val -> val >= min, Double.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(double max) {
        return verifier(val -> val <= max, Double.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(double min, double max) {
        return verifier(val -> val >= min && val <= max, Double.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(@NotNull BigInteger min) {
        return verifier(val -> val.compareTo(min) >= 0, BigInteger.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(@NotNull BigInteger max) {
        return verifier(val -> val.compareTo(max) <= 0, BigInteger.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(@NotNull BigInteger min, @NotNull BigInteger max) {
        return verifier(val -> val.compareTo(min) >= 0 && val.compareTo(max) <= 0, BigInteger.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(@NotNull BigDecimal min) {
        return verifier(val -> val.compareTo(min) >= 0, BigDecimal.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(@NotNull BigDecimal max) {
        return verifier(val -> val.compareTo(max) <= 0, BigDecimal.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(@NotNull BigDecimal min, @NotNull BigDecimal max) {
        return verifier(val -> val.compareTo(min) >= 0 && val.compareTo(max) <= 0, BigDecimal.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier before(@NotNull Date date) {
        return verifier(val -> val.before(date), Date.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier after(@NotNull Date date) {
        return verifier(val -> val.after(date), Date.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier before(@NotNull LocalDate date) {
        return verifier(val -> val.isBefore(date), LocalDate.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier after(@NotNull LocalDate date) {
        return verifier(val -> val.isAfter(date), LocalDate.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier leapYear() {
        return verifier(LocalDate::isLeapYear, LocalDate.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier before(@NotNull LocalTime time) {
        return verifier(val -> val.isBefore(time), LocalTime.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier after(@NotNull LocalTime time) {
        return verifier(val -> val.isAfter(time), LocalTime.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier before(@NotNull LocalDateTime dateTime) {
        return verifier(val -> val.isBefore(dateTime), LocalDateTime.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier after(@NotNull LocalDateTime dateTime) {
        return verifier(val -> val.isAfter(dateTime), LocalDateTime.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier before(@NotNull ZonedDateTime zonedDateTime) {
        return verifier(val -> val.isBefore(zonedDateTime), ZonedDateTime.class);
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier after(@NotNull ZonedDateTime zonedDateTime) {
        return verifier(val -> val.isAfter(zonedDateTime), ZonedDateTime.class);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    @Contract(value = "_, _ -> new", pure = true)
    private static <T> Verifier verifier(@NotNull Predicate<T> predicate, @NotNull Class<T> clazz) {
        return new Verifier((Predicate<Object>) predicate, clazz);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    private static Verifier verifier(@NotNull Predicate<Object> predicate) {
        return new Verifier(predicate, Object.class);
    }

    public boolean verify(final Object val) {
        return this.clazz.isInstance(val) && this.predicate.test(val);
    }

}
