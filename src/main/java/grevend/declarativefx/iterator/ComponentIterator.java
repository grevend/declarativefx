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

package grevend.declarativefx.iterator;

import grevend.declarativefx.component.Component;
import javafx.scene.Node;
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class ComponentIterator implements Iterator<Component<? extends Node>> {

    private final Queue<Iterator<Component<? extends Node>>> queue;
    private final TreeView<String> treeView;
    private final Component<? extends Node> root;

    public ComponentIterator(@NotNull Component<? extends Node> root) {
        this.queue = new ArrayDeque<>();
        this.treeView = new TreeView<>();
        this.root = root;
        this.queue.add(root.getChildren().iterator());
    }

    @NotNull
    public TreeView<String> getTreeView() {
        return treeView;
    }

    @NotNull
    public Component<? extends Node> getRoot() {
        return root;
    }

    @Override
    public boolean hasNext() {
        while (!this.queue.isEmpty()) {
            if (this.queue.peek().hasNext()) {
                return true;
            } else {
                this.queue.poll();
            }
        }
        return false;
    }

    @NotNull
    @Override
    public Component<? extends Node> next() {
        var next = this.queue.element().next();
        this.queue.offer(next.getChildren().iterator());
        return next;
    }

}
