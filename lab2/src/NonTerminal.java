/**
 * Created by disinuo on 16/11/15.
 */
import java.util.ArrayList;
public class NonTerminal {
    private ArrayList<TokenType> first=new ArrayList<TokenType>();
    private ArrayList<TokenType> follow=new ArrayList<TokenType>();
    private String value="";
//----------------------------constructer----------------------------------------------------
    public NonTerminal(String value) {
        this.follow.add(TokenType.$);
        this.value=value;
    }
// --------------------------------------------------------------------------
    public boolean existNull(){
        for(TokenType fir:first){
            if(fir==TokenType.$) return true;
        }
        return false;
    }
    //------------------add------------------------------------------
    public void addFirst(TokenType key) {
        for(TokenType t:first){
            if(t==key){
                return;
            }
        }
        this.first.add(key);
    }
    public void addFirst(ArrayList<TokenType> keys) {
        for(int i=0;i<keys.size();i++){
            if(keys.get(i)==TokenType.$)
                keys.remove(i);
        }
        keys.removeAll(first);


        first.addAll(keys);
    }
    public void addFollow(TokenType key) {
        for(TokenType t:follow){
            if(t==key){
                return;
            }
        }
        this.follow.add(key);
    }
    public void addFollow(ArrayList<TokenType> keys) {
        keys.removeAll(follow);
        follow.addAll(keys);
    }

    //------------------get----------------------------------------------
    public ArrayList<TokenType> getFirst() {
        return first;
    }
    public ArrayList<TokenType> getFollow() {
        for(TokenType t:follow){
            if(t==TokenType.$) return follow;
        }
        follow.add(TokenType.$);
        return follow;
    }
    public String getValue() {
        return value;
    }
    public int getFollowNum(){
        return follow.size();
    }
}
