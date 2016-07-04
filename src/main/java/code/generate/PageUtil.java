package code.generate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PageUtil {

    private String pageListPackage;
    private String pageListClassName;
    private String pageQueryPackage;
    private String pageQueryClassName;
    private String searchMapClassName;

    public void setPageListPackage(String pageListPackage)
    {
        this.pageListPackage = pageListPackage;
    }

    public void setPageListClassName(String pageListClassName)
    {
        this.pageListClassName = pageListClassName;
    }

    public void setPageQueryPackage(String pageQueryPackage)
    {
        this.pageQueryPackage = pageQueryPackage;
    }

    public void setPageQueryClassName(String pageQueryClassName)
    {
        this.pageQueryClassName = pageQueryClassName;
    }

    public void setSearchMapClassName(String searchMapClassName)
    {
        this.searchMapClassName = searchMapClassName;
    }

    public void create()
    {
        createPageList();

    }

    private void createPageList()
    {
        StringBuilder sb = new StringBuilder("package ").append(pageListPackage).append(";\r\n");
        sb.append("\r\n");
        sb.append("import java.io.Serializable;\r\n");
        sb.append("import java.math.BigDecimal;\r\n");
        sb.append("import java.util.Date;\r\n");
        sb.append("\r\n");
        sb.append("/**").append("\r\n");
        sb.append(" * @author ckex.zha created ")
                .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss").format(new Date()))
                .append("\r\n");
        sb.append(" * @explain -").append("\r\n");
        sb.append(" */").append("\r\n");
        sb.append("public class ").append(pageListClassName)
                .append(" implements Serializable {\r\n");
        sb.append("    private static final long serialVersionUID = " + System.currentTimeMillis()
                + "L;\r\n");
        sb.append("\r\n");
        System.out.println(sb.toString());
    }

    public static void main(String[] args)
    {
        PageUtil pageUtil = new PageUtil();
        pageUtil.pageListPackage = "com.test.common";
        pageUtil.pageListClassName = "PageList";
        pageUtil.createPageList();
    }
}
