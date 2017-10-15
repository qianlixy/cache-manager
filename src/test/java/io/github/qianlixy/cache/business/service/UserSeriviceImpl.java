package io.github.qianlixy.cache.business.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.qianlixy.cache.business.dao.UserDao;

@Service
public class UserSeriviceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public int save(String username, String password, String realname) {
		return userDao.save(username, password, realname);
	}
	
	@Override
	public void update(Integer id, String username, String password, String realname) {
		userDao.update(id, username, password, realname);
	}
	
	@Override
	public void delete(Integer id) {
		userDao.delete(id);
	}
	
	@Override
	public List<Map<String, Object>> findAll() {
		return userDao.findAll();
	}
	
	@Override
	public Map<String, Object> find(Integer id) {
		return userDao.find(id);
	}
	
}
