package handler.impl;

import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlRootElementAnalyzer extends XmlFileHandler {

    protected Set<String> roots = new TreeSet<String>();

    public Set<String> getRoots() {
        return roots;
    }

    @Override
    protected boolean processXml(Document xml) {
        Element element = xml.getDocumentElement();
        String root = element.getNodeName();

        roots.add(root);

        return true;
    }

}
