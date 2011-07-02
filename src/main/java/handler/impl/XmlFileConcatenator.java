/**
 * 
 */
package handler.impl;

import handler.IExecutable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 13 июня 2011
 */
public class XmlFileConcatenator extends XmlFileHandler implements IExecutable {

    private static Logger                                logger     = Logger.getLogger(XmlFileConcatenator.class);

    protected HashMap<String, HashMap<String, Document>> xmlStorage = new HashMap<String, HashMap<String, Document>>();

    protected class XmlStorageItem {

    }

    @Override
    protected boolean processXmlFile() {
        Element contentElement = getContentElement();

        String tag = contentElement.getTagName();
        if (tag.equals("")) {
            // @todo log this
            return false;
        }

        String id = contentElement.getAttribute("id");
        if (id.equals("")) {
            // @todo log this
            return false;
        }

        if (!xmlStorage.containsKey(tag)) {
            HashMap<String, Document> newHashMap = new HashMap<String, Document>();
            xmlStorage.put(tag, newHashMap);
        }

        HashMap<String, Document> hashMap = xmlStorage.get(tag);

        if (hashMap.containsKey(id)) {
            logger.info(String.format("processXmlFile FAIL (dublication) for file : %1$s and %2$s::%3$s",
                    file.getAbsolutePath(), tag, id));

            return false;
        }

        xmlStorage.get(tag).put(id, xml);

        return true;
    }

    protected Element getContentElement() {
        return xml.getDocumentElement();
    }

    @Override
    public boolean execute() {
        Iterator<Entry<String, HashMap<String, Document>>> iterator = xmlStorage.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, HashMap<String, Document>> entry = iterator.next();
            System.out.println(entry.getKey() + "-->" + entry.getValue());

            for (String primaryKey : xmlStorage.keySet()) {
                // calculation
                System.out.print(primaryKey);
                if (primaryKey == entry.getKey()) {
                    continue;
                }

                System.out.print("!!!!!");
            }
        }

        return true;
    }

    protected boolean saveToFile() {
        return true;
    }
}
