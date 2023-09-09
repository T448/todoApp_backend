package com.example.spring_project.infrastructure.redis.methods;

import com.example.spring_project.config.ApplicationProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

@Service
public class RedisMethods {

  @Autowired
  private ApplicationProperty applicationProperty;

//   String host = applicationProperty.get("spring.redis_host");
//   String port = applicationProperty.get("spring.redis_port");

  public String RegisterStrings(String key, String[] data) {
    String host = applicationProperty.get("spring.redis_host");
    String port = applicationProperty.get("spring.redis_port");
    try {
        Jedis jedis = new Jedis(host, Integer.parseInt(port));
        jedis.rpush(key, data);
        jedis.close();
        return key;
    } catch (Exception error) {
        return error.toString();
    }
  }

  public List<String> GetStrings(String key) {
    String host = applicationProperty.get("spring.redis_host");
    String port = applicationProperty.get("spring.redis_port");
    try {
        Jedis jedis = new Jedis(host, Integer.parseInt(port));
        List<String> res = jedis.lrange(key, 0, -1);
        jedis.close();
        return res;
    } catch (Exception error) {
        List<String> res = new ArrayList<String>();
        res.add(error.toString());
        return res;
    }
  }

  /*
   * @param String key,String data
   * @return key
   */
  public String RegisterString(String key,String data){
    String host = applicationProperty.get("spring.redis_host");
    String port = applicationProperty.get("spring.redis_port");
    try {
        Jedis jedis = new Jedis(host, Integer.parseInt(port));
        jedis.set(key,data);
        jedis.close();
        return key;
    } catch (Exception error) {
        return error.toString();
    }
  }

  public String GetString(String key){
  String host = applicationProperty.get("spring.redis_host");
  String port = applicationProperty.get("spring.redis_port");
  try {
      Jedis jedis = new Jedis(host, Integer.parseInt(port));
      String res = jedis.get(key);
      jedis.close();
      return res;
  } catch (Exception error) {
      return error.toString();
  }
}

  public String OverwriteData(String key, String[] data) {
    String host = applicationProperty.get("spring.redis_host");
    String port = applicationProperty.get("spring.redis_port");
    try {
        Jedis jedis = new Jedis(host, Integer.parseInt(port));
        jedis.del(key);
        jedis.rpush(key, data);
        jedis.close();
        return key;
    } catch (Exception error) {
        return error.toString();
    }
  }

  public String OverwriteDataString(String key, String data) {
    String host = applicationProperty.get("spring.redis_host");
    String port = applicationProperty.get("spring.redis_port");
    try {
        Jedis jedis = new Jedis(host, Integer.parseInt(port));
        jedis.del(key);
        jedis.set(key, data);
        jedis.close();
        return key;
    } catch (Exception error) {
        return error.toString();
    }
  }
}
