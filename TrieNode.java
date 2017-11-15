package com.trie;

import java.util.ArrayList;
import java.util.List;

public class TrieNode {

	private char value;
	private List<TrieNode> children;
	private boolean isName;
	
	public TrieNode(char val) {
		this.value=val;
		children= new ArrayList<TrieNode>();
	}
	
	public List<TrieNode> getChildren(){
		return children;
	}
	
	public char getValue() {
        return value;
    }
	
	public boolean isName() {
		return isName;
	}
	
	public void setIsName(boolean val) {
		isName=val;
	}
	public void addChildren(TrieNode newNode) {
		children.add(newNode);
	}
	public boolean childrenMatch(char item) {
		for(TrieNode tn: children) {
			if(tn.getValue()==item)
				return true;
		}
		return false;
	}
}
