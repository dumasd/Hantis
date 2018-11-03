package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.sql.Sql;
import com.thinkerwolf.hantis.sql.SqlNode;
import com.thinkerwolf.hantis.sql.xml.XmlSqlNodeParser;
import ognl.OgnlException;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 如何分段获取标签的内容
 *
 * @author wukai
 */
public class SqlParseTest {


    @Test
    public void sqlRegexTest() throws OgnlException {
        System.out.println("===================== sqlRegex ================");
        String s = "\n\t where id = #{ id } and title = #{ title } and content = #{content} \n\t\t";
        Pattern p = Pattern.compile("#\\s*\\{\\s*[^#\\{\\}]*\\s*\\}");
        Pattern p1 = Pattern.compile("[^#\\s\\{\\}]+");
        Pattern p2 = Pattern.compile("[\\n\\t\\s\\f\\r]*");
        Matcher m = p.matcher(s);
        Map<String, Object> paramterMap = new HashMap<>();
        paramterMap.put("id", 3);
        paramterMap.put("title", "Oracle");
        paramterMap.put("content", "Pettern");
        while (m.find()) {
            m.start();
            m.end();
            String ss = m.group();
            Matcher ssMatcher = p1.matcher(ss);
            ssMatcher.find();
            System.out.println("start#" + m.start() + ", end#" + m.end() + "..." + ssMatcher.group());
            // System.out.println(
            // "start#" + m.start() + ", end#" + m.end() + "..." +
            // Ognl.getValue(ssMatcher.group(), paramterMap));
        }
        // System.out.println(Arrays.toString(p.split("\n\tselect * from blog
        // \n\t\t")));
        System.out.println(p2.matcher("\n\t\r \t").matches());
        System.out.println(p2.matcher("5234").matches());
        System.out.println("\n\r34234df\t".replaceAll(p2.pattern(), ""));
    }

    @Test
    public void xmlSqlNodeParser() throws Throwable {
        System.out.println("===================== xmlSqlNodeParser ================");
        String path = "sqls/BlogSqls.xml";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        XmlSqlNodeParser xsp = new XmlSqlNodeParser();
        Map<String, SqlNode> sqlNodeMap = xsp.parse(is);
        SqlNode sqlNode = sqlNodeMap.get("tableBlog.selectOne");
        Map<String, Object> paramterMap = new HashMap<>();
        paramterMap.put("id", 3);
        paramterMap.put("title", "jta_6");
        //paramterMap.put("content", "jta_6_test");

        Sql sql = new Sql(paramterMap);
        sqlNode.generate(sql);
        System.out.println(sql);
    }

}
