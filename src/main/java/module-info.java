module DeclarativeFX {
    requires java.logging;

    requires javafx.graphics;
    requires javafx.controls;

    requires org.jetbrains.annotations;

    exports grevend.declarativefx;
    exports grevend.declarativefx.bindable;
    exports grevend.declarativefx.component;
    exports grevend.declarativefx.decorator;
    exports grevend.declarativefx.event;
    exports grevend.declarativefx.iterator;
    exports grevend.declarativefx.util;
    exports grevend.declarativefx.util.logging;
    exports grevend.declarativefx.view;
    exports grevend.declarativefx.visitor;

    exports grevend.declarativefx.example to javafx.graphics;

    //provides System.LoggerFinder with DeclarativeFXLoggerFinder;
}