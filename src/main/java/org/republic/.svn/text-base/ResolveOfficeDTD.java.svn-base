/*
 * Created on 24-Jun-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.republic;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import java.io.StringReader;

public class ResolveOfficeDTD implements EntityResolver {
    public InputSource resolveEntity (String publicId, String sysId)
    {
        if (sysId.toLowerCase().endsWith(".dtd"))
        {
            StringReader stringInput =
                new StringReader(" ");
            return new InputSource(stringInput);
        }
        else
        {
            return null;    // default behavior
        }
    }
}