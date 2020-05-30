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

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public final class Robot {

    private final Scene scene;
    private final boolean headless;
    private final java.awt.Robot robot;

    @Contract(pure = true)
    public Robot(@NotNull Scene scene, boolean headless, boolean waitForIdle, int delay) throws AWTException {
        this.scene = scene;
        this.headless = headless;
        System.setProperty("java.awt.headless", headless ? "true" : "false");
        robot = new java.awt.Robot();
        robot.setAutoWaitForIdle(waitForIdle);
        robot.setAutoDelay(delay);
    }

    @Contract(pure = true)
    public Robot(@NotNull Scene scene) throws AWTException {
        this(scene, true, true, 100);
    }

    @Contract(pure = true)
    public final boolean headless() {
        return this.headless;
    }

    public final void delay(int delay) {
        this.robot.delay(delay);
    }

    public void pressKey(int code) {
        this.robot.keyPress(code);
    }

    public void pressKey(@NotNull KeyCode code) {
        this.pressKey(code.getCode());
    }

    public void releaseKey(int code) {
        this.robot.keyRelease(code);
    }

    public void releaseKey(@NotNull KeyCode code) {
        this.releaseKey(code.getCode());
    }

    public void enterKey(char c) {
        this.enterKey((int) c);
    }

    public void enterKey(int code) {
        if (Character.isUpperCase(code)) { pressKey(KeyEvent.VK_SHIFT); }
        this.pressKey(Character.toUpperCase(code));
        this.releaseKey(Character.toUpperCase(code));
        if (Character.isUpperCase(code)) { releaseKey(KeyEvent.VK_SHIFT); }
    }

    public void enterText(@NotNull String text) {
        text.chars().forEach(this::enterKey);
    }

    public void moveMouse(int x, int y) {
        robot.mouseMove(x, y);
    }

    public void pressMouse(@NotNull MouseButton button) {
        robot.mousePress(button.equals(MouseButton.PRIMARY) ? InputEvent.BUTTON1_DOWN_MASK :
            (button.equals(MouseButton.MIDDLE) ? InputEvent.BUTTON3_DOWN_MASK : InputEvent.BUTTON2_DOWN_MASK));
    }

    public void releaseMouse(@NotNull MouseButton button) {
        robot.mouseRelease(button.equals(MouseButton.PRIMARY) ? InputEvent.BUTTON1_DOWN_MASK :
            (button.equals(MouseButton.MIDDLE) ? InputEvent.BUTTON3_DOWN_MASK : InputEvent.BUTTON2_DOWN_MASK));
    }

    public void clickMouse(@NotNull MouseButton button) {
        this.pressMouse(button);
        this.releaseMouse(button);
    }

    public void wheelMouse(int amount) {
        this.robot.mouseWheel(amount);
    }

}
