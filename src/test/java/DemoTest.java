import net.jodah.concurrentunit.ConcurrentTestCase;
import net.jodah.concurrentunit.Waiter;

import org.junit.Test;

public class DemoTest extends ConcurrentTestCase {

    @Test
    public void test1() throws Throwable {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    System.out.println("on test1's resume " + Thread.currentThread().getName());
                    resume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println("on test1's await " + Thread.currentThread().getName());
        // The 'main' thread creates the underlying 'waiter' and calls its 'await' method
        await();
    }

    @Test(timeout = 3000)
    public void test2() throws Throwable {
        final Waiter waiter = new Waiter();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    System.out.println("on test2's resume " + Thread.currentThread().getName());
                    waiter.resume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println("on test2's await " + Thread.currentThread().getName());
        // The 'Time-limited' thread creates the 'waiter' and calls its 'await' method
        waiter.await();
    }

    @Test(timeout = 3000)
    public void test3() throws Throwable {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    System.out.println("on test3's resume " + Thread.currentThread().getName());
                    resume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println("on test3's await " + Thread.currentThread().getName());
        // The 'main' thread creates the underlying 'waiter' but the 'Time-limited' thread calls its 'await' method
        // As a result, it throws 'java.lang.IllegalStateException: Must be called from within the main test thread'
        await();
    }

}
