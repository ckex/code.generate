package code.generate.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import code.generate.CodeMain;

/**
 * 2015年6月12日-下午12:37:18
 *
 * @author Ckex.zha spring4 + mybatis3
 */
public class Main {

    public static void main(final String[] args) throws Exception
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context-codegenerate.xml");
        CodeMain codeMain = context.getBean(CodeMain.class);

        codeMain.execute();
        //        codeMain.listTables();
        //        codeMain.showCreateTables();
        //        codeMain.printPKsql();
        //        codeMain.printSql();
        System.out.println("done....");
    }
}
