package io.github.qianlixy.cache.business.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import io.github.qianlixy.cache.SpringContextBaseTest;
import io.github.qianlixy.cache.business.dao.Driver;

public abstract class BaseBusinessTest extends SpringContextBaseTest {

	static final String INIT_DB_FILE_PATH = BaseBusinessTest.class.getClassLoader().getResource("init-database.sql")
			.getPath();
	static final String DESTORY_DB_FILE_PATH = BaseBusinessTest.class.getClassLoader()
			.getResource("destory-database.sql").getPath();

	@BeforeClass
	public static void initDB() {
		execSqlFie(INIT_DB_FILE_PATH);
	}

	@AfterClass
	public static void detoryDB() {
		execSqlFie(DESTORY_DB_FILE_PATH);
	}

	static void execSqlFie(String fileName) {
		try {
			Class.forName(Driver.DRIVER);
			Connection conn = DriverManager.getConnection(Driver.MYSQL_URL, Driver.USERNAME, Driver.PASSWORD);
			ScriptRunner runner = new ScriptRunner(conn);
			runner.setAutoCommit(true);
			File file = new File(fileName);
			try {
				if (file.getName().endsWith(".sql")) {
					// runner.setFullLineDelimiter(true);
					runner.setDelimiter(";");
					runner.setSendFullScript(false);
					runner.setAutoCommit(true);
					runner.setStopOnError(true);
					runner.runScript(new InputStreamReader(new FileInputStream(fileName), "GBK"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
