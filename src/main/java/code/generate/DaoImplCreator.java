package code.generate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DaoImplCreator {
    private String    daoPackage;
    private String    modelPackage;
    private String    pageListPackage;
    private String    pageListClassName;
    private String    pageQueryPackage;
    private String    pageQueryClassName;
    private String    searchMapClassName;
    private String    integerPackage;
    private String    integerClassName;
    private String    utilPackage;
    private String    listClassName;
    private String    collClassName;
    private String    paginatorPackage;
    private String    paginatorClassName;
    private String    searchMapPackage;

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
    public void setSearchMapClassName(String searchMapClassName)
    {
        this.searchMapClassName = searchMapClassName;
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

    /** setter */
    public void setUtilPackage(String utilPackage)
    {
        this.utilPackage = utilPackage;
    }

    /** setter */
    public void setListClassName(String listClassName)
    {
        this.listClassName = listClassName;
    }

    /** setter */
    public void setCollClassName(String collClassName)
    {
        this.collClassName = collClassName;
    }

    /** setter */
    public void setPaginatorPackage(String paginatorPackage)
    {
        this.paginatorPackage = paginatorPackage;
    }

    /** setter */
    public void setPaginatorClassName(String paginatorClassName)
    {
        this.paginatorClassName = paginatorClassName;
    }

    public void setSearchMapPackage(String searchMapPackage)
    {
        this.searchMapPackage = searchMapPackage;
    }

    public void create(ResultSetMetaData meta) throws SQLException
    {
        String tableName = meta.getTableName(1);
        String daoClassName = GenerateHelper.getDaoClassName(tableName);
        String daoImplClassName = daoClassName + "Impl";
        String daoImplPackage = daoPackage + ".impl";
        String modelClassName = GenerateHelper.getModelClassName(tableName);
        String pkClassType = getPKType(meta);
        String pkName = meta.getColumnName(1);

        tableName = "Mapper." + tableName;
        StringBuilder sb = new StringBuilder("package ").append(daoImplPackage).append(";\r\n");
        sb.append("\r\n");

        sb.append("import ").append(integerPackage).append(".").append(integerClassName).append(";\r\n");

        sb.append("import ").append(utilPackage).append(".").append(listClassName).append(";\r\n");

        sb.append("import ").append(utilPackage).append(".").append(collClassName).append(";\r\n");
        sb.append("\r\n");

        sb.append("import org.springframework.stereotype.Repository;\r\n");
        sb.append("\r\n");
        //        sb.append("import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;\r\n");
        //        sb.append("\r\n");

        sb.append("import ").append(daoPackage).append(".").append("AbstractBasicDao;\r\n");
        sb.append("\r\n");

        sb.append("import ").append(daoPackage).append(".").append(daoClassName).append(";\r\n");
        sb.append("import ").append(modelPackage).append(".").append(modelClassName).append(";\r\n");

        sb.append("import ").append(searchMapPackage).append(".").append(searchMapClassName).append(";\r\n");

        sb.append("import ").append(pageQueryPackage).append(".").append(pageQueryClassName).append(";\r\n");

        sb.append("import ").append(pageListPackage).append(".").append(pageListClassName).append(";\r\n");

        sb.append("import ").append(paginatorPackage).append(".").append(paginatorClassName).append(";\r\n");

        sb.append("\r\n");

        sb.append("/**").append("\r\n");
        sb.append(" * @author ckex created ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss").format(new Date())).append("\r\n");
        sb.append(" * @explain -").append("\r\n");
        sb.append(" */").append("\r\n");

        sb.append("@Repository(\"" + getReposName(daoClassName) + "\")\r\n");
        sb.append("public class ").append(daoImplClassName).append(" extends AbstractBasicDao implements ").append(daoClassName).append(" {\r\n");
        sb.append("\r\n");

        //load
        sb.append("    @Override\r\n");
        sb.append("    public ").append(modelClassName).append(" load( ").append(pkClassType).append(" " + pkName + ") {\r\n");
        //sb.append(pkClassType).append(" sellerId) {\r\n");

        sb.append("         ").append(searchMapClassName).append(" map = ").append(" new ").append(searchMapClassName).append("();\r\n");

        //        sb.append("         ").append("map.add(\"sellerId\"").append(",").append("sellerId")
        //                .append(");\r\n");

        sb.append("         ").append("map.add( \"" + pkName + "\"").append(",").append(pkName).append(");\r\n");

        sb.append("        return (").append(modelClassName).append(") getSqlSessionTemplate().selectOne(\"");
        sb.append(tableName).append(".load\" , map);\r\n");

        sb.append("    }\r\n");

        //delete
        sb.append("\r\n");
        sb.append("    @Override\r\n");
        sb.append("    public boolean").append(" delete(").append(pkClassType).append(" " + pkName + ") {\r\n");
        //sb.append(pkClassType).append(" sellerId) {\r\n");

        sb.append("         ").append(searchMapClassName).append(" map = ").append(" new ").append(searchMapClassName).append("();\r\n");

        //        sb.append("         ").append("map.add(\"sellerId\"").append(",").append("sellerId")
        //                .append(");\r\n");

        sb.append("         ").append("map.add( \"" + pkName + "\"").append(",").append(pkName).append(");\r\n");

        sb.append("        return getSqlSessionTemplate().delete(\"");
        sb.append(tableName).append(".delete\", map) > 0;\r\n");
        sb.append("    }\r\n");

        //create
        sb.append("\r\n");
        sb.append("    @Override\r\n");
        sb.append("    public ").append(modelClassName).append(" create(").append(modelClassName).append(" record) {\r\n");
        //        sb.append("        return (").append(pkClassType)
        sb.append("         ").append("getSqlSessionTemplate().insert(\"");
        sb.append(tableName).append(".create\" , record);\r\n");
        sb.append("        return ").append("record;\r\n");
        sb.append("    }\r\n");

        //update
        sb.append("\r\n");
        sb.append("    @Override\r\n");
        sb.append("    public boolean").append(" update(").append(modelClassName).append(" record) {\r\n");
        sb.append("        return getSqlSessionTemplate().update(\"");
        sb.append(tableName).append(".update\", record) > 0;\r\n");
        sb.append("    }\r\n");

        //listByPage
        sb.append("\r\n");
        //        sb.append("    @SuppressWarnings(\"unchecked\")\r\n");
        sb.append("    @Override\r\n");
        sb.append("    public ").append(pageListClassName).append("<").append(modelClassName).append(">").append("  listByPage(");
        //sb.append(pkClassType).append(" sellerId, ")
        sb.append(pageQueryClassName).append(" pageQuery, ").append(integerClassName).append(" count){\r\n");

        sb.append("         ").append(searchMapClassName).append(" map = ").append(" new ").append(searchMapClassName).append("();\r\n");
        //        sb.append("         ").append("map.add(\"sellerId\"").append(",").append("sellerId")
        //                .append(");\r\n");

        sb.append("         map.add(\"startIndex\"").append(",").append("pageQuery.getStartIndex()").append(");\r\n");
        sb.append("         map.add(\"pageSize\"").append(",").append(" pageQuery.getPageSize()").append(");\r\n");

        sb.append("         if( count == null || count.intValue() == 0 ) {").append("\r\n");
        sb.append("             count = (").append(integerClassName).append(")").append("getSqlSessionTemplate().selectOne(\"").append(tableName)
                .append(".listByPageCount\",map);\r\n");
        sb.append("         }\r\n");

        sb.append("         ").append(listClassName).append("<").append(modelClassName).append("> ").append(" list = ").append(collClassName)
                .append(".emptyList();\r\n");

        sb.append("         if( count != null && count.intValue() > 0 ) {").append("\r\n");
        sb.append("             list = ");
        //        sb.append(listClassName).append("<").append(modelClassName).append(">) ");
        sb.append("getSqlSessionTemplate().selectList(\"").append(tableName).append(".listByPage\",map);\r\n");

        sb.append("          }\r\n");

        sb.append("         ").append(paginatorClassName).append(" paginator = ").append(" new ").append(paginatorClassName)
                .append("(pageQuery.getPageSize(), count == null ? 0 : count);\r\n");

        sb.append("         ").append("paginator.setPage(pageQuery.getPageNum());\r\n");

        sb.append("         ").append(pageListClassName).append("<").append(modelClassName).append(">").append(" result = ").append(" new ")
                .append(pageListClassName).append("<").append(modelClassName).append(">").append("(").append("list").append(");\r\n");

        sb.append("         ").append("result.setPaginator(paginator);\r\n");
        sb.append("         ").append("return result;\r\n");

        sb.append("     ").append("}\r\n");

        sb.append(" ").append("}\r\n");

        System.out.println(sb.toString());

        writeFile.write(daoImplPackage, daoImplClassName, "java", sb.toString());
    }

    private String getReposName(String daoClassName)
    {
        return daoClassName.substring(0, 1).toLowerCase() + daoClassName.substring(1);
    }

    private String getPKType(ResultSetMetaData meta) throws SQLException
    {
        int count = meta.getColumnCount();
        for (int i = 1; i <= count; i++)
        {
            if (meta.getColumnName(i).equalsIgnoreCase("id"))
            {
                return GenerateHelper.getJavaType(meta.getColumnClassName(i));
            }
        }
        return meta.getColumnClassName(1);

        //        throw new RuntimeException("getPKType error, no column[id].");
    }
}
