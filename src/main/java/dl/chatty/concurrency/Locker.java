package dl.chatty.concurrency;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Resource synchronizer responsible for locking/unlocking read/write operations
 * 
 * @author Daniel Łaś
 *
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Locker {

    public static <T> T lockRead(Supplier<T> supplier, ReadWriteLock lock) {
        lock.readLock().lock();

        try {
            return supplier.get();
        } finally {
            lock.readLock().unlock();
        }

    }

    public static void lockRead(Runnable runnable, ReadWriteLock lock) {
        lock.readLock().lock();

        try {
            runnable.run();
        } finally {
            lock.readLock().unlock();
        }

    }

    public static <T> T lockWrite(Supplier<T> supplier, ReadWriteLock lock) {
        lock.writeLock().lock();

        try {
            return supplier.get();
        } finally {
            lock.writeLock().unlock();
        }

    }

    public static void lockWrite(Runnable runnable, ReadWriteLock lock) {
        lock.writeLock().lock();

        try {
            runnable.run();
        } finally {
            lock.writeLock().unlock();
        }

    }

}
