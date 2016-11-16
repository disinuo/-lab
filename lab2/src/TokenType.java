/**
 * Created by disinuo on 2016/11/15.
 */

public enum TokenType {
    NUMBER(1,"number"),
    ID(2,"id"),
    BRACE_L(3,"{"),//    {
    BRACE_R(4,"}"),//   }
    BRACKET_L(5,"("),//  (
    BRACKET_R(6,")"),// )

    SEMICOLON(7,";"),//     ;
    AND(8,"and"),//           &&,&
    OR(9,"or"),//            ||,|
    RELOP(10,"relop"),//比较符   > < == >= <= <> !=
    ADD(11,"add"),//          +
    MINUS(12,"-"),//        -
    MULTIPLY(13,"multiply"),//      *
    DIVIDE(14,"/"),//       /
    ASSIGN(15,"="),//       =
    UN_KNOW(16,""),
    $(17,"$"),//一个终结符！
//保留字们。。。************************************
IF(0,"if"),ELSE(0,"else"),WHILE(0,"while"),DO(0,""),CASE(0,""),SWITCH(0,""),DEFAULT(0,""),FOR(0,""),RETURN(0,""),CLASS(0,""),INT(0,""),CHAR(0,""),DOUBLE(0,""),FLOAT(0,""),LONG(0,""),SHORT(0,""),STRING(0,""),BOOLEAN(0,""),PRIVATE(0,""),PUBLIC(0,""),STATIC(0,""),IMPORT(0,""),PACKAGE(0,""),TRUE(0,""),FALSE(0,""),BREAK(0,""),CONTINUE(0,""),TRY(0,""),CATCH(0,""),FINAL(0,""),FINALLY(0,""),THROWS(0,""),ABSTRACT(0,""),ASSERT(0,""),BYTE(0,""),CONST(0,""),ENUM(0,""),EXTENDS(0,""),IMPLEMENTS(0,""),NEW(0,""),THIS(0,""),VOID(0,"");
    public static TokenType toTokenType(String str){
        for(TokenType type:TokenType.values()){
            if(type.toString().equals(str.toUpperCase())||type.value.equals(str)){
                return type;
            }
        }
        return UN_KNOW;
    }
    TokenType(int code,String value){
        this.code=code;
        this.value=value;
    }
    int code=0;
    String value="";
    int getCode(){
        return code;
    }
    String getValue(){
        return value;
    }

    public String toPrint() {
        switch (code){
            case 0:return "保留字"+this.toString();
            case 1:return "数字";
            case 2:return "ID";
            case 3:return "大括号(左)";
            case 4:return "大括号(右)";
            case 5:return "小括号(左)";
            case 6:return "小括号(右)";
            case 7:return "分号";
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:return this.toString();
            case 10:return "比较符";
            case 15:return "赋值符";
            case 16:return "未知token";
        }
        return this.toString();
    }
}