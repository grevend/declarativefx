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

    public static @NotNull <N extends Node> Map<String, String> getPropertyNames(@NotNull Class<N> nodeClass) {
        return Arrays.stream(nodeClass.getMethods()).filter(
            m -> m.getName().contains("Property") && Observable.class.isAssignableFrom(m.getReturnType()) &&
                Modifier.isPublic(m.getModifiers()))
            .collect(Collectors.toMap(m -> m.getName().replaceAll("Property", "").toLowerCase(), Method::getName));
    }

    public static synchronized @Nullable <N extends Node> ObservableValue<?> getObservableValue(@NotNull N node,
                                                                                                @NotNull Map<String, ObservableValue<?>> properties,
                                                                                                @NotNull String property) {
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
                        (ObservableValue<?>) nodeClass
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
