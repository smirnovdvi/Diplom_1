package praktikum;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BurgerTest {

    @Mock
    private Bun mockedBun;

    @Mock
    private Ingredient cheese;

    @Mock
    private Ingredient ham;

    @Mock
    private Ingredient ketchup;

    @InjectMocks
    private Burger burger;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setup() {
        burger.setBuns(mockedBun);
    }

    /**
     * Вспомогательный метод для создания стандартного списка ингредиентов.
     */
    private List<Ingredient> createStandardIngredientsList() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(cheese);
        ingredients.add(ham);
        ingredients.add(ketchup);
        return ingredients;
    }

    /**
     * Приводит все переводы строк к единой форме '\n' для теста с чеком.
     */
    private String normalizeLineSeparators(String input) {
        return input.replaceAll("\\r\\n", "\n").replaceAll("\r", "");
    }

    /**
     * Проверка корректности удаления ингредиентов из бургера.
     */

    @Test
    public void shouldHandleSequentialRemovals() {
        // Создаём начальный список с тремя ингредиентами
        burger.ingredients = createStandardIngredientsList();

        // Проверяем изначальное состояние
        assertEquals(3, burger.ingredients.size());
        assertEquals(cheese, burger.ingredients.get(0));
        assertEquals(ham, burger.ingredients.get(1));
        assertEquals(ketchup, burger.ingredients.get(2));

        // Удаляем средний ингредиент (ham)
        burger.removeIngredient(1);

        // Проверяем изменения после первого удаления на корректность изменения последовательности ингредиентов и их индексов
        assertEquals(2, burger.ingredients.size());
        assertEquals(cheese, burger.ingredients.get(0));
        assertEquals(ketchup, burger.ingredients.get(1));

        // Удаляем первый оставшийся ингредиент (cheese)
        burger.removeIngredient(0);

        // Проверяем изменение после второго удаления
        assertEquals(1, burger.ingredients.size());
        assertEquals(ketchup, burger.ingredients.get(0));

        // Удаляем последний оставшийся ингредиент (tomato)
        burger.removeIngredient(0);

        // Проверяем, что список пуст
        assertTrue(burger.ingredients.isEmpty());
    }

    /**
     * Проверяем корректность переноса ингредиента на другую позицию.
     */
    @Test
    public void shouldMoveIngredientForward() {
        burger.ingredients = createStandardIngredientsList();

        // Проверим первоначальное расположение
        assertEquals(cheese, burger.ingredients.get(0));
        assertEquals(ham, burger.ingredients.get(1));
        assertEquals(ketchup, burger.ingredients.get(2));

        // Перемещаем сыр (0-й элемент) на третью позицию
        burger.moveIngredient(0, 2);

        // Проверяем конечное положение
        assertEquals(ham, burger.ingredients.get(0));
        assertEquals(ketchup, burger.ingredients.get(1));
        assertEquals(cheese, burger.ingredients.get(2));
    }

    /**
     * Проверяет защиту от выхода за границу (невозможно перенести элемент за пределы списка).
     */
    @Test
    public void shouldPreventMovingOutsideRange() {
        burger.ingredients = createStandardIngredientsList();

        // Попытаемся переместить элемент за границу
        try {
            burger.moveIngredient(0, 3); // Пытаемся переместить элемент за границу
            fail("Expected IndexOutOfBoundsException to be thrown.");
        } catch (IndexOutOfBoundsException e) {
            // Нормально, исключение было брошено
        }
    }

    /**
     * Проверяем формирование чека с несколькими ингредиентами.
     */
    @Test
    public void shouldGenerateReceiptWithMultipleIngredients() {
        when(mockedBun.getName()).thenReturn("Карамельная булка");
        when(mockedBun.getPrice()).thenReturn(100F);

        when(cheese.getName()).thenReturn("Сыр");
        when(cheese.getType()).thenReturn(IngredientType.FILLING);
        when(cheese.getPrice()).thenReturn(50F);

        when(ketchup.getName()).thenReturn("Кетчуп");
        when(ketchup.getType()).thenReturn(IngredientType.SAUCE);
        when(ketchup.getPrice()).thenReturn(20F);

        when(ham.getName()).thenReturn("Ветчина");
        when(ham.getType()).thenReturn(IngredientType.FILLING);
        when(ham.getPrice()).thenReturn(70F);

        burger.ingredients = createStandardIngredientsList();

        // Формируем чек
        String receipt = normalizeLineSeparators(burger.getReceipt());

        // Проверяем содержимое чека
        assertEquals(
                "(==== Карамельная булка ====)\n" +
                        "= filling Сыр =\n" +
                        "= filling Ветчина =\n" +
                        "= sauce Кетчуп =\n" +
                        "(==== Карамельная булка ====)\n\n" +
                        "Price: 340,000000\n",
                receipt
        );
    }

}