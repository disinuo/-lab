import java.util.ArrayList;

/**
 * Created by disinuo on 2016/10/22.
 */
public class Main {
    public static void main(String[] args) {
        String CFG[][] =new String[40][2];
        CFG[0][0]=new String("S");CFG[0][1]=new String("if ( A ) { S } B");
        CFG[1][0]=new String("S");CFG[1][1]=new String("while ( A ) { S }");
        CFG[2][0]=new String("S");CFG[2][1]=new String("id = C ;");
        CFG[3][0]=new String("S");CFG[3][1]=new String("$");

        CFG[4][0]=new String("A");CFG[4][1]=new String("F relop F A'");
        CFG[5][0]=new String("A'");CFG[5][1]=new String("E A'");
        CFG[6][0]=new String("A'");CFG[6][1]=new String("$");
// --------------if,while 判断语句-------------------------------------------------------------
        CFG[7][0]=new String("B");CFG[7][1]=new String("else { S }");
        CFG[8][0]=new String("B");CFG[8][1]=new String("$");

        CFG[9][0]=new String("C");CFG[9][1]=new String("D C'");
        CFG[10][0]=new String("C'");CFG[10][1]=new String("add D C'");//比较符  > < == >= <= <> !=
        CFG[11][0]=new String("C'");CFG[11][1]=new String("$");//比较符  > < == >= <= <> !=

// --------------计算表达式 START -----------------------------------------------------------

        CFG[12][0]=new String("D");CFG[12][1]=new String("F D'");
        CFG[13][0]=new String("D'");CFG[13][1]=new String("multiply F D'");
        CFG[14][0]=new String("D'");CFG[14][1]=new String("$");

        CFG[15][0]=new String("E");CFG[15][1]=new String("and A");
        CFG[16][0]=new String("E");CFG[16][1]=new String("or A");

        CFG[17][0]=new String("F");CFG[17][1]=new String("id");
        CFG[18][0]=new String("F");CFG[18][1]=new String("number");
        String non_terminals[]={"F","E","D'","D","C'","C","B","A'","A","S"};
        String terminals[]={"if","else","while","relop","id","number",
                "{","=","}","(",")",";","$","or","and","add","multiply"};//TODO
// --------------计算表达式 END -----------------------------------------------------------
        ArrayList<String> input = IOHelper.getInputWord("input.txt");
        GrammarAnalyser analyser=new GrammarAnalyser(input,CFG,19,non_terminals,terminals);
    }

}
