package grevend.declarativefx.util;

import javafx.beans.Observable;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    public static @NotNull <N extends Node> Map<String, String> getPropertyNames(@NotNull Class<N> nodeClass) {
        return Arrays.stream(nodeClass.getMethods()).filter(
            m -> m.getName().contains("Property") && Observable.class.isAssignableFrom(m.getReturnType()) &&
                Modifier.isPublic(m.getModifiers()))
            .collect(Collectors.toMap(m -> m.getName().replaceAll("Property", "").toLowerCase(), Method::getName));
    }

}
