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
            Executors.newSingleThreadExecutor().execute(() -> launch(new String[0]));
            while (!running) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Failed to launch JavaFX test application.", e);
                }
            }
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
        } else {
            Platform.exit();
            DeclarativeFXRuntime.running = false;
        }
    }

    /**
     * @param component The {@link grevend.declarativefx.component.Component} that should be shown.
     *
     * @since 0.6.1
     */
    @NotNull
    @Contract("_ -> param1")
    public static synchronized Component<? extends Node> show(@NotNull Component<? extends Node> component) {
        var node = component.getNode();
        var scene = new Scene(node instanceof Parent ? ((Parent) node) : new Group(node));
        show(scene);
        return component;
    }

    /**
     * @param scene The {@link javafx.scene.Scene} that should be shown.
     *
     * @since 0.6.5
     */
    public static synchronized void show(@NotNull Scene scene) {
        if (DeclarativeFXRuntime.stage != null) {
            DeclarativeFXRuntime.stage.close();
            DeclarativeFXRuntime.stage = null;
        }
        if (!DeclarativeFXRuntime.running) {
            throw new IllegalStateException("DeclarativeFX test runtime is not running.");
        } else {
            Platform.runLater(() -> {
                var stage = new Stage();
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();
                DeclarativeFXRuntime.stage = stage;
            });
            while (DeclarativeFXRuntime.stage == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Failed to create JavaFX test application stage.", e);
                }
            }
        }
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
        DeclarativeFXRuntime.running = true;
    }

}
