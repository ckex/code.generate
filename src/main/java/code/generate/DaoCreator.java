package code.generate;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ckex created 2014-3-8 下午7:15:52
 * @explain -
 */
public class DaoCreator {
    private String    daoPackage;
    private String    modelPackage;

    private String    pageListPackage;
    private String    pageListClassName;
    private String    pageQueryPackage;
    private String    pageQueryClassName;
    private String    integerPackage;
    private String    integerClassName;

    @Autowired
    private WriteFile writeFile;

    /** setter */
    public void setDaoPackage(String daoPackage)
    {
        this.daoPackage = daoPackage;
    }

    /** setter */
    public void setModelPackage(String modelPackage)
    {
        this.modelPackage = modelPackage;
    }

    /** setter */
    public void setPageListPackage(String pageListPackage)
    {
        this.pageListPackage = pageListPackage;
    }

    /** setter */
    public void setPageListClassName(String pageListClassName)
    {
        this.pageListClassName = pageListClassName;
    }

    /** setter */
    public void setPageQueryPackage(String pageQueryPackage)
    {
        this.pageQueryPackage = pageQueryPackage;
    }

    /** setter */
    public void setPageQueryClassName(String pageQueryClassName)
    {
        this.pageQueryClassName = pageQueryClassName;
    }

    /** setter */
    public void setIntegerPackage(String integerPackage)
    {
        this.integerPackage = integerPackage;
    }

    /** setter */
    public void setIntegerClassName(String integerClassName)
    {
        this.integerClassName = integerClassName;
    }

    public void create(ResultSetMetaData meta) throws SQLException
    {
        String daoClassName = GenerateHelper.getDaoClassName(meta.getTableName(1));
        String modelClassName = GenerateHelper.getModelClassName(meta.getTableName(1));
        String pkClassType = GenerateHelper.getPKType(meta);
        String pkName = meta.getColumnName(1);

        StringBuilder sb = new StringBuilder("package ").append(daoPackage).append(";\r\n");
        //        sb.append("\r\n");
        //        sb.append("import java.util.Date;\r\n");
        sb.append("\r\n");
        sb.append("import ").append(integerPackage).append(".").append(integerClassName)
                .append(";\r\n");
        sb.append("import ").append(modelPackage).append(".").append(modelClassName)
                .append(";\r\n");

        sb.append("import ").append(pageListPackage).append(".").append(pageListClassName)
                .append(";\r\n");

        sb.append("import ").append(pageQueryPackage).append(".").append(pageQueryClassName)
                .append(";\r\n");

        sb.append("\r\n");

        sb.append("/**").append("\r\n");
        sb.append(" * @author ckex created ")
                .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss").format(new Date()))
                .append("\r\n");
        sb.append(" * @explain -").append("\r\n");
        sb.append(" */").append("\r\n");
        sb.append("public interface ").append(daoClassName).append(" {\r\n");
        sb.append("\r\n");

        //load
        sb.append("    ").append(modelClassName).append(" load(").append(pkClassType)
                .append(" " + pkName + ");\r\n");
        //                .append(" " + pkName + ", ").append(pkClassType).append(" sellerId);\r\n");

        //delete
        sb.append("\r\n");
        sb.append("    boolean").append(" delete(").append(pkClassType);
        sb.append(" " + pkName + ");\r\n");
        //        sb.append(" " + pkName + ", ").append(pkClassType).append(" sellerId);\r\n");

        //create
        sb.append("\r\n");
        sb.append("    ").append(modelClassName).append(" create(").append(modelClassName)
                .append(" record);\r\n");

        //update
        sb.append("\r\n");
        sb.append("    boolean").append(" update(").append(modelClassName).append(" record);\r\n");

        //load list
        sb.append("\r\n");
        sb.append("    ").append(pageListClassName).append("<").append(modelClassName).append(">")
                .append("  listByPage(");
        //        sb..append(pkClassType).append(" sellerId, ")
        sb.append(pageQueryClassName).append(" pageQuery, ").append(integerClassName)
                .append(" count);\r\n");

        sb.append("}");

        System.out.println(sb.toString());

        writeFile.write(daoPackage, daoClassName, "java", sb.toString());
    }
}
