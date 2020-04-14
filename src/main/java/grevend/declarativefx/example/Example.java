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

package grevend.declarativefx.example;

import grevend.declarativefx.DeclarativeFX;
import grevend.declarativefx.bindable.BindableValue;
import grevend.declarativefx.util.Verbosity;
import javafx.application.Application;
import javafx.stage.Stage;

import static grevend.declarativefx.component.Controls.Button;
import static grevend.declarativefx.component.Layout.HBox;
import static grevend.declarativefx.component.Layout.Text;

public class Example extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setWidth(175);
        stage.setHeight(100);

        BindableValue counter = new BindableValue(0);

        var root = HBox(
            Text("Value: 0").compute("text", counter, () -> "Value: " + counter.get()),
            Button("Increment").on((event, component) -> {
                counter.update(before -> (int) before + 1);
            })
        );

        DeclarativeFX.show(root, stage);
        System.out.println(DeclarativeFX.stringifyHierarchy(root, Verbosity.DETAILED));
        System.out.println(DeclarativeFX.treeifyHierarchy(root, Verbosity.DETAILED));
    }

}