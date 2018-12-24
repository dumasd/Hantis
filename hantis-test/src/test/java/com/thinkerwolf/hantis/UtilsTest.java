package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.common.io.Resources;
import com.thinkerwolf.hantis.common.io.URLResource;
import com.thinkerwolf.hantis.common.util.ClassUtils;
import com.thinkerwolf.hantis.common.util.PropertyUtils;
import com.thinkerwolf.hantis.example.Blog;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

public class UtilsTest {

    @Test
    public void propertyUtil() {

        Blog blog = new Blog();

        Properties props = new Properties();
        props.put("id", 1);
        props.setProperty("title", "google");
        props.setProperty("contentccc", "555");

        PropertyUtils.setProperties(blog, props);

        System.out.println(blog);
    }

    @Test
    public void resources() {
        Resource r = Resources.getResource("https://read.douban.com/");
        try {
            r.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            URLResource urlResource = new URLResource(new URL("file:C:/Java/jdk1.8/THIRDPARTYLICENSEREADME.txt"));
            urlResource.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            URLResource urlResource = new URLResource(new URL("https://read.douban.com/"));
            urlResource.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File("src/main/resources/jdbc-config.xml");
        System.out.println(file.exists());
    }

    @Test
    public void classUtil() throws Exception {
        System.out.println("================= classUtil ==================");
        System.out.println(ClassUtils.scanClasses("com.thinkerwolf.*"));
    }

    @Test
    public void resourcePathResolve() throws IOException {
        System.out.println("====================== resourcePathResolve ====================");

        String path = "classpath:*/*.xml";
        // System.out.println(Resources.getRootDir(path));
        // System.out.println(Arrays.toString(Resources.resolvePath(path)));

        Pattern p = Pattern.compile("E:/workspace/Hantis/logs/.*");
        System.out.println(p.matcher("E:/workspace/Hantis/logs/JDBC/BlogSql.xml").matches());
        // File file = new File("E:/workspace/Hantis/logs");
        // System.out.println(ResourceUtils.findFilePaths(file));

        // System.out.println(ResourceUtils.findClasspathFilePaths("", "xml"));

        // System.out.println(Arrays.toString(Resources.resolvePath("classpath:*Sqls.xml")));

        System.out.println(Arrays.toString(Resources.getClasspathResources("*Sqls.xml")));

        // System.out.println(Arrays.toString(Resources.getFileSystemResources("E:/workspace/Hantis/logs/*")));

        System.out.println(Arrays.toString(Resources.getResources("src/main/resources/*")));

        System.out.println(Arrays.toString(Resources.getResources("classpath:*.xml")));

        System.out.println(int[].class.getName());
        System.out.println(String.class.getComponentType().getName());
        // System.out.println(Arrays.toString(Resources.getResources("https://read.douban.com/")));
    }

}
