package praktikum;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class BurgerParametrizedTest {

    @Mock
    private Bun mockedBun;

    @Mock
    private Ingredient mockedIngredient;

    @InjectMocks
    private Burger burger;

    private float expectedTotalPrice;
    private int numberOfIngredients;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    public BurgerParametrizedTest(float expectedTotalPrice, int numberOfIngredients) {
        this.expectedTotalPrice = expectedTotalPrice;
        this.numberOfIngredients = numberOfIngredients;
    }

    @Before
    public void setup() {
        when(mockedBun.getPrice()).thenReturn(100F);
        when(mockedIngredient.getPrice()).thenReturn(50F);
        burger.setBuns(mockedBun);

        // Добавляем нужное количество ингредиентов
        for (int i = 0; i < numberOfIngredients; i++) {
            burger.addIngredient(mockedIngredient);
        }
    }

    @Parameterized.Parameters(name = "{index}: totalPrice={0}, numIngredients={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{250F, 1}, // двойная булочка + 1 ингредиент => 100+100+50=250₽
                new Object[]{400F, 4}, // двойная булочка + 4 ингредиента => 100+100+4*50=400₽
                new Object[]{200F, 0}  // двойная булочка без ингредиентов => 200₽
        );
    }
    //Проверяем корректность формирования цены для бургера разной конфигурации
    @Test
    public void calculateTotalPrice() {
        assertEquals(getClass().getSimpleName() + ": Total Price is incorrect",
                expectedTotalPrice, burger.getPrice(), 0.001F);
    }
}