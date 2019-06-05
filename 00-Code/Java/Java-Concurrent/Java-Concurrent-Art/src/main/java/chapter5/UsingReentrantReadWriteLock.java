package chapter5;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class UsingReentrantReadWriteLock {

    private static Map<String, String> cache = new HashMap<>();

    static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    static Lock read = readWriteLock.readLock();
    static Lock write = readWriteLock.writeLock();

    public static String getCache(String key) {
        read.lock();
        try {
            return cache.get(key);
        } finally {
            read.unlock();
        }
    }

    public static String putCache(String key, String value) {
        write.lock();
        try {
            return cache.put(key, value);
        } finally {
            write.unlock();
        }
    }

    public static void clear() {
        write.lock();
        try {
            cache.clear();
        } finally {
            write.unlock();
        }
    }

}
