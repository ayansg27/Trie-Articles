package com.trie;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Trie {

	private TrieNode root;
	private HashMap<String,List<String>> compData;
	private HashMap<String,Integer> nameMap;
	private int currentPosition;
	
	public Trie() {
		root = new TrieNode(' ');
		compData=new HashMap<String,List<String>>();
		nameMap=new HashMap<String,Integer>();
		currentPosition=0;
	}
	//method to add new company name
	public void add(String name,String fName) {
		TrieNode trie=root;
		if(name==null) {
			return;
		}
		char[] characters=name.toCharArray();
		int pos=0;
		insertName(trie,characters,pos,fName);
	}
	//method to recursively store company name to trie
	public void insertName(TrieNode trie, char[] chars, int position, String finName) {
		List<TrieNode> children=trie.getChildren();
		boolean newFlag=true;
		if(position<chars.length) {
			if(trie.childrenMatch(chars[position])) {
				System.out.println("Match found for position: "+position+" -> "+chars[position]);
				TrieNode tn=trie.getChild(chars[position]);
				position++;
				insertName(tn,chars,position,finName);
			}
			else {
				TrieNode newChild=new TrieNode(chars[position]);
				trie.addChildren(newChild);
				if(position==chars.length-1) {
					newChild.setIsName(true);
					newChild.setFinalName(finName);
				}
				System.out.println("New Node for position: "+position+" -> "+newChild.getValue());
				position++;
				trie=newChild;
				insertName(trie,chars,position,finName);
			}
		}
		else{
			System.out.println("End of word");
		}/*
		if(children.size()>0) {
			for(TrieNode tn: children) {
				if(tn.getValue()==chars[position]) {
					System.out.println("position: "+position);
					System.out.println("Match found for "+chars[position]);
					if(position<chars.length) {
						position++;
						trie=tn;
						newFlag=false;
						insertName(trie,chars,position,finName);
					}
				}
			}
		}
		if(newFlag) {
			if(position<chars.length) {
				TrieNode newChild=new TrieNode(chars[position]);
				trie.addChildren(newChild);
				if(position==chars.length-1) {
					newChild.setIsName(true);
					newChild.setFinalName(finName);
				}
				System.out.println("Further nodes "+position);
				System.out.println("New Node for "+newChild.getValue());
				position++;
				trie=newChild;
				insertName(trie,chars,position,finName);
			}
			else {
				System.out.println("End of word");
				return false;
			}
		}*/
	}
	//function to create trie
    public void createCompanyTrie(){
		try {
			BufferedReader brd=new BufferedReader(new FileReader("company.dat"));
			String inputLine=new String();
			try {
				inputLine=brd.readLine();
				while (inputLine!= null) {
					if(inputLine.contains("\t")) {
						String[] company=inputLine.split("\t");
						String finalName=company[0];
						for(String str: company) {
							System.out.println(str);
							String lower=str.toLowerCase();
							add(lower,finalName);
						}
					}
					else {
						System.out.println(inputLine);
						String str=inputLine.toLowerCase();
						add(str,inputLine);
					}
					inputLine=brd.readLine();
				}
				
			}catch(IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if(brd!=null) {
						brd.close();
					}
				}catch(IOException exc) {
					exc.printStackTrace();
				}
			}
		}catch(FileNotFoundException e) {
			//e.printStackTrace();
			System.out.println("No such file exists.");
		}
    }
    //method to search article
    public void searchArticle(String article) {
    	System.out.println("Starting search");
    	TrieNode node=root;
    	char[] articleCharSet=article.toCharArray();
    	System.out.println(articleCharSet);
    	int articleSetLength=articleCharSet.length;
    	int charPos=0;
    	boolean searchStartFlag=false;
    	while(charPos<articleSetLength) {
    		searchStartFlag=node.childrenMatch(articleCharSet[charPos]);
    		System.out.println("Searching for "+articleCharSet[charPos]+" "+searchStartFlag);
    		if(searchStartFlag) {
    			System.out.println("Starting name search");
    			searchName(charPos,articleCharSet,node,articleSetLength);
    			System.out.println("current position: "+currentPosition);
    			int checkPos=charPos;
    			for(int x=currentPosition;x<articleSetLength;x++) {
    				if(articleCharSet[x]==' ') {
    					charPos=x+1;
    					break;
    				}
    			}
    			if(charPos==checkPos) {
    				charPos=articleSetLength;
    			}
    		}
    		else {
    			int checkPos=charPos;
    			for(int x=charPos+1;x<articleSetLength;x++) {
    				if(articleCharSet[x]==' ') {
    					charPos=x+1;
    					break;
    				}
    			}
    			if(charPos==checkPos) {
    				charPos=articleSetLength;
    			}
    		}
    	}
    	System.out.println(nameMap);
    }
    //method to search for company name
    public void searchName(int pos,char[] charSet,TrieNode node,int length) {
    	try {
    		System.out.println("Searching for "+charSet[pos]);
    		//if next character matches
        	if(node.childrenMatch(charSet[pos])) {
        		System.out.println("Match for "+charSet[pos]);
        		node=node.getChild(charSet[pos]);
        		//if it is a matching name
        		if(node.isName()) {
        			System.out.println("is name");
        			if(pos==length) {
        				currentPosition=length;
        			}
        			//if trie has children for this node
        			if(node.getChildren().size()>0) {
        				int nextState=node.getChildState(charSet[++pos]);
        				if(nextState==1) {
        					TrieNode next=node.getChild(charSet[pos]);
        					if(next.getValue()==' ') {
                				int tn=next.getChildState(charSet[++pos]);
                				if(tn==0) {
                					String comName=node.getFinalName();
                    				if(nameMap.containsKey(comName)) {
                    					int val=nameMap.get(comName);
                    					val++;
                    					nameMap.put(comName, val);
                    				}
                    				else {
                    					nameMap.put(comName,1);
                    				}
                    				pos--;
                    				currentPosition=pos;
                				}
                				else {
                					pos--;
                					searchName(pos,charSet,node,length);
                				}
                			}
                			else if(Character.isAlphabetic(next.getValue())) {
                				currentPosition=pos;
                			}
        				}
        			}
        			//end of trie branch 
        			else {
        				String comName=node.getFinalName();
        				if(nameMap.containsKey(comName)) {
        					int val=nameMap.get(comName);
        					val++;
        					nameMap.put(comName, val);
        				}
        				else {
        					nameMap.put(comName,1);
        				}
        				currentPosition=pos;
        			}
        			
        		}
        		else {
        			if(length==pos) {
        				currentPosition=length;
        			}
        			pos++;
        			searchName(pos,charSet,node,length);
        		}
        	}
        	else {
        		currentPosition=pos;
        	}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
}
