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

import grevend.declarativefx.components.Root;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class Window<P extends Parent> extends Root<P> {

    public Window(@NotNull Component<P> child) {
        super(child);
    }

    @Override
    public @NotNull Root<P> launch(@NotNull Stage stage) {
        throw new UnsupportedOperationException();
    }

    public @NotNull Window<P> launch(@NotNull Stage parentStage, @NotNull Modality modality) {
        var stage = new Stage();
        stage.initModality(modality);
        stage.initOwner(parentStage);

        this.beforeConstruction();
        var tree = this.construct();
        if (tree != null) {
            this.afterConstruction();
            this.setScene(new Scene(tree));
            stage.setScene(this.getScene());
            stage.show();
        } else {
            throw new IllegalStateException("Component hierarchy construction failed.");
        }

        stage.show();
        return this;
    }

}
