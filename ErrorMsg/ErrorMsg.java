package ErrorMsg;

public class ErrorMsg {
  private LineList linePos = new LineList(-1,null);
  private int lineNum=1;
  private String filename;
  public boolean anyErrors;

  public ErrorMsg(String f) {
      filename=f;
  }

  public void newline(int pos) {
     lineNum++;
     linePos = new LineList(pos,linePos);
  }
  public void error(int pos, String msg) {
	int n = lineNum;
        LineList p = linePos;
	String sayPos="0.0";
	String linha="0";
	String caractere="0";

	anyErrors=true;

        while (p!=null) {
          if (p.head<pos) {
	     sayPos = ":" + String.valueOf(n) + "." + String.valueOf(pos-p.head);
	     linha = String.valueOf(n);
	     caractere = String.valueOf(pos-p.head);
	     break;
          }
	  p=p.tail; n--;
        }

	System.out.println("===============================================================================");
	System.out.println(" -- " + msg + " --");
	System.out.println(" Nome do arquivo:" + filename);
	System.out.println(" Numero da Linha: " + linha);
	System.out.println(" Caractere: " + caractere);
	System.out.println("===============================================================================");
  }
}

class LineList {
  int head;
  LineList tail;
  LineList(int h, LineList t) {head=h; tail=t;}
}
