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

package grevend.declarativefx.properties;

import grevend.declarativefx.Component;
import grevend.declarativefx.util.BindException;
import grevend.declarativefx.util.BindableValue;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Bindable<N extends Node, C extends Component<N>> {

    default @Nullable String getDefaultProperty() {
        return null;
    }

    @SuppressWarnings("unchecked")
    default <V> C bind(@NotNull BindableValue<V> bindableValue) {
        if (this.getDefaultProperty() != null) {
            this.bind(this.getDefaultProperty(), bindableValue);
        } else {
            throw new BindException(this.toString() + " does not provide a default property.");
        }
        return (C) this;
    }

    <V> C bind(@NotNull String property, @NotNull BindableValue<V> bindableValue);

}
