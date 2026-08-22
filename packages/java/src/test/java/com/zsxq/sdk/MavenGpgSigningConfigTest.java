package com.zsxq.sdk;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Guards the Maven Central signing wiring in the shipped pom.xml.
 * maven-gpg-plugin reads {@code gpg.passphrase} and env {@code MAVEN_GPG_PASSPHRASE};
 * loopback pinentry is required for non-interactive CI/deploy.
 */
class MavenGpgSigningConfigTest {

    @Test
    void gpgPluginBindsPassphrasePropertyAndLoopbackPinentry() throws Exception {
        Path pomPath = locatePom();
        assertTrue(Files.isRegularFile(pomPath), "pom.xml must exist at " + pomPath);

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pomPath.toFile());
        doc.getDocumentElement().normalize();

        Element gpgPlugin = findPlugin(doc, "org.apache.maven.plugins", "maven-gpg-plugin");
        assertNotNull(gpgPlugin, "pom.xml must declare maven-gpg-plugin");

        Element configuration = firstChild(gpgPlugin, "configuration");
        assertNotNull(configuration, "maven-gpg-plugin must have configuration");

        String passphrase = text(firstChild(configuration, "passphrase"));
        assertEquals("${gpg.passphrase}", passphrase,
                "passphrase must bind the gpg.passphrase user property, not a hardcoded secret");
        assertFalse(passphrase.contains("你的口令"), "placeholder passphrase must not be in pom.xml");

        String envName = text(firstChild(configuration, "passphraseEnvName"));
        assertEquals("MAVEN_GPG_PASSPHRASE", envName,
                "passphraseEnvName must be MAVEN_GPG_PASSPHRASE");

        List<String> args = childTexts(firstChild(configuration, "gpgArguments"), "arg");
        assertTrue(args.contains("--pinentry-mode") && args.contains("loopback"),
                "gpgArguments must enable --pinentry-mode loopback for non-interactive signing");

        Element skip = firstChild(configuration, "skip");
        if (skip != null) {
            assertNotEquals("true", text(skip), "GPG signing must not be skipped");
        }

        String skipProperty = text(findProperty(doc, "gpg.skip"));
        assertFalse("true".equalsIgnoreCase(skipProperty), "gpg.skip must not be true");
    }

    private static Path locatePom() {
        Path cwd = Paths.get("").toAbsolutePath();
        Path direct = cwd.resolve("pom.xml");
        if (Files.isRegularFile(direct)) {
            return direct;
        }
        Path nested = cwd.resolve("packages/java/pom.xml");
        if (Files.isRegularFile(nested)) {
            return nested;
        }
        throw new IllegalStateException("Cannot locate pom.xml from " + cwd);
    }

    private static Element findPlugin(Document doc, String groupId, String artifactId) {
        NodeList plugins = doc.getElementsByTagName("plugin");
        for (int i = 0; i < plugins.getLength(); i++) {
            Element plugin = (Element) plugins.item(i);
            if (groupId.equals(text(firstChild(plugin, "groupId")))
                    && artifactId.equals(text(firstChild(plugin, "artifactId")))) {
                return plugin;
            }
        }
        return null;
    }

    private static Element findProperty(Document doc, String name) {
        NodeList properties = doc.getElementsByTagName("properties");
        for (int i = 0; i < properties.getLength(); i++) {
            Element found = firstChild((Element) properties.item(i), name);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static Element firstChild(Element parent, String tag) {
        if (parent == null) {
            return null;
        }
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element
                    && tag.equals(children.item(i).getNodeName())) {
                return (Element) children.item(i);
            }
        }
        return null;
    }

    private static List<String> childTexts(Element parent, String tag) {
        List<String> values = new ArrayList<>();
        if (parent == null) {
            return values;
        }
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element
                    && tag.equals(children.item(i).getNodeName())) {
                values.add(text((Element) children.item(i)));
            }
        }
        return values;
    }

    private static String text(Element element) {
        return element == null ? null : element.getTextContent().trim();
    }
}
