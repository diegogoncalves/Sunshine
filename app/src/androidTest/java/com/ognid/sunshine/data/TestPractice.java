package com.ognid.sunshine.data;

import android.test.AndroidTestCase;

import java.sql.Wrapper;

public class TestPractice extends AndroidTestCase {
    /*
        This gets run before every test.
     */

    Wrapper w;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
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




    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

