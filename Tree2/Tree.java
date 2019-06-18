package Tree2;

import java.util.*;

public class Tree {
	
	private Leaf Root;
	public Tree(){
	}
	public void Print(){
		this.Root.Print();
		System.out.println("");
	}
	public Leaf getRoot(){
		return this.Root;
	}
	public void setRoot(Leaf Root){
		this.Root = Root;
	}
}
