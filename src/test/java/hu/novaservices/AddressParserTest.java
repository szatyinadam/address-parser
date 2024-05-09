package hu.novaservices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressParserTest {

    @Test
    public void addressHelperTest() {
        String address = "József Attila utca 26. B. lp.2.em.8/C.";
        AddressParser addressParser = new AddressParser(HungarianLanguageSettings.langSettings, address);

        assertEquals(addressParser.getFieldValue("kozternev"), "József Attila");
        assertEquals(addressParser.getFieldValue("kozterjell"), "utca");
        assertEquals(addressParser.getFieldValue("hazszam"), "26");
        assertEquals(addressParser.getFieldValue("epulet"), "");
        assertEquals(addressParser.getFieldValue("lepcsohaz"), "B");
        assertEquals(addressParser.getFieldValue("emelet"), "2");
        assertEquals(addressParser.getFieldValue("ajto"), "8/C");
    }
}
