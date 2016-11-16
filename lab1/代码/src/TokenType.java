/**
 * Created by disinuo on 2016/10/22.
 */

public enum TokenType {
    NUMBER(1),
    ID(2),
    SEPERATOR(3),//分隔符     ;  {  }  ,  .  [  ]  (  )  :
    RELOP(4), //比较符         > < == >= <= <> !=
    OPERATOR(5),//运算符      + - * / = | & ! ? || && ++ -- += *=  /= *=
    UN_KNOW(6),

//保留字们。。。************************************
IF(0),ELSE(0),WHILE(0),DO(0),CASE(0),SWITCH(0),DEFAULT(0),FOR(0),RETURN(0),CLASS(0),INT(0),CHAR(0),DOUBLE(0),FLOAT(0),LONG(0),SHORT(0),STRING(0),BOOLEAN(0),PRIVATE(0),PUBLIC(0),STATIC(0),IMPORT(0),PACKAGE(0),TRUE(0),FALSE(0),BREAK(0),CONTINUE(0),TRY(0),CATCH(0),FINAL(0),FINALLY(0),THROWS(0),ABSTRACT(0),ASSERT(0),BYTE(0),CONST(0),ENUM(0),EXTENDS(0),IMPLEMENTS(0),NEW(0),THIS(0),VOID(0);
    public static TokenType tokenType(String str){
        for(TokenType type:TokenType.values()){
            if(type.toString().toLowerCase().equals(str)){
                return type;
            }
        }
        return UN_KNOW;
    }
    TokenType(int code){
        this.code=code;
    }
    int code=0;
    int getCode(){
        return code;
    }

    public String toPrint() {
        switch (code){
            case 0:return "保留字"+this.toString();
            case 1:return "数字";
            case 2:return "ID";
            case 3:return "分隔符";
            case 4:return "比较符";
            case 5:return "运算符";
            case 6:return "未知token";
        }
        return this.toString();
    }
}