/**
 * Created by disinuo on 16/11/15.
 */
public class Grammar {
    private String left;
    private String right[];

    public Grammar(String left, String[] right) {
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public String[] getRight() {
        return right;
    }

    public String getRightStr(){
        String ans="";
        for(String s:right) ans+=s+" ";
        return ans;
    }
}
