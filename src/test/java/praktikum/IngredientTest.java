package praktikum;

import org.junit.Test;
import static org.junit.Assert.*;

public class IngredientTest {

    @Test
    public void shouldReturnCorrectData() {
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "Hot Sauce", 50F);
        assertEquals(IngredientType.SAUCE, ingredient.getType());
        assertEquals("Hot Sauce", ingredient.getName());
        assertEquals(50F, ingredient.getPrice(), 0.001);
    }

}