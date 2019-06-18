	package Table;
import java.util.ArrayList;

public class Register {
	
	private String Name;
	private String Level;
	private String Type;
	private String Temp;
	private ArrayList<String> Records;
	private ArrayList<String> RecordsType;
	public Register(String N, String L){
		this.Name = N;
		this.Level  = L; 
	}	
	
	public Integer getRecordSize(){
		return this.Records.size();
	}


	
	public void addRecord(String S, String T){
		this.Records.add(S);
		this.RecordsType.add(T);
	}
	public String getRecord(Integer i){
		return this.Records.get(i);
	}
	public String getRecordByName(String S){//se achar retorna o tipo, se nao achar retorna null
		for(var i = 0; i < Records.size(); i++){
			if(Records.get(i) == S){
				return RecordsType.get(i);
			}
		}
		return null;
	}

	public String getRecordsType(Integer i){
		return this.RecordsType.get(i);
	}

	public void setType(String T){
		this.Type = T;
	}
	public String getType(){
		return this.Type;
	}

	public void setTemp(String T){
		this.Temp = T;
	}

	public String getTemp(){
		return this.Temp;
	}
	public void setName(String N){
		this.Name = N;
	}
	public String getName(){
		return this.Name;
	}

}
