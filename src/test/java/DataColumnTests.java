import io.vavr.collection.List;
import io.vavr.collection.Vector;
import io.vavr.control.Try;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit Tests for the DataColumn class.
 * Created by Martin on 13/07/2017.
 */
public class DataColumnTests {

    @Test
    public void testEmptyDataColumnCreation() {
        DataColumn<String> column = new DataColumn<>(String.class, "StringCol");

        assertEquals(column.name(), "StringCol");
        assertEquals(column.type(), String.class);
    }

    @Test
    public void testArrayDataColumnCreation() {
        String[] data = new String[] { "AA", "BB", "CC" };
        DataColumn<String> column = new DataColumn<>(String.class, "StringCol", data);

        assertEquals(column.data().length(), 3);
        assertEquals(column.data().get(1), "BB");
    }

    @Test
    public void testIterableDataColumnCreation() {
        List<String> data = List.of("AA", "BB", "CC");
        DataColumn<String> column = new DataColumn<>(String.class, "StringCol", data);

        assertEquals(column.data().length(), 3);
        assertEquals(column.data().get(1), "BB");
    }

    @Test
    public void testVectorDataColumnCreation() {
        Vector<Integer> data = Vector.of(5, 7, 9);
        DataColumn<Integer> column = new DataColumn<>(Integer.class, "IntegerCol", data);

        assertEquals(column.data().length(), 3);
        assertTrue(column.data().get(1) == 7);
    }

    @Test
    public void testDataColumnAdd() {
        DataColumn<String> column = createStringColumn();
        Try<DataColumn<String>> newCol = column.addItem("NewString");

        assertTrue(newCol.isSuccess());
        assertEquals(column.data().length(), 3);
        assertEquals(newCol.get().data().length(), 4);
        assertEquals(newCol.get().data().get(3), "NewString");
    }

    @Test
    public void testDataColumnAddNull() {
        DataColumn<String> column = createStringColumn();
        Try<DataColumn<String>> newCol = column.addItem(null);

        assertTrue(newCol.isSuccess());
        assertEquals(column.data().length(), 3);
        assertEquals(newCol.get().data().length(), 4);
        assertEquals(newCol.get().data().get(3), null);
    }

    @Test
    public void testDataColumnAddInvalidValueType() {
        DataColumn<Integer> column = createIntegerColumn();
        Try<IDataColumn> newCol = column.add("Invalid Type Value");

        //Assert inserting a string value into an integer column fails.
        assertTrue(newCol.isFailure());
    }

    @Test
    public void testValidToTypedColumn() {
        IDataColumn column = createIntegerColumn();
        Try<DataColumn<Integer>> typedCol = column.asType(Integer.class);

        assertTrue(typedCol.isSuccess());
        assertTrue(typedCol.get().data().get(1) == 7);
    }

    @Test
    public void testInvalidToTypedColumn() {
        IDataColumn column = createIntegerColumn();
        Try<DataColumn<Double>> typedCol = column.asType(Double.class);

        assertTrue(typedCol.isFailure());
        assertTrue(typedCol.getCause().getMessage().equals("Column type doesn't match type requested."));
    }

    private DataColumn<String> createStringColumn() {
        List<String> data = List.of("AA", "BB", "CC");
        return new DataColumn<>(String.class, "StringCol", data);
    }

    private DataColumn<Integer> createIntegerColumn() {
        List<Integer> data = List.of(5, 7, 9);
        return new DataColumn<>(Integer.class, "IntegerCol", data);
    }
}
