import java.util.ArrayList;

/**
 * Created by disinuo on 2016/10/22.
 */
public class Main {
    public static void main(String[] args) {
        ArrayList<String> input = IOHelper.getInputWord("src/file/input.txt");
        LexAnalyser analyser=new LexAnalyser(input);
    }

}
