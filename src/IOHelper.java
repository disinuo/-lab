import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by disinuo on 2016/10/22.
 */
public class IOHelper {
    /**
     * 失败返回0，成功返回1
     * @param src
     * @param content
     * @return
     */
    public static int saveFile(String src,String content){
        File file=new File(src);
        if(!file.exists()){
            try {
                file.createNewFile();//不存在的话
            } catch (IOException e) {
                return 0;
            }
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            return 0;
        }
        return 1;
    }
    public static ArrayList<String> getInputWord(String src){
        ArrayList<String> input = new ArrayList<String>();
        Pattern patternHT=Pattern.compile("^( |\t|\n)+|( |\t|\n)+$");
        Pattern patternB=Pattern.compile("( |\t|\n)+");

        File file=new File(src);
        BufferedReader reader=null;
        try {
            reader=new BufferedReader(new FileReader(file));
            String temp=reader.readLine();
            while (temp!=null){
//                去掉头部和尾部的空白 包括空格、/t、/n
                Matcher matcherHT=patternHT.matcher(temp);
                String str_handled=matcherHT.replaceAll("");
                if(str_handled.length()!=0){// 若非空行，将语句中间的每处空白，换成一个空格符
                    Matcher matchB=patternB.matcher(str_handled);
                    str_handled=matchB.replaceAll(" ");
                    String[] str_line=str_handled.split(" ");
                    for(String str:str_line)  input.add(str);
                }
                temp=reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }
    public static ArrayList<String> getInputChar(String src){
        ArrayList<String> input = new ArrayList<String>();
        //TODO
        return input;
    }

}
