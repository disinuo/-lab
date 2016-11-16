import java.util.ArrayList;
/**
 * Created by disinuo on 16/11/15.
 */
public class GrammarAnalyser {
    ArrayList<Token> tokens;//token序列
    LexAnalyser analyser;
    int numOfCFG;           //文法的 产生式个数
    int numOfNT;            //非终结符个数
    int numOfT;             //终结符个数
    Grammar CFG[];          //文法的所有产生式
    NonTerminal non_T[];         //所有 非终结符
    String T[];             //所有 终结符
    PPT ppt;            //预测分析表，行是非终结符，列是终结符
    ArrayList<Grammar> outputProduction;
    public GrammarAnalyser(ArrayList<String> input,
                           String grammars[][], int numOfCFG,
                           String non_T[],
                           String T[]){
        this.numOfCFG=numOfCFG;
        this.analyser=new LexAnalyser(input);
        ppt = new PPT(non_T,T);
        this.T=T;
        numOfT=T.length;
        numOfNT=non_T.length;
        outputProduction=new ArrayList<Grammar>();
        init(grammars,non_T);
        int result=analyse();
        if(result<0){System.out.println("ERROR!");}
        else {
            String ans_str="";
            for(Grammar g:outputProduction){
               ans_str+=g.getLeft()+"-->"+g.getRightStr()+"\n";
            }
            System.out.print(ans_str);
//            存储LL1 推导过程序列
            int saveResult=IOHelper.saveFile("output_production.txt",ans_str);
            if(saveResult==0)System.out.println("token序列存储失败！");
        }
    }
    public void init(String grammars[][],String nonT[]){
        tokens=analyser.getTokens();
        tokens.add(new Token(TokenType.$));
        non_T=new NonTerminal[numOfNT];
        CFG=new Grammar[numOfCFG];
        Grammar g;
        NonTerminal nt;
        for(int i=0;i<numOfCFG;i++){
            CFG[i]=new Grammar(grammars[i][0],grammars[i][1].split(" "));

        }
        for (int i=0;i<numOfNT;i++){
            nt=new NonTerminal(nonT[i]);
            this.non_T[i]=nt;
        }
    }

    private int analyse(){
        first();
        int followNum[]=new int[numOfNT];
loop:   while (true){
           for(int i=0;i<numOfNT;i++) followNum[i]=non_T[i].getFollowNum();
           follow();
            for(int i=0;i<numOfNT;i++){
                if(non_T[i].getFollowNum()!=followNum[i]) continue loop;
            }
            break ;
        }
//  ------------打印first和follow----------
//        for(NonTerminal nt:non_T){
//            System.out.println(nt.getValue()+": "+"first= "+nt.getFirst()+" follow= "+nt.getFollow());
//        }
//--------------------------------构造PPT----------------------------------------------
        for(int i=0;i<numOfNT;i++){
            NonTerminal nt=non_T[i];
            for(int j=0;j<CFG.length;j++){//第j个产生式
                Grammar cfg=CFG[j];
                if(CFG[j].getLeft().equals(nt.getValue())){
//                    求右式的first
                    String right[]=cfg.getRight();
                    if(isTerminal(right[0])){//是终结符
                        if(right[0].equals(TokenType.$.toString())){
                            for(TokenType tok:toNT(cfg.getLeft()).getFollow()){
                                ppt.set(nt.getValue(),tok.getValue(),j);
                            }
                        }else {
                            ppt.set(nt.getValue(), right[0], j);
                        }
                    }else {
                        int m=0;
                        while (m<right.length&&!isTerminal(right[m])){
                            NonTerminal temp=toNT(right[m]);
                            for (TokenType t:temp.getFirst()){
                                String str=t.getValue();
                                ppt.set(nt.getValue(),str,j);
                            }
                            if(!temp.existNull())break;
                            m++;
                        }
                        if(m<right.length&&isTerminal(right[m])){
                            ppt.set(nt.getValue(),TokenType.toTokenType(right[m]).getValue(),j);
                        }if(m==right.length){
                            for (TokenType t:toNT(cfg.getLeft()).getFollow()){
                                String str=t.getValue();
                                ppt.set(nt.getValue(),str,j);
                            }
                        }
                    }
                }
            }
        }
//------------打印ppt--------------
//        int tt[][]=ppt.getTable();
//        System.out.print("~\t");
//        for(String t:T){
//            System.out.print(t+"__");
//        }
//        System.out.println();
//        int i=0;
//        for(int[] mm:tt){
//            System.out.print(non_T[i].getValue()+"\t");
//            for(int x:mm) System.out.print(x+"\t");
//            System.out.println("\n----------------------------------------------------------------------------------------------");
//            i++;
//        }

//  ---------初始化符号栈---------------------
        ArrayList<String> stack=new ArrayList<String>();
        stack.add("S");
        stack.add("$");
        int n,t=0;
        while (!(stack.get(0).equals("$")&&tokens.get(0).getType()==TokenType.$)){
            TokenType token=tokens.get(0).getType();
            String ele=stack.get(0);
            if(isTerminal(ele)){//栈内是终结符
                if(!ele.equals(token.getValue())){
                    return -1;
                }else {
                    stack.remove(0);
                    tokens.remove(0);
                }
            }else{//栈内是非终结符
                int x=ppt.get(ele,token.getValue());//查表
                if(x<0) return -1;
                outputProduction.add(CFG[x]);//放入输出队列
                stack.remove(0);
                String rr[]=CFG[x].getRight();
                if(rr.length==1&&rr[0].equals("$")){}else {
                    for(int p=0;p< rr.length;p++){//右式入栈
                        stack.add(0+p,rr[p]);
                    }
                }
            }
        }
     return 0;
    }
    private void first(){
        int m=0;
        for(NonTerminal nt:non_T){
            for(Grammar grammar:CFG){
                if(grammar.getLeft().equals(nt.getValue())){//找到了左式与该非终结符相等的产生式
                    String right[]=grammar.getRight();
                    int i=0;
                    if(isTerminal(right[0])){
                        nt.addFirst(TokenType.toTokenType(right[0]));
                    }else{
                        int hasNull=0;
                        while (i < right.length&&!isTerminal(right[i])){
                            NonTerminal temp=toNT(right[i]);
                            ArrayList<TokenType> fir=new ArrayList<TokenType>(temp.getFirst());
                            nt.addFirst(fir);
                            if(!temp.existNull()) break;
                            hasNull=1;
                            i++;
                        }
                        if(i<right.length&&isTerminal(right[i])){
                            nt.addFirst(TokenType.toTokenType(right[i]));
                        }
                        if(hasNull==1){
                            nt.addFirst(TokenType.$);
                        }
                    }
                }
            }

         m++;
        }

    }
    private void follow(){
        for(NonTerminal nt:non_T){
            for(Grammar grammar:CFG){
                String right[]=grammar.getRight();
        loop:   for(int i=0;i<right.length;i++){
                    if(right[i].equals(nt.getValue())){//右式有这个非终结符
                        if((i+1)==right.length){//在末尾
                            ArrayList<TokenType> f=new ArrayList<>(toNT(grammar.getLeft()).getFollow());
                            nt.addFollow(f);
                        }else {
                            i++;
                            String temp=right[i];
                            if(isTerminal(temp)){
                                nt.addFollow(TokenType.toTokenType(temp));
                            }else {
                                while (i < right.length&&!isTerminal(right[i])){
                                    NonTerminal t=toNT(right[i]);
                                    ArrayList<TokenType> te=new ArrayList<>(t.getFirst());
                                    nt.addFollow(te);
                                    if(!t.existNull()) break;
                                    i++;
                                }
                                if(i<right.length&&isTerminal(right[i])){
                                    nt.addFollow(TokenType.toTokenType(right[i]));
                                }if(i==right.length){
                                    ArrayList<TokenType> te=new ArrayList<>(toNT(grammar.getLeft()).getFollow());
                                    nt.addFollow(te);
                                }

                            }
                        }
                    }
                }
            }
        }
    }
    private boolean isTerminal(String value){
        for(String str:T){
            if(str.toLowerCase().equals(value.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    private NonTerminal toNT(String value){
        for(NonTerminal nt:non_T){
            if(nt.getValue().equals(value)) {
                return nt;
            }
        }
        return null;
    }

}
