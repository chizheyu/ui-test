package reference;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class testRe {

    public static void main(String[] args) {
        //  Auto-generated method stub
        /*
        String candidate =
                 "A Matcher examines the results of applying a pattern.";
                String regex = "\\ba\\w*\\b";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(candidate);
                String val = null;
                System.out.println("INPUT: " + candidate);
                System.out.println("REGEX: " + regex +"\r\n");
                while (m.find()){
                  val = m.group();

                  System.out.println("MATCH: " + val);
                }
                if (val == null) {
                  System.out.println("NO MATCHES: ");
                }
                */
        System.out.println("please choose platform :");
        System.out.println("1. staging ");
        System.out.println("2. production");
        Scanner sc=new Scanner(System.in);

        while(sc.hasNextLine())
        {
            System.out.println("输出："+sc.nextLine());
        }
        int a = 0;
        String tempString = "tt11dfd11项目任务（61）";

        Matcher mat;
        String regEx = "[^0-9]|/";
        String tel[] = tempString.split("（");
        System.out.println(tel[1]);
        System.out.println(regEx);
        Pattern pat = Pattern.compile(regEx);
        mat = pat.matcher(tel[1]);
        System.out.println( Integer.parseInt(mat.replaceFirst("").trim()));
        while (mat.find()){
            System.out.println("MATCH: " + mat.group());
        }

        //System.out.println( Integer.parseInt(mat.replaceFirst("").trim()));


    }

}
