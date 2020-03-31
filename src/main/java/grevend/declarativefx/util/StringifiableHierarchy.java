package grevend.declarativefx.util;

import org.jetbrains.annotations.NotNull;

public interface StringifiableHierarchy {

    default @NotNull String stringifyHierarchy() {
        return stringifyHierarchy(Verbosity.NORMAL);
    }

    default @NotNull String stringifyHierarchy(@NotNull Verbosity verbosity) {
        var builder = new StringBuilder();
        this.stringifyHierarchy(builder, "", "", verbosity);
        return builder.toString();
    }

    void stringifyHierarchy(@NotNull StringBuilder builder, @NotNull String prefix, @NotNull String childPrefix,
                            @NotNull Verbosity verbosity);

    enum Verbosity {
        SIMPLIFIED, NORMAL, DETAILED;
    }


}
