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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author David Greven
 * @since 0.6.7
 */
public final class Verifier {

    private final Predicate<Object> predicate;
    private final Class<?> clazz;
    private final String representation;

    @Contract(pure = true)
    private Verifier(@NotNull Predicate<Object> predicate, @NotNull Class<?> clazz, @NotNull String representation) {
        this.predicate = predicate;
        this.clazz = clazz;
        this.representation = representation;
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier some() {
        return verifier(Objects::nonNull, "some[value != null]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier none() {
        return verifier(Objects::isNull, "none[value == null]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier any() {
        return verifier(val -> true, "any[value]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier type(@NotNull Class<?> type) {
        return verifier(type::isInstance, "type[value instanceof type]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier bool() {
        return verifier(val -> Objects.equals(val, true) || Objects.equals(val, false), "bool[true || false]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier numeric() {
        return verifier(Number.class::isInstance, "numeric[value instanceof Number]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier string() {
        return verifier(String.class::isInstance, "string[value instanceof String]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier collection() {
        return verifier(Collection.class::isInstance, "collection[value instanceof Collection]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier array() {
        return verifier(val -> val.getClass().isArray(), "array[value is array]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier enumeration() {
        return verifier(val -> val.getClass().isEnum(), "array[value is enumeration]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier iterable() {
        return verifier(Iterable.class::isInstance, "iterable[value instanceof Iterable]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier annotation() {
        return verifier(val -> val.getClass().isAnnotation(), "annotation[value is annotation");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(byte min) {
        return verifier(val -> val >= min, Byte.class, "min[byte value >= byte min]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(byte max) {
        return verifier(val -> val <= max, Byte.class, "max[byte value <= byte max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(byte min, byte max) {
        return verifier(val -> val >= min && val <= max, Byte.class,
            "range[byte value >= byte min && byte value <= byte max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(short min) {
        return verifier(val -> val >= min, Short.class, "min[short value >= short min]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(short max) {
        return verifier(val -> val <= max, Short.class, "max[short value <= short max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(short min, short max) {
        return verifier(val -> val >= min && val <= max, Short.class,
            "range[short value >= short min && short value <= short max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(int min) {
        return verifier(val -> val >= min, Integer.class, "min[int value >= int min]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(int max) {
        return verifier(val -> val <= max, Integer.class, "max[int value <= int max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(int min, int max) {
        return verifier(val -> val >= min && val <= max, Integer.class,
            "range[int value >= int min && int value <= int max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(long min) {
        return verifier(val -> val >= min, Long.class, "min[long value >= long min]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(long max) {
        return verifier(val -> val <= max, Long.class, "max[long value <= long max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(long min, long max) {
        return verifier(val -> val >= min && val <= max, Long.class,
            "range[long value >= long min && long value <= long max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(float min) {
        return verifier(val -> val >= min, Float.class, "min[float value >= float min]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(float max) {
        return verifier(val -> val <= max, Float.class, "max[float value <= float max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(float min, float max) {
        return verifier(val -> val >= min && val <= max, Float.class,
            "range[float value >= float min && float value <= float max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(double min) {
        return verifier(val -> val >= min, Double.class, "min[double value >= double min]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(double max) {
        return verifier(val -> val <= max, Double.class, "max[double value <= double max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(double min, double max) {
        return verifier(val -> val >= min && val <= max, Double.class,
            "range[double value >= double min && double value <= double max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(@NotNull BigInteger min) {
        return verifier(val -> val.compareTo(min) >= 0, BigInteger.class, "min[BigInteger value >= BigInteger min]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(@NotNull BigInteger max) {
        return verifier(val -> val.compareTo(max) <= 0, BigInteger.class, "max[BigInteger value <= BigInteger max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(@NotNull BigInteger min, @NotNull BigInteger max) {
        return verifier(val -> val.compareTo(min) >= 0 && val.compareTo(max) <= 0, BigInteger.class,
            "range[BigInteger value >= BigInteger min && BigInteger value <= BigInteger max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier min(@NotNull BigDecimal min) {
        return verifier(val -> val.compareTo(min) >= 0, BigDecimal.class, "min[BigDecimal value >= BigDecimal min]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier max(@NotNull BigDecimal max) {
        return verifier(val -> val.compareTo(max) <= 0, BigDecimal.class, "max[BigDecimal value <= BigDecimal max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier range(@NotNull BigDecimal min, @NotNull BigDecimal max) {
        return verifier(val -> val.compareTo(min) >= 0 && val.compareTo(max) <= 0, BigDecimal.class,
            "range[BigDecimal value >= BigDecimal min && BigDecimal value <= BigDecimal max]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier before(@NotNull Date date) {
        return verifier(val -> val.before(date), Date.class, "before[Date value before Date date]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier after(@NotNull Date date) {
        return verifier(val -> val.after(date), Date.class, "after[Date value after Date date]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier before(@NotNull LocalDate date) {
        return verifier(val -> val.isBefore(date), LocalDate.class, "before[LocalDate value before LocalDate date]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier after(@NotNull LocalDate date) {
        return verifier(val -> val.isAfter(date), LocalDate.class, "after[LocalDate value after LocalDate date]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier leapYear() {
        return verifier(LocalDate::isLeapYear, LocalDate.class, "leapYear[LocalDate value is leap year]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier before(@NotNull LocalTime time) {
        return verifier(val -> val.isBefore(time), LocalTime.class, "before[LocalTime value before LocalTime time]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier after(@NotNull LocalTime time) {
        return verifier(val -> val.isAfter(time), LocalTime.class, "after[LocalTime value after LocalTime time]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier before(@NotNull LocalDateTime dateTime) {
        return verifier(val -> val.isBefore(dateTime), LocalDateTime.class,
            "before[LocalDateTime value before LocalDateTime dateTime]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier after(@NotNull LocalDateTime dateTime) {
        return verifier(val -> val.isAfter(dateTime), LocalDateTime.class,
            "after[LocalDateTime value after LocalDateTime dateTime]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier before(@NotNull ZonedDateTime zonedDateTime) {
        return verifier(val -> val.isBefore(zonedDateTime), ZonedDateTime.class,
            "before[ZonedDateTime value before ZonedDateTime zonedDateTime]");
    }

    @NotNull
    @Contract(pure = true)
    public static Verifier after(@NotNull ZonedDateTime zonedDateTime) {
        return verifier(val -> val.isAfter(zonedDateTime), ZonedDateTime.class,
            "after[ZonedDateTime value after ZonedDateTime zonedDateTime]");
    }

    @NotNull
    @SuppressWarnings("unchecked")
    @Contract(value = "_, _, _ -> new", pure = true)
    private static <T> Verifier verifier(@NotNull Predicate<T> predicate, @NotNull Class<T> clazz, @NotNull String representation) {
        return new Verifier((Predicate<Object>) predicate, clazz, representation);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    private static Verifier verifier(@NotNull Predicate<Object> predicate, @NotNull String representation) {
        return new Verifier(predicate, Object.class, representation);
    }

    public boolean verify(@Nullable final Object val) {
        return this.clazz.isInstance(val) && this.predicate.test(val);
    }

    @NotNull
    @Override
    @Contract(pure = true)
    public String toString() {
        return this.representation;
    }

}
