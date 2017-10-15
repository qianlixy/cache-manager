package io.github.qianlixy.cache.business.service;

import java.util.List;
import java.util.Map;

public interface UserService {

	int save(String username, String password, String realname);

	void update(Integer id, String username, String password, String realname);

	void delete(Integer id);

	List<Map<String, Object>> findAll();

	Map<String, Object> find(Integer id);

}