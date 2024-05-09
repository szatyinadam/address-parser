package hu.novaservices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressParserTest {

    @Test
    public void addressHelperTest() {
        AddressParser addressParser = new AddressParser(HungarianLanguageSettings.langSettings);
        String address = "József Attila utca 26. B. lp.2.em.8/C.";

        assertEquals(addressParser.getFieldValue(address, "hu", "kozternev"), "József Attila");
        assertEquals(addressParser.getFieldValue(address, "hu", "kozterjell"), "utca");
        assertEquals(addressParser.getFieldValue(address, "hu", "hazszam"), "26");
        assertEquals(addressParser.getFieldValue(address, "hu", "epulet"), "");
        assertEquals(addressParser.getFieldValue(address, "hu", "lepcsohaz"), "B");
        assertEquals(addressParser.getFieldValue(address, "hu", "emelet"), "2");
        assertEquals(addressParser.getFieldValue(address, "hu", "ajto"), "8/C");
    }
}
