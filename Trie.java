package com.trie;

import java.util.List;

public class Trie {

	private TrieNode root;
	
	public Trie() {
		root = new TrieNode(' ');
	}
	//method to add new company name
	public void add(String name) {
		TrieNode trie=root;
		if(name==null) {
			return;
		}
		char[] characters=name.toCharArray();
		int pos=0;
		insertName(trie,characters,pos);
	}
	//method to recursively store company name to trie
	public boolean insertName(TrieNode trie, char[] chars, int position) {
		List<TrieNode> children=trie.getChildren();
		boolean newFlag=true;
		if(children.size()>0) {
			for(TrieNode tn: children) {
				if(tn.getValue()==chars[position]) {
					System.out.println("position: "+position);
					System.out.println("Match found for "+chars[position]);
					position++;
					trie=tn;
					newFlag=false;
					insertName(trie,chars,position);
				}
			}
		}
		if(newFlag) {
			if(position<chars.length) {
				TrieNode newChild=new TrieNode(chars[position]);
				trie.addChildren(newChild);
				if(position==chars.length-1) newChild.setIsName(true);
				System.out.println("Further nodes "+position);
				System.out.println("New Node for "+newChild.getValue());
				position++;
				trie=newChild;
				insertName(trie,chars,position);
			}
			else {
				System.out.println("End of word");
				return false;
			}
		}
		return true;
	}
	//method to perform lookahead search for company name
	public boolean searchStart(char current) {
		TrieNode trie=root;
		boolean startSearch=trie.childrenMatch(current);
		return startSearch;
	}
}
