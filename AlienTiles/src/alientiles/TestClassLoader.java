package alientiles;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TestClassLoader {
    @Test
    public void test() {
        assertNotNull("Unable to find james.txt in classpath", this.getClass().getResourceAsStream("/james.txt"));
        assertNotNull("Unable to find imsInfo.txt in classpath", this.getClass().getResourceAsStream("/images/imsInfo.txt"));
    }
}
