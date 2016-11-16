import java.util.ArrayList;

/**
 * Created by disinuo on 2016/10/22.
 */
public class LexAnalyser {
    int state=0;
    private ArrayList<Token> tokens;
    private ArrayList<String> keyWordsList=new ArrayList<String>();

    public LexAnalyser(ArrayList<String> input) {
//        ------initialize------------------
        tokens=new ArrayList<Token>();
//        初始化保留字列表~从TokenType读取
        for (TokenType tokenType:TokenType.values()){
            if(tokenType.getCode()==0) keyWordsList.add(tokenType.toString().toLowerCase());
        }
        for(String str:input){
            analyse(str);
        }
//        打印token序列
        System.out.println("下面输出解析出的token序列，格式为： <  类型 ,   值   >,且该结果会存在file文件夹下的output.txt文件里");
        String ans_str="";
        for(Token token:tokens){
            ans_str+=token.print()+"\n";
        }
        System.out.print(ans_str);
        IOHelper.saveFile("src/file/output.txt",ans_str);
    }
    private void analyse(String word){
        int i=0;
        String charArray="";
        char c=word.charAt(0);
//------------------保留字&字母开头的id（保留字优先）-------------------------------------------------------------
        if(isLetter(c)){
loop:           while (isLetter(c)||isDigit(c)||c=='_'||c=='$'){
                charArray+=c;
                for(String keyword:keyWordsList){
                    if(charArray.equals(keyword)){//下一个字符不能是字母或数字
                        i++;
                        //匹配了，但下一个是数字or字母，则继续向下扫描，匹配。。。因为可能有final和finally都是保留字这种情况
                        if(i<word.length()&&(isLetter(word.charAt(i))||isDigit(word.charAt(i)))){
                            i--;
                            break;
                        }else{//这是个保留字！
                            i--;
                            TokenType type=TokenType.tokenType(keyword);
                            tokenAdder(type,"");
                            charArray="";
                            if(word.length()>keyword.length()){
                                analyse(word.substring(keyword.length()));
                            }
                            break loop;
                        }
                    }
                }
                i++;
                if(i>=word.length()) break;
                c=word.charAt(i);
            }
            if(charArray.length()>0){
                tokenAdder(TokenType.ID,charArray);
                if(word.length()>i){
                    analyse(word.substring(i));
                }
            }
// --------------非字母开头的ID----------------------------------------------------------
        }else if(c=='_'||c=='$'){
            while (isLetter(c)||isDigit(c)||c=='_'||c=='$'){
                charArray+=c;
                i++;
                if(i>=word.length()) break;
                c=word.charAt(i);
            }
            tokenAdder(TokenType.ID,charArray);
            if(word.length()>charArray.length()){
                analyse(word.substring(charArray.length()));
            }
// ------------------分隔符：    ;#  {#  }#  ,#  .#  [#  ]#  (#  )#  :# ------------------------------------------
        }else if(isIsolator(c)){
            tokenAdder(TokenType.SEPERATOR,c+"");
            if(word.length()>1) analyse(word.substring(1));
//      -----------------digit开头的number------------------------------------------
        }else if(isDigit(c)){
            int state=2;
            String str=numberAnalyser(word,state);
            if(str.length()>0) analyse(str);
//     -------------- +或-开头的number ，以及运算符 +,-,*,/,++，--,+=，-=,*=,/=  --------------------------------------------------
        }else if(c=='-'||c=='+'||c=='*'||c=='/'){
            if(word.length()==1){
                tokenAdder(TokenType.OPERATOR,c+"");
            }else {
                char charBefore=c;
                c=word.charAt(++i);
                if(c==charBefore||c=='='){
                    tokenAdder(TokenType.OPERATOR,word.substring(0,2));
                    if(word.length()>2) analyse(word.substring(2));
                }else if((charBefore=='-'||charBefore=='+')&&isDigit(c)){
                    String str=numberAnalyser(word,0);
                    if(str.length()>0) analyse(str);
                }else{
                    tokenAdder(TokenType.OPERATOR,charBefore+"");
                    analyse(word.substring(1));
                }
            }
//-------------- 识别运算符  ?  %  -------------------------------------------
        }else if(c=='%'||c=='?'){
            tokenAdder(TokenType.OPERATOR,c+"");
            if(word.length()>1) analyse(word.substring(1));
//     ----------运算符 = 和 比较符 ==   ---------------------------------------
        }else if(c=='='){
            Token token;
            if(word.length()==1){
                tokenAdder(TokenType.OPERATOR,"=");
            }else{
                c=word.charAt(++i);
                if(c=='='){
                    tokenAdder(TokenType.RELOP,"==");
                    if(word.length()>2) analyse(word.substring(2));
                }else{
                    tokenAdder(TokenType.OPERATOR,"=");
                    analyse(word.substring(1));
                }
            }
//    -----------运算符|,&,|| ,&& ---------------------------------------------------
        }else if(c=='|'||c=='&'){
            Token token;
            char charBefore=c;
            if(word.length()==1){
                tokenAdder(TokenType.OPERATOR,c+"");
            }else{
                c=word.charAt(++i);
                if(c==charBefore){
                    tokenAdder(TokenType.OPERATOR,word.substring(0,2));
                    if(word.length()>2) analyse(word.substring(2));
                }else{
                    tokenAdder(TokenType.OPERATOR,charBefore+"");
                    analyse(word.substring(1));
                }
            }
//  ----------------比较符  <,<=,<> 和 运算符<<
        }else if(c=='<'){
            Token token;
            if(word.length()==1){
                tokenAdder(TokenType.RELOP,c+"");
            }else{
                c=word.charAt(++i);
                if(c=='<'){
                    tokenAdder(TokenType.OPERATOR,"<<");
                    if(word.length()>2) analyse(word.substring(2));
                }else if(c=='='||c=='>'){
                    tokenAdder(TokenType.RELOP,word.substring(0,2));
                    if(word.length()>2) analyse(word.substring(2));
                }else{
                    tokenAdder(TokenType.RELOP,"<");
                    analyse(word.substring(1));
                }
            }
//  ----------------比较符  >,>=, 和 运算符>>
        }else if(c=='>'){
            Token token;
            if(word.length()==1){
                tokenAdder(TokenType.RELOP,c+"");
            }else{
                c=word.charAt(++i);
                if(c=='>'){
                    tokenAdder(TokenType.OPERATOR,">>");
                    if(word.length()>2) analyse(word.substring(2));
                }else if(c=='='){
                    tokenAdder(TokenType.RELOP,">=");
                    if(word.length()>2) analyse(word.substring(2));
                }else{
                    tokenAdder(TokenType.RELOP,">");
                    analyse(word.substring(1));
                }
            }
        }else if(c=='!'){
            Token token;
            if(word.length()==1){
                tokenAdder(TokenType.OPERATOR,c+"");
            }else{
                c=word.charAt(++i);
                if(c=='=') {
                    tokenAdder(TokenType.RELOP, "!=");
                    if (word.length() > 2) analyse(word.substring(2));
                }else {
                    tokenAdder(TokenType.OPERATOR,"!");
                    analyse(word.substring(1));
                }
            }
        }
        else {//本词法分析器不认识了。。。
            tokenAdder(TokenType.UN_KNOW,c+"");
            if(word.length()>1) analyse(word.substring(1));
        }
    }

    /**
     * 检查数字的
     * @param word,st
     * @return
     */
    private String numberAnalyser(String word,int st){
        int state=st;
        int length=word.length();
        int i=0;
        String charArray="";
        char c=word.charAt(0);
loop1:  while (state<8){
            switch (state){
                case 0:
                case 1:if(++i<length){
                           c=word.charAt(i);
                           if(isDigit(c)) state=2;
                           else state=10;
                        }else state=10;
                    break ;
                case 2:if(++i<length){
                            c=word.charAt(i);
                            while (isDigit(c)){
                                c=word.charAt(++i);
                            }
                            if(c=='.') state=3;
                            else if(c=='e'||c=='E') state=5;
                            else state=8;
                        }else state=8;
                    break ;
                case 3:if(++i<length){
                            c=word.charAt(i);
                            if(isDigit(c)) state=4;
                            else state=9;
                        }else state=9;
                    break ;
                case 4:if(++i<length){
                            c=word.charAt(i);
                            while (isDigit(c)){
                                c=word.charAt(++i);
                            }
                            if(c=='e'||c=='E') state=5;
                            else state=8;
                        }else state=8;
                    break ;
                case 5:if(++i<length){
                            c=word.charAt(i);
                            if(isDigit(c)) state=7;
                            else if(c=='+'||c=='-') state=6;
                            else state=9;
                        }else state=9;
                    break ;
                case 6:if(++i<length){
                            c=word.charAt(i);
                            if(isDigit(c)) state=7;
                            else state=9;
                        }else state=9;
                    break ;
                case 7:if(++i<length){
                            c=word.charAt(i);
                            while (isDigit(c)){
                                c=word.charAt(++i);
                            }
                            state=8;
                        }else state=8;
                    break ;
            }
        }
        if(state==8){
            String ans=word.substring(0,i);
            Token token=new Token(TokenType.NUMBER,ans);
            tokens.add(token);
            if(i<word.length()) return word.substring(i);
            else return "";
        }
        else if(state==9){
            Token token = new Token(TokenType.UN_KNOW,"未输完的数字："+word);
            tokens.add(token);
            return "";
        }else if(state==10){
            return word;
        }
        return "";
    }

    /**
     * 检查运算符的
     * @param word
     * @return
     */
    private String operatorAnalyser(String word) {
       return "";
    }


    private String isolateAnalyser(String word) {
        return "";
    }


    private boolean isDigit(char c){
        if('0'<=c&&c<='9') return true;
        else return false;
    }
    private boolean isLetter(char c){
        if(('a'<=c&&c<='z')||('A'<=c&&c<='Z')) return true;
        else return false;
    }
    private boolean isIsolator(char c){
        switch (c){
            case ';':
            case ',':
            case '.':
            case '{':
            case '}':
            case '[':
            case ']':
            case '(':
            case ')':
            case ':':
            case '\\':
            case '\'':
            case '"':
                return true;
        }
        return false;
    }
    private void tokenAdder(TokenType type,String value){
        Token token=new Token(type,value);
        tokens.add(token);
    }
}

