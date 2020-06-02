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

import grevend.declarativefx.bindable.BindableValue;

import static grevend.declarativefx.test.Verifier.bool;
import static grevend.declarativefx.test.Verifier.string;

public class Test {

    public static void main(String[] args) {
        /*var text = Text("Hello World").fixture();
        text.prop("text").verify(string());
        //text.prop("text").verify(numeric()); //Exception
        text.prop("text").matches("Hello World");

        value(12).verify(range(6, 24));*/

        BindableValue bindableValue = new BindableValue(12);
        /*var assertion = bindableValue.assertion();
        assertion.changes(3);
        assertion.change(12, 4);
        assertion.change(4, string());
        assertion.change(string(), bool());*/

        var assertion = bindableValue.assertion()
            .changes(3)
            .from(12).to(4).to(string()).to(bool());

        bindableValue.set(4);
        bindableValue.set("Hello World!");
        bindableValue.set(true);

        assertion.verify();
    }

}
