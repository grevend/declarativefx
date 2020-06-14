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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author David Greven
 * @since 0.6.1
 */
public final class DeclarativeFXRuntime extends Application {

    protected static volatile Stage stage = null;
    protected static volatile boolean running = false;

    /**
     * @return
     *
     * @since 0.6.5
     */
    @Contract(pure = true)
    public static synchronized boolean running() {
        return DeclarativeFXRuntime.running;
    }

    /**
     * Starts the runtime that will be used for testing.
     *
     * @since 0.6.1
     */
    @SuppressWarnings("unused")
    public static synchronized void launch() {
        if (running) {
            throw new IllegalStateException("Close previous test runtime before launching a new one.");
        } else {
            Executors.newSingleThreadExecutor().execute(() -> {
                Platform.setImplicitExit(false);
                launch(new String[0]);
            });
        }
    }

    /**
     * Stops the JavaFX application.
     *
     * @since 0.6.1
     */
    public static synchronized void exit() {
        if (!running) {
            throw new IllegalStateException("No test runtime currently running.");
        }
        Platform.runLater(() -> {
            if (DeclarativeFXRuntime.stage != null) {
                DeclarativeFXRuntime.stage.close();
                DeclarativeFXRuntime.stage = null;
            }
            DeclarativeFXRuntime.running = false;
            Platform.exit();
        });
    }

    /**
     * @param component The {@link grevend.declarativefx.component.Component} that should be shown.
     *
     * @since 0.6.1
     */
    @NotNull
    public static synchronized Robot show(@NotNull Component<? extends Node> component) throws AWTException {
        var node = component.getNode();
        var scene = new Scene(node instanceof Parent ? ((Parent) node) : new VBox(node));
        return robot(show(scene));
    }

    /**
     * @param component The {@link grevend.declarativefx.component.Component} that should be shown.
     * @param width     The width of the window.
     * @param height    The height of the window.
     *
     * @since 0.6.9
     */
    @NotNull
    public static synchronized Robot show(@NotNull Component<? extends Node> component, double width, double height)
        throws AWTException {
        var node = component.getNode();
        var scene = new Scene(node instanceof Parent ? ((Parent) node) : new VBox(node));
        return robot(show(scene, width, height));
    }

    /**
     * @param scene The {@link javafx.scene.Scene} that should be shown.
     *
     * @since 0.6.5
     */
    @NotNull
    @Contract("_, _, _ -> param1")
    public static synchronized Scene show(@NotNull Scene scene, double width, double height) {
        if (!DeclarativeFXRuntime.running) {
            throw new IllegalStateException("DeclarativeFX test runtime is not running.");
        } else {
            var latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                DeclarativeFXRuntime.stage.setScene(scene);
                if (width < 0 || height < 0) {
                    DeclarativeFXRuntime.stage.setFullScreen(true);
                } else {
                    DeclarativeFXRuntime.stage.setMinWidth(width);
                    DeclarativeFXRuntime.stage.setMaxWidth(width);
                    DeclarativeFXRuntime.stage.setMinHeight(height);
                    DeclarativeFXRuntime.stage.setMaxHeight(height);
                }
                DeclarativeFXRuntime.stage.setAlwaysOnTop(true);
                DeclarativeFXRuntime.stage.setResizable(false);
                scene.getRoot().setVisible(true);
                DeclarativeFXRuntime.stage.show();
                latch.countDown();
            });
            try {
                latch.await(15, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to create JavaFX test application stage.", e);
            }
        }
        return scene;
    }

    @NotNull
    @Contract("_ -> param1")
    public static synchronized Scene show(@NotNull Scene scene) {
        return show(scene, -1.0, -1.0);
    }

    /**
     * @return The current {@link javafx.stage.Stage}.
     *
     * @since 0.6.5
     */
    @NotNull
    public static synchronized Stage stage() {
        if (!DeclarativeFXRuntime.running) {
            throw new IllegalStateException("DeclarativeFX test runtime is not running.");
        } else if (DeclarativeFXRuntime.stage == null) {
            throw new IllegalStateException("No stage defined for the current test runtime.");
        }
        return DeclarativeFXRuntime.stage;
    }

    /**
     * @return The current {@link javafx.scene.Scene}.
     *
     * @since 0.6.5
     */
    @NotNull
    public static synchronized Scene scene() {
        var scene = stage().getScene();
        if (scene == null) {
            throw new IllegalStateException("No scene defined for the current test stage.");
        }
        return scene;
    }

    /**
     * @param scene
     *
     * @return
     *
     * @throws AWTException
     * @since 0.6.9
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    private static Robot robot(@NotNull Scene scene) throws AWTException {
        return new Robot(scene);
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
        DeclarativeFXRuntime.stage = stage;
        DeclarativeFXRuntime.running = true;
    }

}
