package Tree2;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Leaf {
	
	private String Label;
	private String Content;
	public List<Leaf> Children;
	private Leaf Parent;
	private String Type; //falta implementar
	private String Code;


	public String getCode(){
		return this.Code;
	}
	public void setCode(String S){
		this.Code = S;
	}

	public Leaf() {
        	Children = new ArrayList<Leaf>();
		Type = "void";
 	}

	public String getType(){
		return this.Type;
	}

	public void setType(String T){
		this.Type = T;
	}

	public void Print() {
		System.out.print(", ");
		System.out.print(this.Label);
		if(this.Content != "NULL"){
			System.out.print("(");
			System.out.print(this.Content);
			System.out.print(")");
		}
		else{
			System.out.print("(");
			for(int i = 0; i < Children.size(); i ++){
				Leaf aux = Children.get(i);
				aux.Print();
			}
			System.out.print(")");
		}
	}
	public Leaf(String Lab, String Cont) {
		this();
		this.Label = Lab;
		this.Content = Cont;

	}
	
	public Leaf getParent(){
		return this.Parent;
	}

	public String getContent(){
		return this.Content;
	}
	
	public void setContent(String T){
		this.Content = T;
	}

	public String toString(){
		return this.Label;
	}

	public void addChild(Leaf child) {
        	child.Parent = this;
        	this.Children.add(child);
    	}
}
