package dl.chatty;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

@RunWith(Parameterized.class)
public abstract class DataTestBase<T> {

    @Parameter
    public EqualityPair<T> testedObject;

    @Test
    public void testEquality() {
        if (testedObject.isEquality()) {
            assertTrue(testedObject.getThisObj().equals(testedObject.getThatObj()));
        } else {
            assertFalse(testedObject.getThisObj().equals(testedObject.getThatObj()));
        }
    }

    @Test
    public void hashCodeShouldBeCalculated() {
        assertNotNull(testedObject.getThisObj().hashCode());
    }

    @Test
    public void hashCodeShouldBeCorrect() {
        if (testedObject.isEquality()) {
            assertTrue(testedObject.getThisObj().hashCode() == testedObject.getThatObj().hashCode());
        } else {
            if (testedObject.getThatObj() != null && testedObject.getThisObj() != null) {
                assertFalse(testedObject.getThisObj().hashCode() == testedObject.getThatObj().hashCode());
            }
        }
    }

    @Test
    public void toStringShouldSuccess() {
        assertNotNull(testedObject.getThisObj().toString());
        assertNotNull(testedObject.getThatObj().toString());
    }

}
