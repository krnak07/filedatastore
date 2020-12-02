import org.json.simple.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testAssert{
    CRD ds ;

    public testAssert() {
        ds = new CRD("test-file.txt");
    }

    @Test
    public void creationTest() {
        JSONObject jObj = new JSONObject();
        jObj.put("test","test");

        String result = ds.create("testin1",jObj,1);
        assertEquals("Created",result);

        result = ds.create("testin2",jObj,120);
        assertEquals("Created",result);

        result = ds.create("testin5",jObj);
        assertEquals("Created",result);

        result = ds.create("testin2",jObj,120);
        assertEquals("error : Key already exists",result);

        result = ds.create("this is a test to test the size of the key",jObj,120);
        assertEquals("error : Key length exceeded 32 characters",result);

    }

    @Test
    public void deletionTest(){
        String result = ds.delete("testin5");
        assertEquals("deleted",result);

        result = ds.delete("testin11");
        assertEquals("error : Key doesn't exists",result);
    }

    @Test
    public void readTest(){
        String result = ds.read("testin2").toString();
        assertEquals("{\"test\":\"test\"}",result);

        result = ds.read("testin5").toString();
        assertEquals("{\"error\":\"Key doesn't exists\"}",result);

        result = ds.read("this is a test to test the size of the key").toString();
        assertEquals("{\"error\":\"length of key is exceeding 32 characters\"}",result);
    }


}
