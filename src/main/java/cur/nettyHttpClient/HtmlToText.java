package cur.nettyHttpClient;

/**
 * @author created by WBC
 * @date 2020/12/4
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlToText {
    //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
    private static String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
    //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
    private static String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
    //定义HTML标签的正则表达式
    private static String regEx_html = "<[^>]+>";
    //定义注释信息的正则表达式
    private static String regEx_others = "/\\*[\\s\\S]*\\*/";
    //定义中文的正则表达式
    private static String regEX_mandarin = "[\u4e00-\u9fa5]";
    //定义空格的正则表达式
    private static String regEx_space= "[ ]+";

    /**Html信息转换为Text信息方法
     * @param htmlStr String
     * @return String
     * @throws Exception
     */
    public static String regexHtmlToText(String htmlStr) throws Exception {
        String textStr = htmlStr;
        Pattern myPattern;
        Matcher myMatcher;
        //过滤script标签
        myPattern = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        myMatcher = myPattern.matcher(textStr);
        textStr = myMatcher.replaceAll(" ");
        //过滤style标签
        myPattern = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        myMatcher = myPattern.matcher(textStr);
        textStr = myMatcher.replaceAll(" ");
        //过滤html标签
        myPattern = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        myMatcher = myPattern.matcher(textStr);
        textStr = myMatcher.replaceAll(" ");
        //过滤中文信息
        myPattern = Pattern.compile(regEX_mandarin, Pattern.CASE_INSENSITIVE);
        myMatcher = myPattern.matcher(textStr);
        textStr = myMatcher.replaceAll(" ");
        //过滤注释信息
        myPattern = Pattern.compile(regEx_others, Pattern.CASE_INSENSITIVE);
        myMatcher = myPattern.matcher(textStr);
        textStr = myMatcher.replaceAll(" ");

        textStr = textStr.replaceAll("39;","'");
        textStr = textStr.replaceAll("[ ]+", " ");
        textStr = textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", " ");
        textStr = textStr.replaceAll("\t", "");
        textStr = textStr
                .replaceAll("&nbsp;", " ")
                .replace("&gt;", " ")
                .replace("&mdash;", " ")
                .replace("&#","");
        textStr = textStr.replaceAll("","");
        textStr = textStr.replaceAll("\\\\", " ");
        textStr = textStr.replaceAll("\r\n", " ");
        textStr = textStr.replaceAll("\n", " ");
        myPattern = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        myMatcher = myPattern.matcher(textStr);
        textStr = myMatcher.replaceAll(" ");
        textStr = textStr.trim();
        return textStr;
    }
}