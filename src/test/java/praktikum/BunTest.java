package praktikum;

import org.junit.Test;
import static org.junit.Assert.*;

public class BunTest {

    @Test
    public void shouldReturnCorrectData() {
        Bun bun = new Bun("Black Bun", 100F);
        assertEquals("Black Bun", bun.getName());
        assertEquals(100F, bun.getPrice(), 0.001);
    }

}