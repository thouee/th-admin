package me.th.share.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings("all")
public class RedisUtils {

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisUtils(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setStringSerializer(new StringRedisSerializer());
    }

    /**
     * 设置指定 key 过期时间
     *
     * @param key  -
     * @param time 时间（秒）
     * @return boolean
     */
    public boolean expire(String key, long time) {
        return expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 设置指定 key 过期时间
     *
     * @param key      -
     * @param time     时间
     * @param timeUnit 时间单位
     * @return boolean
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 获取指定 key 的过期时间（秒）
     *
     * @param key -
     * @return long 时间，为 0 表示永久有效
     */
    public long getExpire(String key) {
        return getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 获取指定 key 的过期时间
     *
     * @param key      -
     * @param timeUnit 时间单位
     * @return long 时间，为 0 表示永久有效
     */
    public long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 查找匹配 key
     *
     * @param pattenKey -
     * @return List<String>
     */
    public List<String> scan(String pattenKey) {
        ScanOptions so = ScanOptions.scanOptions().match(pattenKey).build();
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection rc = Objects.requireNonNull(factory).getConnection();
        Cursor<byte[]> cursor = rc.scan(so);
        List<String> result = cursor.stream().map(String::new).collect(Collectors.toList());
        try {
            RedisConnectionUtils.releaseConnection(rc, factory);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 分页查找匹配 key
     *
     * @param pattenKey -
     * @param page      页码，从 0 开始
     * @param size      每页数量
     * @return List<String>
     */
    public List<String> findKeys4Page(String pattenKey, int page, int size) {
        ScanOptions so = ScanOptions.scanOptions().match(pattenKey).build();
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection rc = Objects.requireNonNull(factory).getConnection();
        Cursor<byte[]> cursor = rc.scan(so);
        List<String> result = Lists.newArrayList();
        int tmpI = 0, fromI = page * size, toI = fromI + size;
        while (cursor.hasNext()) {
            if (tmpI >= fromI && tmpI < toI) {
                result.add(new String(cursor.next()));
                tmpI++;
                continue;
            }
            if (tmpI >= toI) {
                break;
            }
            tmpI++;
            cursor.next();
        }
        try {
            RedisConnectionUtils.releaseConnection(rc, factory);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 判断 key 是否存在
     *
     * @param key -
     * @return boolean
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                Boolean result = redisTemplate.delete(keys[0]);
                log.debug("删除缓存 {}：{}", keys[0], result);
            } else {
                Set<Object> keySet = Sets.newHashSet();
                for (String key : keys) {
                    if (hasKey(key)) {
                        keySet.add(key);
                    }
                }
                Long count = redisTemplate.delete(keySet);
                log.debug("删除缓存 {}：数量 {}", keySet, count);
            }
        }
    }

    /**
     * 获取
     *
     * @param key -
     * @return Object
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 批量获取
     *
     * @param keys -
     * @return List<Object>
     */
    public List<Object> get(List<String> keys) {
        List<Object> list = redisTemplate.opsForValue().multiGet(Sets.newHashSet(keys));
        List<Object> result = Lists.newArrayList();
        Optional.ofNullable(list).ifPresent(e -> e.forEach(ee -> {
            Optional.ofNullable(ee).ifPresent(result::add);
        }));
        return result;
    }


    /**
     * 放入
     *
     * @param key   -
     * @param value -
     * @return boolean
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 放入并设置过期时间
     *
     * @param key   -
     * @param value -
     * @param time  过期时间，单位秒
     * @return boolean
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 放入并设置过期时间
     *
     * @param key      -
     * @param value    -
     * @param time     过期时间
     * @param timeUnit 时间单位
     * @return boolean
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * HashGet
     *
     * @param key  键，不能为 null
     * @param item 项，不能为 null
     * @return Object
     */
    public Object hGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取 hashKey 对应的所有键值
     *
     * @param key -
     * @return Map<Object, Object>
     */
    public Map<Object, Object> hGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key -
     * @param map -
     * @return boolean
     */
    public boolean hSet(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * HashSet 并设置过期时间
     *
     * @param key  -
     * @param map  -
     * @param time 过期时间，单位秒
     * @return boolean
     */
    public boolean hSet(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向指定哈希表中存放数据，如果不存在将创建
     *
     * @param key   -
     * @param item  -
     * @param value -
     * @return boolean
     */
    public boolean hSet(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向指定哈希表中存放数据，并设置过期时间，如果不存在将创建
     *
     * @param key   -
     * @param item  -
     * @param value -
     * @param time  过期时间，单位秒
     * @return boolean
     */
    public boolean hSet(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除哈希表中的值
     *
     * @param key  -
     * @param item 项，可以有多个，不能为 null
     */
    public void hDel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断哈希表中是否有该项的值
     *
     * @param key  -
     * @param item -
     * @return boolean
     */
    public boolean hHasKey(String key, String item) {
        return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(key, item));
    }

    /**
     * hash 递增，如果不存在就会创建一个，并把新增后的值返回
     *
     * @param key  -
     * @param item -
     * @param by   要增加的数
     * @return double
     */
    public double hIncr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash 递减
     *
     * @param key  -
     * @param item -
     * @param by   要减少的数
     * @return double
     */
    public double hDecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 获取 key 对应 Set 中的所有值
     *
     * @param key -
     * @return Set<Object>
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 查询指定 Set 中是否有指定 value
     *
     * @param key   -
     * @param value -
     * @return boolean
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将数据存放 Set 中
     *
     * @param key    -
     * @param values -
     * @return long
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 将数据存放 Set 中，并设置过期时间
     *
     * @param key    -
     * @param time   过期时间，单位秒
     * @param values -
     * @return long
     */
    public long sSet(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 获取 Set 长度
     *
     * @param key -
     * @return long
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 移除指定数据
     *
     * @param key    -
     * @param values -
     * @return long
     */
    public long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 添加
     *
     * @param key   -
     * @param value -
     * @param score -
     * @return boolean
     */
    public boolean zAdd(String key, Object value, double score) {
        try {
            redisTemplate.opsForZSet().add(key, value, score);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除
     *
     * @param key   -
     * @param value -
     */
    public void zDel(String key, Object... value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 范围获取
     *
     * @param key -
     * @param min -
     * @param max -
     * @return Object
     */
    public Object zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 获取 list 缓存内容
     *
     * @param key   -
     * @param start 起始
     * @param end   结束
     * @return List<Object>
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取 list 缓存所有内容
     *
     * @param key -
     * @return List<Object>
     */
    public List<Object> lGet(String key) {
        return lGet(key, 0, -1);
    }

    /**
     * 获取 list 缓存长度
     *
     * @param key -
     * @return long
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 通过索引获取 list 中的值
     *
     * @param key   -
     * @param index 索引，0：表头，-1 表尾
     * @return Object
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 放入 list 缓存
     *
     * @param key   -
     * @param value -
     * @return boolean
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 放入 list 缓存，并设置过期时间
     *
     * @param key   -
     * @param value -
     * @param time  过期时间，单位秒
     * @return boolean
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 放入 list 缓存
     *
     * @param key   -
     * @param value -
     * @return boolean
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 放入 list 缓存，并设置过期时间
     *
     * @param key   -
     * @param value -
     * @param time  过期时间，单位秒
     * @return boolean
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 根据索引更改 list 中的数据
     *
     * @param key   -
     * @param index -
     * @param value -
     * @return boolean
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 移除 count 个 值为 value
     *
     * @param key   -
     * @param count -
     * @param value -
     * @return long
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 清除指定前缀的缓存
     *
     * @param prefix -
     * @param ids    -
     */
    public void delByKeys(String prefix, Set<Long> ids) {
        Set<Object> keys = Sets.newHashSet();
        for (Long id : ids) {
            keys.addAll(Objects.requireNonNull(redisTemplate.keys(new StringBuffer(prefix).append(id).toString())));
        }
        redisTemplate.delete(keys);
    }
}
