package code.generate;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import code.generate.util.StringUtil;

/**
 * 2015年6月12日-下午12:43:55
 *
 * @author Ckex.zha TODO
 */
public class GenerateHelper {

    public static String getModelClassName(String tableName)
    {
        if (tableName.startsWith("d_") || tableName.startsWith("t_"))
        {
            String result = StringUtil.toCamelCase(tableName);
            return result.substring(1) + "Do";
        } else
        {
            String result = StringUtil.toCamelCase(tableName);
            return result.substring(0, 1).toUpperCase() + result.substring(1) + "Do";
            //            throw new RuntimeException("not supported table name : " + tableName);
        }
    }

    public static String getJavaType(String columnClassName)
    {
        if ("java.lang.Boolean".equals(columnClassName))
        {
            return "Integer";
        }
        if (columnClassName.startsWith("java.lang."))
        {
            return columnClassName.substring("java.lang.".length());
        } else if (columnClassName.equals("java.sql.Timestamp") || columnClassName.equals("java.sql.Date"))
        {
            return "Date";
        } else if (columnClassName.equals("java.math.BigInteger"))
        {
            return "Long";
        } else if (columnClassName.equals("java.math.BigDecimal"))
        {
            return "BigDecimal";
        } else if (columnClassName.equals("[B"))
        {
            return "byte[]";
        } else
        {
            throw new RuntimeException("not supported type : " + columnClassName);
        }
    }

    public static String getSqlmapJavaType(String columnName, String columnClassName)
    {
        if (columnClassName.equals("java.lang.Integer"))
        {
            return "INTEGER";
        } else if (columnClassName.equals("java.lang.String"))
        {
            return "VARCHAR";
        } else if (columnClassName.equals("java.sql.Timestamp") || columnClassName.equals("java.sql.Date"))
        {
            return "TIMESTAMP";
        } else if (columnClassName.equals("java.lang.Long") || columnClassName.equals("java.math.BigInteger"))
        {
            return "BIGINT";
        } else if (columnClassName.equals("java.lang.Float"))
        {
            return "NUMBER";
        } else if (columnClassName.equals("java.math.BigDecimal"))
        {
            return "DECIMAL";
        } else if (columnClassName.equals("java.lang.Double"))
        {
            return "DECIMAL";
        } else if (columnClassName.equals("java.lang.Boolean"))
        {
            return "TINYINT";
        } else if (columnClassName.equals("[B"))
        {
            return "BINARY";
        } else
        {
            throw new RuntimeException(columnName + " not supported type : " + columnClassName);
        }
    }

    public static String getDaoClassName(String tableName)
    {
        if (tableName.startsWith("d_") || tableName.startsWith("t_"))
        {
            String result = StringUtil.toCamelCase(tableName);
            return result.substring(1) + "Dao";
        } else
        {
            String result = StringUtil.toCamelCase(tableName);
            return result.substring(0, 1).toUpperCase() + result.substring(1) + "Dao";
            //            throw new RuntimeException("not supported table name : " + tableName);
        }
    }

    public static String getPKType(ResultSetMetaData meta) throws SQLException
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
        //        throw new RuntimeException("getPKType error, no column[id]. table is "
        //                + meta.getTableName(1));
    }

    public static String getColumnName(String columnNameInMeta)
    {
        return StringUtil.toCamelCase(columnNameInMeta);
    }
}
