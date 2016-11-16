/**
 * Created by disinuo on 2016/10/22.
 */
public class Token {
    private TokenType type;
    private String value="";

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }
    public Token(TokenType type) {
        this.type = type;
    }
    public String print(){
        if(type==TokenType.UN_KNOW) return ("Error:没识别出来这是个啥哎。。："+value);
        else return ("<    "+type.toPrint()+"  ,   "+value+"    >");
    }
}
