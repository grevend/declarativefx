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

package grevend.declarativefx.properties;

import grevend.declarativefx.Component;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Findable {

    @Nullable Component<? extends Node> findById(@NotNull String id, boolean root);

    default @Nullable Component<? extends Node> findById(@NotNull String id) {
        return this.findById(id, true);
    }

    @NotNull Collection<Component<? extends Node>> findByClass(
        @NotNull Collection<Component<? extends Node>> components, @NotNull String clazz, boolean root);

    default @NotNull Collection<Component<? extends Node>> findByClass(@NotNull String clazz, boolean root) {
        return this.findByClass(new ArrayList<>(), clazz, root);
    }

    default @NotNull Collection<Component<? extends Node>> findByClass(@NotNull String clazz) {
        return this.findByClass(new ArrayList<>(), clazz, true);
    }

    default @Nullable Component<? extends Node> find(@NotNull String identifier, boolean root) {
        if (identifier.startsWith("#")) {
            return this.findById(identifier.replace("#", ""), root);
        } else if (identifier.startsWith(".")) {
            var components = this.findByClass(identifier.replace(".", ""), root);
            return components.isEmpty() ? null : components.iterator().next();
        } else {
            return null;
        }
    }

    default @Nullable Component<? extends Node> find(@NotNull String id) {
        return this.find(id, true);
    }

    default @Nullable Collection<Component<? extends Node>> findAll(@NotNull String... identifiers) {
        return Arrays.stream(identifiers).flatMap(identifier -> {
            if (identifier.startsWith("#")) {
                return Stream.ofNullable(this.findById(identifier.replace("#", ""), true));
            } else if (identifier.startsWith(".")) {
                return this.findByClass(identifier.replace(".", ""), true).stream();
            } else {
                return Stream.empty();
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    default @Nullable Collection<Component<? extends Node>> findAll(@NotNull Iterable<String> identifiers) {
        return StreamSupport.stream(identifiers.spliterator(), false).flatMap(identifier -> {
            if (identifier.startsWith("#")) {
                return Stream.ofNullable(this.findById(identifier.replace("#", ""), true));
            } else if (identifier.startsWith(".")) {
                return this.findByClass(identifier.replace(".", ""), true).stream();
            } else {
                return Stream.empty();
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
