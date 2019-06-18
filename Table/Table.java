package Table;
import java.util.ArrayList;

import Tree2.Leaf;
import Tree2.Tree;

public class Table{

	private ArrayList<Register> RealTable;
	private ArrayList<Leaf> Functions;  //guarda a raiz das funcoes
	private ArrayList<String> FunctionsLabels; //respectivos lbels da raizes
	private String Level;
	private Integer Nargs;
	public Integer CounterLabel = 0; //geracao de label
	public Integer CounterTemp = 0; //geraçai de temps

	public Table(Tree ResultTree){
		this.Nargs = 0;
		CounterTemp = 0;
		CounterLabel = 0;

		this.RealTable =  new ArrayList<Register>();
		Register inttype = new Register("int","0");
		inttype.setType("type");	
		Register stringtype = new Register("string", "0");	
		stringtype.setType("type");
		RealTable.add(inttype);
		RealTable.add(stringtype);
		this.Level = "0";
		Leaf Root = ResultTree.getRoot();
		this.addreg(Root);
		System.out.println("Codigo intermediario:");
		System.out.println(Root.Children.get(0).getCode());
	}

	public String getLabel(){
		return this.CounterLabel.toString();
	}

	public Register Search(String Name){
		Register aux = null;
		Integer i;
		
		Integer limit = this.RealTable.size();
		for(i = 0; i < limit; i++){
			aux = RealTable.get(i);
			if(Name.equals(aux.getName())){
				return aux;
			}
		}
		if(i == limit){
			return null;
		}
		else{
			return aux;
		}
	}
	
	public void addSymT(String Name, String Level, String Type){
		if(Search(Name) == null)
		{ 
			Register Insertion = new Register(Name, Level);
			Insertion.setType(Type);
			RealTable.add(Insertion);
		}
	}
	public void addSym(String Name, String Level){
		if(Search(Name) == null)
		{
		Register Insertion = new Register(Name, Level);
		RealTable.add(Insertion);
		}
	}

	public void backSet(String Type){
		Integer LastIndex = this.RealTable.size() - 1;
		Register aux;
		for(var i = 0; i < Nargs; i++){
			aux = this.RealTable.get(LastIndex - i);
			aux.setType(Type);
		}
		this.Nargs = 0;
	}



	//funcao para andar na arvore e pegar o tipo que uma expressao retorna
	//public getType();


	public void addreg(Leaf ResultLeaf){
		this.addreg2(ResultLeaf);
	//	for(var i=0; i < ResultLeaf.Children.size(); i++){
	//		Leaf aux = ResultLeaf.Children.get(i);
	//		System.out.print(aux.toString());
	//		System.out.print("	");
	//	}
	//	System.out.println("");
		for(var i=0; i < ResultLeaf.Children.size(); i++){
			Leaf aux = ResultLeaf.Children.get(i);
			this.addreg(aux);
		}
	}

	public void completeBreak(Leaf Root, String Complete){
		if(Root.toString() == "WHILE"){
			return;
		}
		if(Root.toString() == "FOR"){
			return;
		}
		if(Root.toString() == "BREAK"){
			Root.setCode(Root.getCode() + "," + Complete + ")" );
		}

		for(var i=0; i < Root.Children.size(); i++){
			Leaf aux = Root.Children.get(i);
			this.addreg(aux);
		}
	}
	

	public void addreg2(Leaf Rl){
		if(Rl.toString() == "exp"){
			this.exp(Rl);
		}
		else if(Rl.toString() == "args"){
	//		this.args(Rl);
		}
		else if(Rl.toString() == "args1"){
	//		this.args1(Rl);
		}
		else if(Rl.toString() == "expseq"){
			this.expseq(Rl);
		}
		else if(Rl.toString() == "idbracs"){
	//		this.idbracs(Rl);
		}
		else if(Rl.toString() == "lvaluecont"){
			this.lvaluecont(Rl);
		}	
		else if(Rl.toString() == "letexp"){
			this.letexp(Rl);
		}
		else if(Rl.toString() == "decs"){
			this.decs(Rl);
		}
		else if(Rl.toString() == "typeid"){
			this.typeid(Rl);
		}
		else if(Rl.toString() == "idexps"){
			this.idexps(Rl);
		}
		else if(Rl.toString() == "ID"){
			this.ID(Rl);
		}
//		System.out.println("print depues");
//		System.out.println(Rl.toString());
//		System.out.println(Rl.getCode());
	
	}

	public void lvaluecont(Leaf lvaluecont){	
		if(lvaluecont.Children.size() == 1){
			Leaf aux = lvaluecont.Children.get(0); //ID
			addreg2(aux);
			lvaluecont.setContent(aux.getContent());

			if(Search(aux.getContent()) == null){	//verifica se foi declarado
				String NTemp = "T" + CounterTemp;
				CounterTemp = CounterTemp + 1;
				
			//	System.out.println("a variavel " + aux.getContent() + " é " + NTemp);
				Register Insertion = new Register(aux.getContent(), "0");
				Insertion.setTemp(NTemp);
				lvaluecont.setCode("TEMP " + NTemp);
				RealTable.add(Insertion);

			}
			else{
				Register value = Search(aux.getContent());
				lvaluecont.setCode("TEMP " + value.getTemp());
			}
		}
	}

	public void exp(Leaf exp){
		if(exp.Children.size() == 1){
			Leaf aux = exp.Children.get(0);
			addreg2(aux);
			if(aux.toString() == "NIL"){
				aux.setCode("NIL");
				exp.setType(aux.getType());
				exp.setCode(aux.getCode());
			}
			else if(aux.toString() == "NUM"){
				aux.setCode("CONST  " + aux.getContent());
				aux.setType("int");
				exp.setType("int");
				exp.setCode(aux.getCode());
			}
			else if(aux.toString() == "STRING"){
				aux.setCode("STRING  " + aux.getContent());
				aux.setType("string");
				exp.setType("string");
				exp.setCode(aux.getCode());
		
			}
			else if(aux.toString() == "BREAK"){
				aux.setCode("JUMP( " ); //esta inteligencia vai ficar pro pai
				//coloca um JUMP para esse desvio 
				// tem de rodar a rotina auxiliar no final do loop	
				exp.setCode(aux.getCode());
			}
			else if(aux.toString() == "lvaluecont"){
				// prosegue
				exp.setCode(aux.getCode());
				exp.setType(aux.getType());
				exp.setContent(aux.getContent());
			}
			else if(aux.toString() == "letexp"){
				exp.setCode(aux.getCode());
			}
			else if(aux.toString() == "exp"){
				exp.setCode(aux.getCode());
				exp.setType(aux.getType());
				exp.setContent(aux.getContent());
			}
			else{
				System.out.println("ERROR");
				System.out.println(exp.Children.size());
				System.out.println(exp.toString());
				System.out.println(aux.toString());
				System.out.println(aux.Children.get(0).toString());
				
				//erro
			}
		}
		else if(exp.Children.size() == 2){	
			Leaf aux = exp.Children.get(0);
			Leaf aux2 = exp.Children.get(1);
			addreg2(aux);
			addreg2(aux2);
			if(aux.toString() == "MINUS"){
				if(aux2.toString() == "exp"){
					if(aux2.getType() != "int"){
						System.out.println("Não se pode negar não inteiros");
						System.out.println("Erro Semantico");
						System.exit(1);
					}
					String Code = " BINOP( MINUS, CONST 0, ";
					exp.setCode(Code + aux2.getCode() + ")");
					System.out.println(exp.getCode());
				}
				else{
					System.out.println("Error");
					System.exit(1);
				}
			}
			else if(aux.toString() == "COMMENT"){
				if(aux2.toString() == "exp"){
					exp.setCode(aux2.getCode());
					exp.setType(aux2.getType());
					exp.setContent(aux2.getContent());
				}
				else{
					System.out.println("Error");
					System.exit(1);
				}
			}
			else{
				System.out.println("Inesperado");
				System.exit(1);
			}

		}
		else if(exp.Children.size() == 3){
			Leaf aux = exp.Children.get(0);
			Leaf aux2 = exp.Children.get(1);
			Leaf aux3 = exp.Children.get(2);
			addreg2(aux);
			addreg2(aux2);
			addreg2(aux3);
			if(aux.toString() == "exp"){
				if(aux2.toString() == "TIMES"){
					if(aux3.toString()  != "exp"){
						System.out.println("error");
					}
					if(aux.getType() == "string"){
						System.out.println("Tipo inadequado para multiplicacao");
						System.exit(1);
					}	
					if(aux3.getType() == "string"){
						System.out.println("Tipo inadequado para multiplicacao");
						System.exit(1);
					}
					String Code = " BINOP( TIMES, ";
					Code = Code + aux.getCode();
					Code = Code + ", " + aux3.getCode() + ")";
					exp.setCode(Code);
					//exp.setContent();
					exp.setType("int");
				}
				else if(aux2.toString() == "DIVIDE"){
						if(aux3.toString()  != "exp"){
						System.out.println("error");
					}
					if(aux.getType() == "string"){
						System.out.println("Tipo inadequado para div");
						System.exit(1);
					}	
					if(aux3.getType() == "string"){
						System.out.println("Tipo inadequado para div");
						System.exit(1);
					}
					String Code = " BINOP( DIVIDE, ";
					Code = Code + aux.getCode();
					Code = Code + ", " + aux3.getCode() + ")";
					exp.setCode(Code);
					//exp.setContent();
					exp.setType("int");

				}
				else if(aux2.toString() == "PLUS"){
						if(aux3.toString()  != "exp"){
						System.out.println("error");
					}
					if(aux.getType() == "string"){
						System.out.println("Tipo inadequado para soma");
						System.exit(1);
					}	
					if(aux3.getType() == "string"){
						System.out.println("Tipo inadequado para soma");
						System.exit(1);
					}
					String Code = " BINOP( PLUS, ";
					Code = Code + aux.getCode();
					Code = Code + ", " + aux3.getCode() + ")";
					exp.setCode(Code);
					//exp.setContent();
					exp.setType("int");

				}
				else if(aux2.toString() == "MINUS"){
						if(aux3.toString()  != "exp"){
						System.out.println("error");
					}
					if(aux.getType() == "string"){
						System.out.println("Tipo inadequado para SUB");
						System.exit(1);
					}	
					if(aux3.getType() == "string"){
						System.out.println("Tipo inadequado para SUB");
						System.exit(1);
					}
					String Code = " BINOP( MINUS, ";
					Code = Code + aux.getCode();
					Code = Code + ", " + aux3.getCode() + ")";
					exp.setCode(Code);
					//exp.setContent();
					exp.setType("int");

				}
				else if(aux2.toString() == "DIFF"){
						if(aux3.toString()  != "exp"){
						System.out.println("error");
					}
					String Code = " BINOP( DIFF, ";
					Code = Code + aux.getCode();
					Code = Code + ", " + aux3.getCode() + ")";
					exp.setCode(Code);
					//exp.setContent();
					exp.setType("bool");

				}
				else if(aux2.toString() == "EQUAL"){
						if(aux3.toString()  != "exp"){
						System.out.println("error");
					}
					String Code = " BINOP( EQUAL, ";
					Code = Code + aux.getCode();
					Code = Code + ", " + aux3.getCode() + ")";
					exp.setCode(Code);
					//exp.setContent();
					exp.setType("bool");

				}
				else if(aux2.toString() == "SMALLER"){
						if(aux3.toString()  != "exp"){
						System.out.println("error");
					}
					String Code = " BINOP( SMALLER, ";
					Code = Code + aux.getCode();
					Code = Code + ", " + aux3.getCode() + ")";
					exp.setCode(Code);
					//exp.setContent();
					exp.setType("bool");

				}
				else if(aux2.toString() == "BIGGER"){
					if(aux3.toString()  != "exp"){
						System.out.println("error");
					}
					String Code = " BINOP( BIGGER, ";
					Code = Code + aux.getCode();
					Code = Code + ", " + aux3.getCode() + ")";
					exp.setCode(Code);
					//exp.setContent();
					exp.setType("bool");

				}
				else if(aux2.toString() == "SMALLER_EQUAL"){
						if(aux3.toString()  != "exp"){
						System.out.println("error");
					}
					String Code = " BINOP( SMALLER_EQUAL, ";
					Code = Code + aux.getCode();
					Code = Code + ", " + aux3.getCode() + ")";
					exp.setCode(Code);
					//exp.setContent();
					exp.setType("bool");

				}
				else if(aux2.toString() == "BIGGER_EQUAL"){
						if(aux3.toString()  != "exp"){
						System.out.println("error");
					}
					String Code = " BINOP( BIGGER_EQUAL, ";
					Code = Code + aux.getCode();
					Code = Code + ", " + aux3.getCode() + ")";
					exp.setCode(Code);
					//exp.setContent();
					exp.setType("bool");

				}
				else if(aux2.toString() == "AND"){
						if(aux3.toString()  != "exp"){
						System.out.println("error");
					}
					String Code = " BINOP( AND, ";
					Code = Code + aux.getCode();
					Code = Code + ", " + aux3.getCode() + ")";
					exp.setCode(Code);
					//exp.setContent();
					exp.setType("bool");

				}

				else if(aux2.toString() == "OR"){
						if(aux3.toString()  != "exp"){
						System.out.println("error");
					}
					String Code = " BINOP( OR, ";
					Code = Code + aux.getCode();
					Code = Code + ", " + aux3.getCode() + ")";
					exp.setCode(Code);
					//exp.setContent();
					exp.setType("bool");

				}

			}
			else if(aux.toString() == "LPAR"){
				if(aux2.toString() == "expseq"){
					if(aux3.toString() == "RPAR"){
						exp.setType(aux2.getType());
						exp.setCode(aux2.getCode());
					}
				}
			}
			else if(aux.toString() == "lvaluecont"){
			//	if(aux.getType() != "NAME"){
			//		System.out.println(aux.getType());
			//		System.out.println("Atribuindo a algo que não é uma variavel");
			//		System.exit(1);
			//	}
	//			Register R = Search(aux.getContent());
			//	if(aux.getType() != aux3.getType()){
			//		System.out.println("Os tipo da atribuicao não conferem");
			//		System.exit(1);
			//	}
			///	if(R != null) {
					//String Temp = R.getTemp();
					//System.out.println(aux.getContent());
					String Code = "MOVE(" +  aux.getCode() + ", "+  aux3.getCode() + ")";
					exp.setCode(Code);

			//	}	
			}
			else if(aux.toString() == "idbracs"){
		//		if(aux.getType() != aux3.getType()){
		//			System.out.println("Os tipo da atribuicao não conferem");
		//			System.exit(1);
		//		}
				
				if(aux3.getType() == "int"){
					String code = "call( NAME initarray, const 0, ";
					code = code + aux.getContent() + ", ";
					code = code + aux3.getCode() + ")";
					exp.setCode(code);
					exp.setType("int");
				}
				else if(aux3.getType() == "NAME"){
					Register R = Search(aux3.getContent());
					if(R != null){
						String code = "CALL( NAME initarray, const 0, ";
						code = code + aux.getContent() + ", ";
						code = code + "TEMP " + R.getTemp() + ")";
						exp.setCode(code);
						exp.setType(R.getType());
					}
				}
				else{
					System.out.println("error");
					System.exit(1);
				}

					
			}
			else{
				System.out.println("Error");
				System.exit(1);	
			}
		}
		else if(exp.Children.size() == 4){
			Leaf aux = exp.Children.get(0);
			addreg2(aux);
			Leaf aux2 = exp.Children.get(1);
			addreg2(aux2);
			Leaf aux3 = exp.Children.get(2);
			addreg2(aux3);
			Leaf aux4 = exp.Children.get(3);
			addreg2(aux4);
			if(aux.toString() == "WHILE"){
				String label1 = "L" + this.getLabel();
				CounterLabel = CounterLabel + 1;
				String label2 = "L" + CounterLabel;
				CounterLabel = CounterLabel + 1;
				String label3 = "L" + CounterLabel;
				CounterLabel = CounterLabel + 1;
				if(aux2.getType() != "bool"){
					System.out.println("A cond do while não é bool");
					System.exit(1);
				}
				completeBreak(aux4, label3);	
				String Code = label1 + ":\n" + "CJUMP( " + " AND, " + aux2.getCode() + ", TRUE, " + label2 +", " + label3 +" )\n" + label2 + ":\n" + aux4.getCode()   + "\n JUMP( TRUE, "+ label1 +" )"+ "\n" + label3 + ":\n";
				exp.setCode(Code);		
			}
			else if(aux.toString() == "IF"){
				if(aux2.getType() != "bool"){
					System.out.println("A cond do while não é bool");
					System.exit(1);
				}
				String label1 = "L" + CounterLabel;
				CounterLabel = CounterLabel + 1;
				String label2 = "L" + CounterLabel;
				CounterLabel = CounterLabel + 1;
				String Code = "CJUMP( AND, " + aux2.getCode() + ",  TRUE, " + label1 + ", " + label2 + " )\n " + label1 + ":\n" + aux4.getCode() + "\n"  + label2 +":\n";
				exp.setCode(Code);
			}
			else if(aux.toString() == "ID"){
				exp.setCode("chamada de funcao");
			}
				
		}
		else if(exp.Children.size() == 6){
			Leaf aux = exp.Children.get(0);
			addreg2(aux);
			Leaf aux2 = exp.Children.get(1);
			addreg2(aux2);
			Leaf aux3 = exp.Children.get(2);
			addreg2(aux3);
			Leaf aux4 = exp.Children.get(3);
			addreg2(aux4);
			Leaf aux5 = exp.Children.get(4);
			addreg2(aux5);
			Leaf aux6 = exp.Children.get(5);
			addreg2(aux6);
		
			if(aux2.getType() != "bool"){
					System.out.println("A cond do while não é bool");
					System.exit(1);
				}

			String label1 = "L" + this.getLabel();
			CounterLabel = CounterLabel + 1;
			String label2 = "L" + this.getLabel();
			CounterLabel = CounterLabel + 1;
			
			String Code = "CJUMP( AND, " + aux2.getCode() + ",  TRUE, " + label1 + ", " + label2 + ")\n " + label1 + ":\n" + aux4.getCode() + "\n"  + label2 +":\n " + aux6.getCode();
			exp.setCode(Code);
			
				
				
		}
		else if(exp.Children.size() == 7){
			Leaf aux = exp.Children.get(0);
			addreg2(aux);
			Leaf aux2 = exp.Children.get(1);
			addreg2(aux2);
			Leaf aux3 = exp.Children.get(2);
			addreg2(aux3);
			Leaf aux4 = exp.Children.get(3);
			addreg2(aux4);
			Leaf aux5 = exp.Children.get(4);
			addreg2(aux5);
			Leaf aux6 = exp.Children.get(5);
			addreg2(aux6);
			Leaf aux7 = exp.Children.get(6);
			addreg2(aux7);
			if(Search(aux.getContent()) == null){
				System.out.println("Este tipo nao foi definido");
				System.exit(1);
			}
			Register Reg = Search(aux.getContent());
			if(VerificaCampos(aux6, Reg, 1) == 0){
				System.out.println("Tipos dentro do Registro não conferem");
				System.exit(1);
			}
			String Code = "MOVE( " + aux3.getCode() + ", " + aux5.getCode() + " )\n" + aux6.getCode();
			exp.setCode(Code);
		}	
		else if(exp.Children.size() == 8){
			Leaf aux = exp.Children.get(0);
			addreg2(aux);
			Leaf aux2 = exp.Children.get(1);
			addreg2(aux2);
			Leaf aux3 = exp.Children.get(2);
			addreg2(aux3);
			Leaf aux4 = exp.Children.get(3);
			addreg2(aux4);
			Leaf aux5 = exp.Children.get(4);
			addreg2(aux5);
			Leaf aux6 = exp.Children.get(5);
			addreg2(aux6);
			Leaf aux7 = exp.Children.get(6);
			addreg2(aux7);
			Leaf aux8 = exp.Children.get(7);
			addreg2(aux8);
			String label1 = "L" + this.getLabel();
			CounterLabel = CounterLabel + 1;
			String label2 = "L" + this.getLabel();
			CounterLabel = CounterLabel + 1;
			if(aux4.getType() != "int"){
				System.out.println("A variavel que itera dentro do for é inteira");
				System.exit(1);
			}
			if(aux6.getType() != "int"){
				System.out.println("A variavel que itera dentro do for é inteira");
				System.exit(1);
			}
			completeBreak(aux8, label2);
			

			Register id = Search(aux2.getContent());

	//		if(id.getType() != aux4.getType()){
	//			System.out.println("Error na atribuicao");
	//			System.exit(1);
	//		}

			if(id == null){
				System.out.println("nao declarado");
				System.exit(1);
			}
			
			String Code = "MOVE(   TEMP " +  id.getTemp() + ", " + aux4.getCode() +" )\n";
			Code = Code + label1 +":\n" + "BINOP( PLUS, TEMP" + id.getTemp() + ", CONST 1)\n"; 
			Code = Code + aux6.getCode() + "\nJUMP( " + "BINOP( SMALLER, TEMP" + id.getTemp() + ", " + label1 + ")\n" + label2 + ":\n";
			exp.setCode(Code);

		}



	}

	public int VerificaCampos(Leaf idexps, Register Reg, Integer Nargs){
			if(idexps.getContent() == "NULL"){
				return 1;
			}
			Leaf aux = idexps.Children.get(4); //pega o exp dentro de idexps que igualara ao ID
			Leaf aux2 = idexps.Children.get(2);
			String typeid  = Reg.getRecordByName(aux2.getContent());
			if(typeid == null){
				
				System.out.println("invalido dentro do registro");
				System.exit(1);
				return 0;
			}
			if(aux.getType() == Reg.getRecordByName(aux2.getContent())){
				return VerificaCampos(idexps.Children.get(5), Reg, Nargs+1);
			}
			else{
				System.out.println("invalido dentro do registro");
				System.exit(1);

				return 0;
			}


	}

	public void idexps (Leaf idexps){
		if(idexps.Children.size() == 1){
			idexps.setContent("NULL");
			idexps.setCode("");
		}
		else{
			Leaf aux = idexps.Children.get(0);
			addreg2(aux);
			Leaf aux2 = idexps.Children.get(1);
			addreg2(aux2);
			Leaf aux3 = idexps.Children.get(2);
			addreg2(aux3);
			Leaf aux4 = idexps.Children.get(3);
			addreg2(aux4);
			Leaf aux5 = idexps.Children.get(4);
			addreg2(aux5);
			String Code = "MOVE( " + aux2.getCode() + ", " + aux4.getCode() +" )\n" + aux5.getCode(); 
			idexps.setCode(Code);
		}
	}

	public void decs(Leaf decs){
		if(decs.Children.size() == 5){
			Leaf aux = decs.Children.get(0);
			addreg2(aux);	
			if(aux.toString() == "TYPE"){
				Leaf aux2 = decs.Children.get(1);
				addreg2(aux2);
				Leaf aux3 = decs.Children.get(2);
				addreg2(aux3);
				Leaf aux4 = decs.Children.get(3);
				Register Insertion = new Register(aux4.getContent(), "0");
				ty(aux4, Insertion);
				Leaf aux5 = decs.Children.get(4);
				addreg2(aux5);
				aux.setCode(aux5.getCode());
			}
			else if(aux.toString() == "VAR"){
				Leaf aux2 = decs.Children.get(1);
				//addreg2(aux2);
				Leaf aux3 = decs.Children.get(2);
				addreg2(aux3);
				Leaf aux4 = decs.Children.get(3);
				addreg2(aux4);
				Leaf aux5 = decs.Children.get(4);
				if(aux5.toString() != "EMPTY"){
					//addreg2(aux5);
				}
				//aux.setCode(aux4.getCode());
				//aux2.setType(aux4.getType());

				Register Content = Search(aux2.getContent());
				String NTemp;
				if(Content == null){
					Register Insertion = new Register(aux2.getContent(), aux2.getType());
					NTemp = "T" + CounterTemp;
					CounterTemp = CounterTemp + 1;
					Insertion.setTemp(NTemp);
					RealTable.add(Insertion);
				}
				else{
					NTemp = Content.getTemp();
				}
				System.out.println("A variavel " + aux2.getContent() + " é " + NTemp);
				decs.setCode("MOVE(TEMP " + NTemp + ", " + aux4.getCode() + ")");	
			}

			
		}
		else if(decs.Children.size() == 7){
			Leaf aux = decs.Children.get(0);
			addreg2(aux);
			Leaf aux2 = decs.Children.get(1);
			addreg2(aux2);
			Leaf aux3 = decs.Children.get(2);
			addreg2(aux3);
			Leaf aux4 = decs.Children.get(3);
			addreg2(aux4);
			Leaf aux5 = decs.Children.get(4);
			addreg2(aux5);
			Leaf aux6 = decs.Children.get(5);
			addreg2(aux6);
			Leaf aux7 = decs.Children.get(6);
			addreg2(aux7);
			Register Insertion = new Register(aux2.getContent(), aux4.getContent());
			String NTemp = "T" + CounterTemp;
			CounterTemp = CounterTemp + 1;
			Insertion.setTemp(NTemp);
			decs.setCode("MOVE(" + NTemp+ ", " + aux6.getCode() + ")");	
		}
		else if(decs.Children.size() == 1){
			Leaf aux = decs.Children.get(0);
			addreg2(aux);	
			if(aux.toString() == "EMPTY"){
				//aux.setCode("null"); nao gera nenhum código
			}
		}	
		else if(decs.Children.size() == 8){

		}
		else if(decs.Children.size() == 19){
		}
	}

	public void tyfields(Leaf tyfields, Register Insertion){
		if(tyfields.Children.size() == 1){
			//faz nada, esse campo é vazio
		}
		else if(tyfields.Children.size() == 4){
			Leaf aux = tyfields.Children.get(0);
			addreg2(aux);
			Leaf aux2 = tyfields.Children.get(1);
			addreg2(aux2);
			Leaf aux3 = tyfields.Children.get(2);
			addreg2(aux3);
			Register ulala = Search(aux3.getContent());
			if(ulala == null){
				System.out.println("O tipo"+  aux3.getContent() +"nao foi declarado");
				System.exit(1);
			}
			Insertion.addRecord(aux.getContent(), aux3.getContent());
			Leaf aux4 = tyfields.Children.get(3);
			tyfields1(aux4, Insertion);
		}
	}


	public void expseq(Leaf expseq){
			Leaf aux = expseq.Children.get(0);
			addreg2(aux);
			Leaf aux2 = expseq.Children.get(1);
			addreg2(aux2);
			expseq.setCode(aux.getCode() + "\n"+ aux2.getCode());
	}


	public void ty(Leaf ty, Register Insertion){
		if(ty.Children.size() == 1){
			Leaf aux = ty.Children.get(0);
			addreg2(aux);//setara para Name
			ty.setContent(aux.getContent());
			if(Search(aux.getContent()) == null){
				System.out.println("O tipo não" + aux.getContent() + "foi definido");
				System.exit(1);
			}
			Register ulala = Search(aux.getContent());
			Insertion.setType(ulala.getType());
			RealTable.add(Insertion);		
		}
		else if(ty.Children.size() == 2){
			Leaf aux = ty.Children.get(0);
			addreg2(aux);//setara para Name
			Leaf aux2 = ty.Children.get(1);
			addreg2(aux);//setara para Name
			if(Search(aux2.getContent()) == null){
				System.out.println("O tipo não" + aux2.getContent() + " foi declarado\n");
				System.out.println("CU");
				System.exit(1);
			}
			Insertion.setType("array of" + aux2.getContent());
			RealTable.add(Insertion);
		}
		else if(ty.Children.size() == 6){
			Leaf aux = ty.Children.get(0);
			addreg2(aux);
			Leaf aux2 = ty.Children.get(1);
			addreg2(aux2);
			Leaf aux3 = ty.Children.get(2);
			addreg2(aux3);
			Leaf aux4 = ty.Children.get(3);
			addreg2(aux4);
			Leaf aux6 = ty.Children.get(5);
			addreg2(aux6);
			if(Search(aux4.getContent()) == null){
				System.out.println("O tipo não" + aux4.getContent() + " foi declarado\n");
				System.exit(1);
			}
			Insertion.setType("Register");
			Insertion.addRecord(aux2.getContent(), aux4.getContent());
			Leaf aux5 = ty.Children.get(4);
			tyfields1(aux5, Insertion);
			
		}	
	}

	public void tyfields1(Leaf tyfields1, Register Insertion){
		if(tyfields1.toString() == "EMPTY"){
			RealTable.add(Insertion);
		}
		else{	
			Leaf aux2 = tyfields1.Children.get(1);
			addreg2(aux2);
			Leaf aux4 = tyfields1.Children.get(3);
			addreg2(aux4);
			Leaf aux5 = tyfields1.Children.get(4);
			addreg2(aux5);
			Insertion.addRecord(aux2.getContent(), aux4.getContent());
			tyfields1(aux5, Insertion);
		}
	}
	
	
	public void typeid(Leaf typeid){
		Leaf aux = typeid.Children.get(0);
		typeid.setContent(aux.getContent());
		typeid.setType("type");
	}

	public void letexp(Leaf letexp){
		Leaf aux2 = letexp.Children.get(1);
		addreg2(aux2);
		Leaf aux4 = letexp.Children.get(3);
		addreg2(aux4);
		String Code = aux2.getCode() +"\n" + aux4.getCode();
		letexp.setCode(Code);
	//	System.out.println(letexp.getCode());
	//	System.out.println(aux2.getCode());
	//	System.out.println(aux4.getCode());
	}
	

	public void ID (Leaf ID){
		ID.setType("NAME");	
	//	System.out.println(ID.getContent());
	}
}
