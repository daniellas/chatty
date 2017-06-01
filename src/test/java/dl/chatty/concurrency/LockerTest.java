package dl.chatty.concurrency;

import static org.mockito.Mockito.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LockerTest {

    @Mock
    private ReadWriteLock lock;

    @Mock
    private Lock readLock;

    @Mock
    private Lock writeLock;

    @Before
    public void before() {
        when(lock.readLock()).thenReturn(readLock);
        when(lock.writeLock()).thenReturn(writeLock);
    }

    @Test
    public void shouldLockUnlockRead() {
        Locker.lockRead(() -> {

        }, lock);

        verify(lock, times(2)).readLock();
        verify(readLock).lock();
        verify(readLock).unlock();
    }

    @Test
    public void shouldLockUnlockWrite() {
        Locker.lockWrite(() -> {

        }, lock);

        verify(lock, times(2)).writeLock();
        verify(writeLock).lock();
        verify(writeLock).unlock();
    }

    @Test
    public void shouldLockUnlockReadOnException() {
        try {
            Locker.lockRead(() -> {
                throw new RuntimeException();
            }, lock);
        } catch (RuntimeException e) {
        }

        verify(lock, times(2)).readLock();
        verify(readLock).lock();
        verify(readLock).unlock();
    }

    @Test
    public void shouldLockUnlockWriteOnException() {
        try {
            Locker.lockWrite(() -> {
                throw new RuntimeException();
            }, lock);
        } catch (RuntimeException e) {
        }

        verify(lock, times(2)).writeLock();
        verify(writeLock).lock();
        verify(writeLock).unlock();
    }

    @Test
    public void shouldLockUnlockReadResult() {
        Locker.lockRead(() -> {
            return 1;
        }, lock);

        verify(lock, times(2)).readLock();
        verify(readLock).lock();
        verify(readLock).unlock();
    }

    @Test
    public void shouldLockUnlockWriteResult() {
        Locker.lockWrite(() -> {
            return 1;
        }, lock);

        verify(lock, times(2)).writeLock();
        verify(writeLock).lock();
        verify(writeLock).unlock();
    }

    @SuppressWarnings("unused")
    @Test
    public void shouldLockUnlockReadResultOnException() {
        try {
            Locker.lockRead(() -> {
                Integer i = 1 / 0;
                return 1;
            }, lock);
        } catch (RuntimeException e) {
        }

        verify(lock, times(2)).readLock();
        verify(readLock).lock();
        verify(readLock).unlock();
    }

    @SuppressWarnings("unused")
    @Test
    public void shouldLockUnlockWriteResultOnException() {
        try {
            Locker.lockWrite(() -> {
                Integer i = 1 / 0;
                return 1;
            }, lock);
        } catch (RuntimeException e) {
        }

        verify(lock, times(2)).writeLock();
        verify(writeLock).lock();
        verify(writeLock).unlock();
    }

}
