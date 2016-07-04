package code.generate;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;

import code.generate.util.StringUtil;

public class SqlmapCreator {
    private String    daoPackage;
    private String    modelPackage;
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

    public void create(ResultSetMetaData meta) throws SQLException
    {
        String tableName = meta.getTableName(1);
        String modelClassName = GenerateHelper.getModelClassName(tableName);
        String resultClassName = modelClassName.substring(0, modelClassName.length() - 2);
        String resultClassNameFull = resultClassName + "Result";
        String pkClassType = getPKType(meta);
        String pkName = meta.getColumnName(1);
        String pkClassTypePrimitive = null;
        if (pkClassType.equalsIgnoreCase("Integer")
                || pkClassType.equalsIgnoreCase("java.util.Integer"))
        {
            pkClassTypePrimitive = "int";
        } else if (pkClassType.equalsIgnoreCase("Long")
                || pkClassType.equalsIgnoreCase("java.lang.Long"))
        {
            pkClassTypePrimitive = "long";
        } else if (pkClassType.equalsIgnoreCase("java.math.BigInteger"))
        {
            pkClassTypePrimitive = "long";
        } else
        {
            throw new RuntimeException("not support pk type : " + pkClassType);
        }

        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
        sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\r\n");
        sb.append("<mapper namespace=\"Mapper.").append(tableName).append("\">\r\n");
        sb.append("   <cache type=\"org.mybatis.caches.ehcache.LoggingEhcache\" readOnly=\"true\" />\r\n");
        //        sb.append("    <typeAlias alias=\"").append(resultClassName).append("\" type=\"");
        //        sb.append(modelPackage).append(".").append(modelClassName).append("\" />\r\n");
        String fullClassName = modelPackage + "." + modelClassName;
        sb.append("    <resultMap id=\"").append(resultClassNameFull).append("\" type=\"")
                .append(fullClassName).append("\">\r\n");

        int count = meta.getColumnCount();
        //生成字段
        for (int i = 1; i <= count; i++)
        {
            String columnClassName = meta.getColumnClassName(i);
            String columnName = meta.getColumnName(i);
            String sqlmapJavaType = GenerateHelper.getSqlmapJavaType(columnName, columnClassName);

            sb.append("        <result column=\"").append(columnName).append("\"");
            sb.append(" property=\"").append(GenerateHelper.getColumnName(columnName)).append("\"");

//            if (meta.getColumnType(i) == 1 && columnClassName.equals("java.lang.String"))
//            {
//                sb.append(" jdbcType=\"CHAR\" javaType=\"boolean\" nullValue=\"N\"");
//            } else
//            {
//                sb.append(" jdbcType=\"").append(sqlmapJavaType).append("\"");
//                if (sqlmapJavaType.equalsIgnoreCase("DECIMAL"))
//                {
//                    sb.append(" nullValue=\"0.00\"");
//                }
//            }

            sb.append(" />\r\n");
        }
        sb.append("    </resultMap>\r\n");

        //load方法
        sb.append("\r\n");
        sb.append("    <select id=\"load\" resultMap=\"").append(resultClassNameFull)
                .append("\" parameterType=\"").append("java.util.HashMap").append("\">\r\n");

        sb.append("        select ");
        for (int i = 1; i <= count; i++)
        {
            String columnName = meta.getColumnName(i);
            if (i > 1)
            {
                sb.append(", ");
            }
            sb.append("`");
            sb.append(columnName);
            sb.append("`");
        }
        sb.append("\r\n");
        sb.append("        FROM ").append(tableName).append("\r\n");
        sb.append("        WHERE " + pkName + " = #{" + pkName);
        //                .append(pkClassType.toUpperCase());
        sb.append("}\r\n");
        //        sb.append("        AND sellerId = #sellerId:").append(pkClassType.toUpperCase()).append("#\r\n");
        sb.append("    </select>");

        //delete
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append("    <delete id=\"delete\" parameterType=\"").append("java.util.HashMap")
                .append("\">\r\n");
        sb.append("        delete from ").append(tableName).append("\r\n");
        sb.append("        WHERE " + pkName + " = #{" + pkName);
        sb.append("}\r\n");
        //                .append(pkClassType.toUpperCase());
        //        sb.append("        AND sellerId = #sellerId:").append(pkClassType.toUpperCase()).append("#\r\n");
        sb.append("    </delete>");

        //create
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append("    <insert id=\"create\" parameterType=\"").append(fullClassName)
                .append("\">\r\n");
        sb.append("        insert into ").append(tableName).append(" (");
        for (int i = 1; i <= count; i++)
        {
            String columnName = meta.getColumnName(i);
            if (i > 1)
            {
                sb.append(", ");
            }
            sb.append("`");
            sb.append(columnName);
            sb.append("`");
        }
        sb.append(") \r\n        values (");
        for (int i = 1; i <= count; i++)
        {
            String columnClassName = meta.getColumnClassName(i);
            String columnName = meta.getColumnName(i);
            String sqlmapJavaType = GenerateHelper.getSqlmapJavaType(columnName, columnClassName);
            if (i > 1)
            {
                sb.append(", ");
            }
            if (columnName.equalsIgnoreCase("createTime")
                    || columnName.equalsIgnoreCase("updateTime")
                    || columnName.equalsIgnoreCase("gmt_create")
                    || columnName.equalsIgnoreCase("gmt_modified")
                    || columnName.equalsIgnoreCase("gmt_created")
                    || columnName.equalsIgnoreCase("gmt_modifie"))
            {
                sb.append("now()");
            } else
            {
                if (meta.getColumnType(i) == 1 && columnClassName.equals("java.lang.String"))
                {
                    sb.append("#{").append(GenerateHelper.getColumnName(columnName)).append("}");
                    //                            .append(":CHAR#");
                } else
                {
                    sb.append("#{").append(GenerateHelper.getColumnName(columnName)).append("}");
                    //                    sb.append(":").append(sqlmapJavaType).append("#");
                }
            }
        }
        sb.append(")\r\n");

        //        sb.append("        <selectKey keyProperty=\"id\" type=\"post\" resultClass=\"")
        //                .append(pkClassTypePrimitive).append("\">\r\n");
        //        sb.append("            select LAST_INSERT_ID() as id\r\n");
        //        sb.append("        </selectKey>\r\n");

        sb.append("    </insert>");

        //update
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append("    <update id=\"update\" parameterType=\"").append(fullClassName)
                .append("\">\r\n");
        sb.append("        update ").append(tableName).append("\r\n");
        //        sb.append("        <dynamic prepend=\"set\">\r\n");
        // sb.append(" set gmt_modified = now()\r\n");
        sb.append("      <set>\r\n");
        boolean fk = Boolean.FALSE;
        String last = null;
        for (int i = 1; i <= count; i++)
        {
            String columnName = meta.getColumnName(i);
            if (columnName.equalsIgnoreCase("gmt_modified") && last == null)
            {
                //                sb.append("        gmt_modified = now(),\r\n");
                last = "          gmt_modified = now()\r\n";
                continue;
            } else if (columnName.equalsIgnoreCase("gmt_modifie") && last == null)
            {
                //                sb.append("        gmt_modified = now(),\r\n");
                last = "          gmt_modified = now()\r\n";
                continue;
            } else if (columnName.equalsIgnoreCase("updateTime") && last == null)
            {
                //                sb.append("        gmt_modified = now(),\r\n");
                last = "          gmt_modified = now()\r\n";
                continue;
            }else if(columnName.equalsIgnoreCase("datetime_modified") && last == null){
                last = "          `DATETIME_MODIFIED` = now()\r\n";
                continue;
            } else if (columnName.equalsIgnoreCase("gmt_created"))
            {
                continue;
            } else if (columnName.equalsIgnoreCase("gmt_create"))
            {
                continue;
            }
            //            if (columnName.equalsIgnoreCase("sellerId") || columnName.equalsIgnoreCase("seller_id"))
            //            {
            //                fk = Boolean.TRUE;
            //            }
            //            if (columnName.equalsIgnoreCase("id") || columnName.equalsIgnoreCase("sellerId")
            //                    || columnName.equalsIgnoreCase("seller_id"))
            //            {
            //                continue;
            //            }
            //            String hasserName = "has"
            //                    + StringUtil.first2UpperCase(GenerateHelper.getColumnName(columnName));
            String columnClassName = meta.getColumnClassName(i);
            String sqlmapJavaType = GenerateHelper.getSqlmapJavaType(columnName, columnClassName);
            //            if (columnName.equalsIgnoreCase("createTime")
            //                    || columnName.equalsIgnoreCase("updateTime")
            //                    || columnName.equalsIgnoreCase("gmt_create")
            //                    || columnName.equalsIgnoreCase("gmt_modified"))
            //            {
            //                continue;
            //            }

            // <if test="dicName != null">dic_name = #{dicName},</if>
            sb.append("          <if test=\"" + GenerateHelper.getColumnName(columnName)  + " != null\">`" + columnName + "` = #{"
                    + GenerateHelper.getColumnName(columnName) + "},</if>\r\n");

            //            sb.append("        <isNotNull prepend=\",\" property=\"").append(columnClassName)
            //                    .append("\">\r\n");
            //            if (meta.getColumnType(i) == 1 && columnClassName.equals("java.lang.String"))
            //            {
            //                sqlmapJavaType = "CHAR";
            //            }
            //            sb.append("            ").append(columnName).append(" = ").append("#{")
            //                    .append(GenerateHelper.getColumnName(columnName));
            //            sb.append("}\r\n");
            //            //            sb.append(":").append(sqlmapJavaType);
            //            sb.append("        </isNotNull>\r\n");
        }
        if (last != null)
        {
            sb.append(last);
        }
        sb.append("      </set>\r\n");
        String columnClassName = meta.getColumnClassName(1);
        String sqlmapJavaType = GenerateHelper.getSqlmapJavaType(pkName, columnClassName);
        sb.append("        WHERE " + pkName + " = #{" + pkName + "}\r\n");
        //        (":" + sqlmapJavaType + "#\r\n");
        if (fk)
        {
            sb.append("        AND sellerId = #sellerId:BIGINT#\r\n");
        }
        sb.append("    </update>");

        sb.append("\r\n");

        //listByPage方法
        sb.append("\r\n");
        sb.append("    <select id=\"listByPage\" resultMap=\"").append(resultClassNameFull)
                .append("\" parameterType=\"").append("java.util.HashMap").append("\">\r\n");

        sb.append("        SELECT ");
        for (int i = 1; i <= count; i++)
        {
            String columnName = meta.getColumnName(i);
            if (i > 1)
            {
                sb.append(", ");
            }
            sb.append("t2.").append("`");
            sb.append(columnName);
            sb.append("`");
        }
        sb.append("\r\n");
        sb.append("        FROM (").append("\r\n");
        sb.append("                 SELECT " + pkName + " FROM ").append(tableName);
        sb.append("                 WHERE 1 ");
        //        sb.append("                 WHERE sellerId = #sellerId:").append(pkClassType.toUpperCase())
        //                .append("#\r\n");
        sb.append("                 ORDER BY " + pkName).append("\r\n");
        sb.append("                 LIMIT #{startIndex},#{pageSize}");
        sb.append(")t1,").append(tableName).append(" t2 ");
        sb.append("WHERE t1." + pkName + " = t2." + pkName);
        //sb.append(" AND t2.sellerId = #sellerId:").append(pkClassType.toUpperCase()).append("#\r\n");
        sb.append("\r\n");
        sb.append("    </select>");

        sb.append("\r\n");

        //listByPageCount方法
        sb.append("\r\n");
        sb.append("    <select id=\"listByPageCount\" resultType=\"").append("java.lang.Integer")
                .append("\" parameterType=\"").append("java.util.HashMap").append("\">\r\n");

        sb.append("        SELECT count(*) ").append("\r\n");
        sb.append("        FROM ").append(tableName);
        sb.append("         WHERE 1 ");
        //                sb.append("        WHERE sellerId = #sellerId:").append(pkClassType.toUpperCase()).append("#")
        sb.append("\r\n");

        sb.append("    </select>");

        sb.append("\r\n");

        sb.append("</mapper>");

        sb.append("\r\n");

        System.out.println(sb.toString());

        writeFile.write(daoPackage, tableName + "_mapper", "xml", sb.toString());
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
