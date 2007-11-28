package simple.easymock.example;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.not;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;

import junit.framework.TestCase;

import org.easymock.IAnswer;

public class SimpleEasyMockTest extends TestCase {

    private interface BackendSystem {
        String getCurrentYear();

        String getFirstName(int id);

        String getLastName(int id);

        int[] reorder(int[] id);
    }

    private static class ClassToTest {
        private BackendSystem backend;

        private String convertToNameCase(String name) {
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }

        private String convertToNameCaseSlow(String name) {
            return String.format("%1$S%2$s", name.charAt(0), name.substring(1));
        }

        public BackendSystem getBackend() {
            return backend;
        }

        public int getCurrentYear() {
            String rawY2KUnsafeYear = getBackend().getCurrentYear();

            int y2kUnsafeYear = Integer.parseInt(rawY2KUnsafeYear);

            if (y2kUnsafeYear < 72) {
                return 2000 + y2kUnsafeYear;
            } else {
                return 1900 + y2kUnsafeYear;
            }
        }

        public String getStandardName(int id) {
            try {
                String lastName = getBackend().getLastName(id);
                String firstName = getBackend().getFirstName(id);

                firstName = convertToNameCase(firstName);
                lastName = convertToNameCase(lastName);

                return firstName + " " + lastName;
            } catch (RuntimeException e) {
                return "";
            }
        }

        public int[] reorder(int[] id) {
            return getBackend().reorder(id);
        }

        // public String[] getStandardNames(int[] ids) {
        // List<String> names = new ArrayList<String>();
        //
        // for (int i = 0; i < ids.length; i++) {
        // names.add(getStandardName(ids[i]));
        // }
        //
        // String result[] = new String[names.size()];
        // return (String[]) names.toArray(result);
        // }

        public void setBackend(BackendSystem functions) {
            this.backend = functions;
        }
    }

    private BackendSystem mock;

    private ClassToTest testClass;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mock = createMock(BackendSystem.class);
        testClass = new ClassToTest();
        testClass.setBackend(mock);
    }

    public void testConvertToNameCase() {
        long before = System.nanoTime();
        String orig = testClass.convertToNameCase("james");
        long origTime = System.nanoTime() - before;

        before = System.nanoTime();
        String alt = testClass.convertToNameCaseSlow("james");

        long slowTime = System.nanoTime() - before;

        assertTrue(origTime < slowTime);
        assertEquals(orig, alt);
    }

    public void testCurrentYear() {

        expect(mock.getCurrentYear()).andReturn("06");
        replay(mock);

        int curYear = testClass.getCurrentYear();

        verify(mock);

        assertEquals(curYear, 2006);
    }

    public void testDynamicTypes() {
        expect(mock.getFirstName(1)).andAnswer(new IAnswer<String>() {
            public String answer() throws Throwable {
                return "dynamic";
            }
        });

        expect(mock.getLastName(1)).andAnswer(new IAnswer<String>() {
            public String answer() throws Throwable {
                return "name";
            }
        });

        replay(mock);

        String name = testClass.getStandardName(1);

        verify(mock);

        assertEquals(name, "Dynamic Name");
    }

    public void testName() {

        expect(mock.getFirstName(1)).andReturn("james").atLeastOnce();
        expect(mock.getLastName(1)).andReturn("dunwoody").times(1, 1);

        replay(mock);

        String name = testClass.getStandardName(1);

        verify(mock);
        assertEquals(name, "James Dunwoody");
    }

    public void testReorder() {

        expect(mock.reorder(aryEq(new int[] { 1, 2 }))).andReturn(new int[] { 1, 2 });

        replay(mock);

        int reordered[] = testClass.reorder(new int[] { 1, 2 });

        verify(mock);
        assertTrue(Arrays.equals(reordered, new int[] { 1, 2 }));
    }

    public void testStub() {

        expect(mock.getCurrentYear()).andStubReturn("06");
        expect(mock.getFirstName(not(eq(2)))).andStubReturn("james");
        expect(mock.getLastName(not(eq(2)))).andStubReturn("dunwoody");

        replay(mock);

        int cur = testClass.getCurrentYear();
        testClass.getCurrentYear();

        testClass.getStandardName(1);

        verify(mock);

        assertEquals(cur, 2006);
    }

    public void testThrows() {
        expect(mock.getLastName(0)).andThrow(new RuntimeException());

        replay(mock);

        testClass.getStandardName(0);

        verify(mock);

    }
}
