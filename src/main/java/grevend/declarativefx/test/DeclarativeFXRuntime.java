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

import grevend.declarativefx.component.Component;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;

/**
 * @author David Greven
 * @since 0.6.1
 */
public final class DeclarativeFXRuntime extends Application {

    protected static boolean running = false;

    private Stage stage;

    /**
     * Starts the runtime that will be used for testing.
     *
     * @since 0.6.1
     */
    @SuppressWarnings("unused")
    public static synchronized void launch() {
        Executors.newSingleThreadExecutor().execute(() -> launch(new String[0]));
    }

    /**
     * Stops the JavaFX application.
     *
     * @since 0.6.1
     */
    public static synchronized void exit() {
        Platform.exit();
    }

    /**
     * {@inheritDoc}
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will not be
     *              primary stages.
     *
     * @since 0.6.1
     */
    @Override
    @Contract(pure = true)
    public void start(Stage stage) {
        this.stage = stage;
        DeclarativeFXRuntime.running = true;
    }

    /**
     * @param component The {@link grevend.declarativefx.component.Component} that should be shown.
     *
     * @since 0.6.1
     */
    public synchronized void show(@NotNull Component<? extends Node> component) {
        var node = component.getNode();
        var scene = new Scene(node instanceof Parent ? ((Parent) node) : new Group(node));
        Platform.runLater(() -> stage.setScene(scene));
    }

}
