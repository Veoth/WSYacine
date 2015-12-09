package sallesaffectees.contractfirst;

import junit.framework.TestCase;

import org.w3c.dom.Element;

public class TestXmlHelper extends TestCase {

    private static final String filePath = "SallesAffectees.xml";

    public void testGetRootElementFromFileInClasspath() {
        try {
            Element  element = XmlHelper.getRootElementFromFileInClasspath(filePath);
            assert element != null;
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
