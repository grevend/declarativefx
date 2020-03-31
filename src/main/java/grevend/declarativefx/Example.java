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

package grevend.declarativefx;

import grevend.declarativefx.components.Layout;
import javafx.application.Application;
import javafx.stage.Stage;

import static grevend.declarativefx.components.Compat.Binding;
import static grevend.declarativefx.components.Compat.Root;
import static grevend.declarativefx.components.Controls.Button;
import static grevend.declarativefx.components.Layout.*;

public class Example extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setWidth(175);
        stage.setHeight(100);

        var root = Root(
            HBox(
                VBox(
                    Binding("num", "text", (num, text) ->
                        VBox(
                            Text(text.compute(num, () -> "Value: " + num.get(0))),
                            Button("Increment", event -> {
                                num.set((int) num.get(0) + 1);
                            }),
                            VBox(
                                Binding("num", Layout::Text),
                                VBox(
                                    Text("1"),
                                    Text("2")
                                )
                            ),
                            VBox(
                                VBox(
                                    Text("3")
                                )
                            )
                        )
                    )
                )
            )
        );
        root.construct();
        System.out.println(root.stringifyHierarchy());
        root.launch(stage);
    }

}