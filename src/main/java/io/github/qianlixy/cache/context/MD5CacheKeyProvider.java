package io.github.qianlixy.cache.context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将缓存key生成为它的MD5值，保证缓存key的唯一性。
 * 如果不能生成MD5值，将使用key的hashcode
 * @author qianli_xy@163.com
 * @since 1.0.0
 */
public class MD5CacheKeyProvider implements CacheKeyProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MD5CacheKeyProvider.class);

	@Override
	public String process(String key) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Unable to generate MD5, use hashcode instead on [{}]", key);
			String hashCode = String.valueOf(key.hashCode());
			LOGGER.debug("Generate cache key [{}] on [{}]", hashCode, key);
			return hashCode;
		}

		char[] charArray = key.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Generate cache key [{}] on [{}]", hexValue.toString(), key);
		}
		return hexValue.toString();
	}

}
