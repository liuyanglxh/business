package com.liuyang.common.utils.search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检测并杜绝xss攻击
 */
public class XSSFilter {

    // 定义HTML标签的正则表达式
    private static final Pattern HTML = Pattern.compile("<[^>]+>");

    private static final Pattern XSS_PATTERN = Pattern.compile(
            new StringBuilder()
                    //脚本
                    .append("<script>(.*?)</script>")
                    .append("|javascript:")
                    .append("|vbscript:")
                    .append("|<script(.*?)>")
                    .append("|</script>")
                    .append("|src[\r\n]*=[\r\n]*(\\\'|\\\")(.*?)(\\\'|\\\")")
                    .append("|e­xpression\\((.*?)\\)")
                    .append("|<OBJECT(.*?)>(.*?)</OBJECT>")//ActiveX
                    //HTML事件
                    .append("|onmouseover").append("|onmouseout").append("|onmousedown")
                    .append("|onmouseup").append("|onmousemove").append("|onclick")
                    .append("|ondblclick").append("|onkeypress").append("|onkeydown")
                    .append("|onkeyup").append("|ondragstart").append("|onerrorupdate")
                    .append("|onhelp").append("|onreadystatechange").append("|onrowenter")
                    .append("|onrowexit").append("|onselectstart").append("|onload")
                    .append("|onunload").append("|onbeforeunload").append("|onblur")
                    .append("|onerror").append("|onfocus").append("|onresize")
                    .append("|onscroll").append("|oncontextmenu")
                    //一些JS属性或方法
                    .append("|alert\\((.*?)\\)")
                    .append("|eval\\((.*?)\\)")
                    .toString(),
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public static String hasXSSRisk(String source) {
        if (null != source && source.trim().length() > 0) {
            Matcher matcher = XSS_PATTERN.matcher(source);
            if (matcher.find()) {
                return "存在XSS攻击风险";
            }
        }

        return null;
    }

    /**
     * 是否存在Xss攻击
     *
     * @param source
     * @return
     */
    public static boolean existXSSRisk(String source) {
        if (null != source && source.trim().length() > 0) {
            Matcher matcher = XSS_PATTERN.matcher(source);
            if (matcher.find()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 匹配攻击脚本，并返回匹配的脚本语句
     *
     * @param source
     * @return
     */
    public static String hasXSSRiskCharacter(String source) {
        if (null != source && source.trim().length() > 0) {
            Matcher matcher = XSS_PATTERN.matcher(source);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return null;
    }

    /**
     * 检测并替换掉有xss攻击的脚本
     *
     * @param source
     * @param removeHtmlTag 是否移除html标签
     * @return
     */
    public static String filterXSSSCRIPT(String source, boolean removeHtmlTag) {

        if (null != source && source.trim().length() > 0) {

            // html 标签去掉
            if (removeHtmlTag) {
                Matcher matcher1 = HTML.matcher(source);
                if (matcher1.find()) {
                    source = source.replaceAll(HTML.toString(), "");
                }
            }

            // xss 去掉
            Matcher matcher = XSS_PATTERN.matcher(source);
            if (matcher.find()) {
                // 替换掉
                source = source.replaceAll(XSS_PATTERN.toString(), "");
            }
        }

        return source;
    }
}