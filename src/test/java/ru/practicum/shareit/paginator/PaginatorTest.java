package ru.practicum.shareit.paginator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.InvalidPageParametersException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaginatorTest {

    Paginator<Integer> paginator = new Paginator<>();
    List<Integer> objects;

    @BeforeEach
    public void before() {
        objects = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    @Test
    public void givenPageAndCollection_whenFromAndSizeAreDefault_thenReturnAllCollection() {
        Page page = new Page(-1, -1);
        assertEquals(10, paginator.paginate(page, objects).size());
        assertEquals(objects, paginator.paginate(page, objects));
    }

    @Test
    public void givenPageAndCollection_whenFromBelowZero_thenThrowInvalidPageParametersException() {
        Page page = new Page(-1, 2);
        assertThrows(InvalidPageParametersException.class, () -> paginator.paginate(page, objects));
    }

    @Test
    public void givenPageAndCollection_whenSizeBelowZero_thenThrowInvalidPageParametersException() {
        Page page = new Page(2, -1);
        assertThrows(InvalidPageParametersException.class, () -> paginator.paginate(page, objects));
    }

    @Test
    public void givenPageAndCollection_whenCollectionIsEmpty_thenReturnEmptyCollection() {
        Page page = new Page(2, 2);
        assertEquals(0, paginator.paginate(page, Collections.emptyList()).size());
        assertTrue(paginator.paginate(page, Collections.emptyList()).isEmpty());
    }

    @Test
    public void givenPageAndCollection_whenFromBiggerThanObjectsSize_thenThrowInvalidPageParametersException() {
        Page page = new Page(12, 5);
        assertThrows(InvalidPageParametersException.class, () -> paginator.paginate(page, objects));
    }

    @Test
    public void givenPageAndCollection_whenPaginate_thenReturnRequiredCollection() {
        Page page = new Page(5, 2);
        assertEquals(2, paginator.paginate(page, objects).size());
        assertEquals(6, paginator.paginate(page, objects).get(0));
        assertEquals(7, paginator.paginate(page, objects).get(1));
    }
}