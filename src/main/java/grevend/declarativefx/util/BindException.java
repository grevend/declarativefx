package grevend.declarativefx.util;

import org.jetbrains.annotations.NotNull;

public class BindException extends RuntimeException {

    public BindException(@NotNull String msg) {
        super(msg);
    }

}
