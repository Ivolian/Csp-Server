package com.withub.csp;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.io.Serializable;


public class CacheUtils {

    static CacheManager cacheManager;

    private static void initCache() {
        cacheManager = CacheManager.create();
        Cache memoryOnlyCache = new Cache("memoryCache", 10000, false, false, 600, 600);
        cacheManager.addCache(memoryOnlyCache);
    }

    public static Cache getMemoryCache() {
        if (cacheManager == null) {
            initCache();
        }
        return cacheManager.getCache("memoryCache");
    }

    public static Element getElementByKey(String key) {
        return getMemoryCache().get(key);
    }

    public static void putElement(String key, Serializable value) {
        Element element = new Element(key, value);
        getMemoryCache().put(element);
    }

}
