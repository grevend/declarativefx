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

package grevend.declarativefx.util;

import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    public final static Map<Class<? extends Node>, Map<String, String>> propertyNames =
        new HashMap<>();

    @NotNull
    public static <N extends Node> Map<String, String> getPropertyNames(@NotNull Class<N> nodeClass) {
        return Arrays.stream(nodeClass.getMethods()).filter(
            m -> m.getName().contains("Property") && Observable.class.isAssignableFrom(m.getReturnType()) &&
                Modifier.isPublic(m.getModifiers()))
            .collect(Collectors.toMap(m -> m.getName().replaceAll("Property", "").toLowerCase(), Method::getName));
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static synchronized <N extends Node> ObservableValue<?> getObservableValue(@NotNull N node, @NotNull Map<String, ObservableValue<Object>> properties, @NotNull String property) {
        var nodeClass = node.getClass();
        if (properties.containsKey(property.toLowerCase())) {
            return properties.get(property.toLowerCase());
        } else {
            if (!propertyNames.containsKey(nodeClass)) {
                propertyNames.put(nodeClass, Utils.getPropertyNames(nodeClass));
            }
            if (propertyNames.get(nodeClass).containsKey(property.toLowerCase())) {
                try {
                    properties.put(property.toLowerCase(),
                        (ObservableValue<Object>) nodeClass
                            .getMethod(propertyNames.get(nodeClass).get(property.toLowerCase())).invoke(node));
                    return properties.get(property.toLowerCase());
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

}
