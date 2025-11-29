package org.gluu.jsf2.customization;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jakarta.faces.application.ApplicationConfigurationPopulator;

/**
 * Created by eugeniuparvan on 5/1/17.
 */
public class FacesConfigPopulator extends ApplicationConfigurationPopulator {
	/**
	 * <p>
	 * /faces-config/navigation-rule
	 * </p>
	 */
	private static final String NAVIGATION_RULE = "navigation-rule";
	private static final String FACES_CONFIG_PATTERN = ".*\\.navigation\\.xml$";
	private static final String DEFAULT_NAVIGATION_PATH = "META-INF/navigation";

	private Logger log = LoggerFactory.getLogger(FacesConfigPopulator.class);

	@Override
	public void populateApplicationConfiguration(Document toPopulate) {
        try {
			populateNavigationRules(toPopulate);

		    dump(toPopulate);
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			log.error("Failed to build custom faces-config.xml", e);
		}
	}

	// Navigation Rules
	protected void populateNavigationRules(Document toPopulate) throws TransformerFactoryConfigurationError, TransformerException {
		log.debug("Starting configuration populator");

		if (Utils.isCustomPagesDirExists()) {
			String customPath = Utils.getCustomPagesPath();
			log.debug("Adding navigation rules from custom dir folder: {}", customPath);
			try {
				findAndUpdateNavigationRules(toPopulate,  customPath);
			} catch (Exception ex) {
				log.error("Can't add customized navigation rules", ex);
			}
		}

		try {
			log.debug("Adding navigation rules from application resurces");
			Enumeration<URL> urlEnumeration = getClass().getClassLoader().getResources(DEFAULT_NAVIGATION_PATH);
			if (urlEnumeration.hasMoreElements()) {
				URL url = urlEnumeration.nextElement();
				findAndUpdateNavigationRules(toPopulate, url.getPath());
			}
		} catch (Exception ex) {
			log.error("Failed to populate application configuraton", ex);
		}
	}

	/**
	 * Recursively finds all *.navigation.xml files located in custom pages
	 * directory, and adds navigation rules to navigation handler
	 *
	 * @param path to custom pages directory
	 * @throws Exception
	 */
	private void findAndUpdateNavigationRules(Document toPopulate, String path) throws Exception {
        var namespace = toPopulate.getDocumentElement().getNamespaceURI();
        var rootElement = toPopulate.getDocumentElement();

        File file = new File(path);
		RegexFileFilter regexFileFilter = new RegexFileFilter(FACES_CONFIG_PATTERN);
		Collection<File> facesConfigFiles = FileUtils.listFiles(file, regexFileFilter, DirectoryFileFilter.DIRECTORY);
		log.debug("Found '{}' navigation rules files", facesConfigFiles.size());

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		factory.setNamespaceAware(false);

        // Fix XXE vulnerability
		factory.setXIncludeAware(false);
		factory.setExpandEntityReferences(false);
		factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder builder = factory.newDocumentBuilder();

		for (File files : facesConfigFiles) {
			String faceConfig = files.getAbsolutePath();
			updateDocument(toPopulate, namespace, rootElement, builder, faceConfig);
			log.debug("Added navigation rules from {}", faceConfig);
		}
	}

	/**
	 * Validates *.faces-config.xml file and creates DocumentInfo class
	 *
	 * @param toPopulateBuilder
	 * @param faceConfig
	 * @return
	 */
	private void updateDocument(Document toPopulate, String namespace, Element rootElement, DocumentBuilder toPopulateBuilder, String faceConfig) {
		try {
			Document navDoc = toPopulateBuilder.parse(new File(faceConfig));

			Element navDocRoot = navDoc.getDocumentElement();
			NodeList navDocRootChilds = navDocRoot.getChildNodes(); 
			for (int i = 0; i < navDocRootChilds.getLength(); ++i) {
				Node child = navDocRootChilds.item(i);
				if ((child.getNodeType() == Node.ELEMENT_NODE) &&
					(NAVIGATION_RULE.equals(child.getNodeName()))){
					Element importedNode = (Element) toPopulate.importNode(child, true);
					rootElement.appendChild(importedNode);
				}
			}
		} catch (Exception ex) {
			log.error("Failed to update navigation rules", ex);
		}
	}

	private void dump(Node toPopulate) throws TransformerFactoryConfigurationError, TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(toPopulate), new StreamResult(writer));
        String output = writer.getBuffer().toString();
        System.out.println(output);
	}

}
