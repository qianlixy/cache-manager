package io.github.qianlixy.cache.parse;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.stat.TableStat.Name;

import io.github.qianlixy.cache.context.ApplicationContext;
import io.github.qianlixy.cache.context.CacheContext;

public class DruidSQLParser extends FilterEventAdapter implements SQLParser {
	
	private CacheContext context;

	@Override
	protected void statementExecuteUpdateAfter(StatementProxy statement, String sql, int updateCount) {
		handleSql(statement, sql);
	}

	@Override
	protected void statementExecuteQueryAfter(StatementProxy statement, String sql, ResultSetProxy resultSet) {
		handleSql(statement, sql);
	}

	@Override
	protected void statementExecuteAfter(StatementProxy statement, String sql, boolean result) {
		handleSql(statement, sql);
	}

	@Override
	protected void statementExecuteBatchAfter(StatementProxy statement, int[] result) {
		for(String sql : statement.getBatchSqlList()) {
			handleSql(statement, sql);
		}
	}

	private void handleSql(StatementProxy statement, String sql) {
		try {
			String dbType = getDbType(statement);
			boolean isAlter = false;
			LOGGER.debug("Intercepted SQL : [{}]", sql);
			List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType.toLowerCase());
			List<String> tables = new ArrayList<>();
			for (SQLStatement sqlStatement : stmtList) {
				MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
				sqlStatement.accept(visitor);
				Map<Name, TableStat> tableMap = visitor.getTables();
				for (Name name : tableMap.keySet()) {
					String tableName = name.getName();
					tables.add(tableName.toLowerCase());
					TableStat tableStat = tableMap.get(name);
					if (tableStat.getInsertCount() > 0
							|| tableStat.getUpdateCount() > 0
							|| tableStat.getDeleteCount() > 0) {
						context.setTableLastAlterTime(tableName.toLowerCase(), 
								ApplicationContext.getConsistentTimeProvider().newInstance());
						if (!isAlter) isAlter = true;
					}
				}
			}
			context.addTables(tables);
			context.setQuery(!isAlter);
		} catch (Throwable th) {
			LOGGER.error("Occur exception while parse SQL. exception message : {}", th.getMessage());
		}
	}
	
	private String getDbType(StatementProxy statement) throws SQLException {
		try {
			DatabaseMetaData metaData = statement.getConnectionProxy().getMetaData();
			return metaData.getDatabaseProductName();
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		if(!(dataSource instanceof DruidDataSource)) {
			throw new ClassCastException("DruidSqlParser need [" 
					+ DruidDataSource.class.getName() 
					+ "] but get [" 
					+ dataSource.getClass().getName() + "]");
		}
		DruidDataSource dds = (DruidDataSource) dataSource;
		List<Filter> filters = new ArrayList<>();
		filters.add(this);
		dds.setProxyFilters(filters);
		LOGGER.info("DruidSQLParser bind to [{}]", dataSource.getClass());
	}

	@Override
	public void setCacheContext(CacheContext context) {
		this.context = context;
	}
	
}
