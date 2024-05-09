package hu.novaservices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressHelperTest {

    @Test
    public void addressHelperTest(){
            String address = "József Attila utca 26. B. lp.2.em.8/C.";

            assertEquals(AddressHelper.getFieldValue(address, "hu", "kozternev"),"József Attila");
            assertEquals(AddressHelper.getFieldValue(address, "hu", "kozterjell"),"utca");
            assertEquals(AddressHelper.getFieldValue(address, "hu", "hazszam"),"26");
            assertEquals(AddressHelper.getFieldValue(address, "hu", "epulet"),"");
            assertEquals(AddressHelper.getFieldValue(address, "hu", "lepcsohaz"),"B");
            assertEquals(AddressHelper.getFieldValue(address, "hu", "emelet"),"2");
            assertEquals(AddressHelper.getFieldValue(address, "hu", "ajto"),"8/C");
    }
}
