package de.bujanowski.midijazz.theory.music.structures;

import de.bujanowski.midijazz.theory.music.elements.Scale;

import java.util.ArrayList;
import java.util.List;

public class ScaleHierarchy {

    private Node root;

    public ScaleHierarchy() {
        this.root = new Node(Scale.buildScale(new String[] {"Twelve Tone Scale"}, "HHHHHHHHHHHH", 0, null));
    }

    public boolean addScale(Scale scale) {
        return this.root.addScale(scale);
    }

    public void log() {
        this.root.log("");
    }
}

class Node {
    private final Scale data;
    private List<Node> children;

    public Node(Scale data) {
        this.data = data;
        this.children = new ArrayList<>();
    }

    public boolean addScale(Scale scale) {
        if(scale.isSubscale(this.data)) {
            // find out if subscale of a child
            for(Node child : this.children) {
                // if child returns true -> added to child tree
                if(child.addScale(scale)) return true;
            }
            // reaching this point means no child added scale -> add it as new child
            this.children.add(new Node(scale));
            return true;
        } else return false;
    }

    public void log(String tab) {
        System.out.println(tab + this.data.toString());
        for(Node child : this.children) {
            child.log(tab + "   ");
        }
    }

    public List<Scale> getScaleList() {
        List<Scale> scaleList = new ArrayList<>();
        this.addScaleToList(scaleList);
        return scaleList;
    }

    private void addScaleToList(List<Scale> scaleList) {
        scaleList.add(this.data);
        for(Node child : this.children) {
            child.addScaleToList(scaleList);
        }
    }
}
