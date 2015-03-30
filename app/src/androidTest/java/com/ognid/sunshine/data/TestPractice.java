package com.ognid.sunshine.data;

import android.test.AndroidTestCase;

public class TestPractice extends AndroidTestCase {
    /*
        This gets run before every test.
     */

    Wrapper w;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        w=new Wrapper();
        w.x=10;
       // x=10;
    }

    public void testThatDemonstratesAssertions() throws Throwable {
        int a = 5;
        int b = 3;
        int c = 5;
        int d = 10;

        assertEquals("X should be equal", a, c);
        assertTrue("Y should be true", d > a);
        assertFalse("Z should be false", a == b);

        if (b > d) {
            fail("XX should never happen");
        }
    }

    public void test1(){
        assertTrue(w.x==10);
        w.x=90;
    }

    public void test2(){
        assertTrue("message 1",w.x==10);
        test1();
        assertTrue("message 2",w.x==10);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

class Wrapper{
    public int x;

}