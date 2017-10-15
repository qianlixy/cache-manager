package io.github.qianlixy.cache.business.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {
	
	@Override
	public int save(final String username, final String password, final String realname) {
		final String sql = "INSERT INTO USER VALUES (NULL, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				statement.setString(1, username);
				statement.setString(2, password);
				statement.setString(3, realname);
				return statement;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	@Override
	public void update(Integer id, String username, String password, String realname) {
		getJdbcTemplate().update("UPDATE USER SET USERNAME = ? AND PASSWORD = ? AND REALNAME = ? WHERE ID = ?", username, password, realname, id);
	}
	
	@Override
	public void delete(Integer id) {
		getJdbcTemplate().update("DELETE FROM USER WHERE ID = ?", id);
	}
	
	@Override
	public List<Map<String, Object>> findAll() {
		return getJdbcTemplate().queryForList("SELECT * FROM USER");
	}
	
	@Override
	public Map<String, Object> find(Integer id) {
		List<Map<String, Object>> list = getJdbcTemplate().queryForList("SELECT * FROM USER WHERE ID = ?", id);
		if(null == list || list.size() < 1) return null;
		return list.get(0);
	}
	
}
