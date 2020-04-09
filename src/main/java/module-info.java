module DeclarativeFX {
    requires java.logging;

    requires javafx.graphics;
    requires javafx.controls;

    requires org.jetbrains.annotations;

    exports grevend.declarativefx;
    exports grevend.declarativefx.bindable;
    exports grevend.declarativefx.components;
    exports grevend.declarativefx.components.builder;
    exports grevend.declarativefx.event;
    exports grevend.declarativefx.lifecycle;
    exports grevend.declarativefx.util;

    exports grevend.declarativefx.example to javafx.graphics;
}