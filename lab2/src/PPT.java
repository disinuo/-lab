/**
 * Created by disinuo on 16/11/16.
 */
public class PPT {
    private int table[][];
    int numOfNT=0;
    int numOfT=0;
    String[] nonT;
    String[] t;
    public PPT(String[] nonT,String[] t){
        this.nonT=nonT;
        this.t=t;
        numOfNT=nonT.length;
        numOfT=t.length;
        table=new int[numOfNT][numOfT];
        for(int i=0;i<numOfNT;i++){
            for (int j=0;j<numOfT;j++){
                table[i][j]=-1;
            }
        }

    }
    public void set(String nt,String t, int num){

        int ntNum=stringToNum(nt);
        int tNum=stringToNum(t);
        if( table[ntNum][tNum]==-1)
        table[ntNum][tNum]=num;
    }
    public int get(String nt,String t){

        int ntNum=stringToNum(nt);
        int tNum=stringToNum(t);
        if(ntNum>=0&&tNum>=0)
            return table[ntNum][tNum];
        return -1;
    }
    private int stringToNum(String str){
        for(int i=0;i<numOfT;i++){
            if(t[i].equals(str.toLowerCase())) return i;
        }
        for (int i=0;i<numOfNT;i++){
            if(nonT[i].equals(str)) return i;
        }

        return -1;
    }
    public int[][] getTable(){
        return table;
    }
}
