package de.bujanowski.midijazz.theory.music.elements;

import java.util.ArrayList;
import java.util.List;

public class ScaleHierarchy {
}

class Node {
    private final Scale data;
    private List<Node> children;

    public Node(Scale data) {
        this.data = data;
        this.children = new ArrayList<>();
    }
}
