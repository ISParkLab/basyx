package org.eclipse.basyx.regression.directory.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.eclipse.basyx.aas.backend.connector.MetaprotocolHandler;
import org.eclipse.basyx.aas.backend.http.tools.GSONTools;
import org.eclipse.basyx.aas.backend.http.tools.factory.DefaultTypeFactory;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.identifier.IdentifierType;
import org.eclipse.basyx.regression.support.server.context.ComponentsRegressionContext;
import org.eclipse.basyx.testsuite.support.backend.servers.AASHTTPServerResource;
import org.eclipse.basyx.tools.aasdescriptor.AASDescriptor;
import org.eclipse.basyx.tools.webserviceclient.WebServiceRawClient;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * Test queries to SQL directory provider.
 * 
 * @author kuhn, ps
 *
 */
public class TestDirectorySQLProvider {

	/**
	 * Makes sure Tomcat Server is started
	 */
	@ClassRule
	public static AASHTTPServerResource res = new AASHTTPServerResource(new ComponentsRegressionContext());

	// Directory web service URL
	public String wsURL;

	/**
	 * GSON instance
	 */
	protected GSONTools serializer = new GSONTools(new DefaultTypeFactory());
	private MetaprotocolHandler handler = new MetaprotocolHandler();

	@Before
	public void setUp() {
		this.wsURL = "http://localhost:8080/basys.components/Testsuite/Directory/SQL";
	}

	/**
	 * Execute test case that test working calls
	 */
	@Test
	public void testGetterCalls() {
		System.out.println("ws url " + wsURL);
		// Invoke BaSyx service calls via web services
		WebServiceRawClient client = new WebServiceRawClient();


		// First test - get all locally registered AAS
		{
			// Get all locally registered AAS
			String result = getResult(client.get(wsURL + "/api/v1/registry"));

			// Check if all AAS are contained in result
			assertTrue(result.contains("content.aas1"));
			assertTrue(result.contains("content.aas2"));
			assertTrue(result.contains("content.aas3"));
			assertTrue(result.contains("content.aas4"));
		}

		// Get a specific AAS (1)
		try {
			// Get a known AAS by its ID
			String result = getResult(client.get(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("microscope#A-19", "UTF-8")));

			System.out.println("Res:" + result);

			// Check if all AAS are contained in result
			assertTrue(result.equals("content.aas1"));
		} catch (Exception e) {
			fail("Get specific AAS test case did throw exception:" + e);
		}

		// Get a specific AAS (2)
		try {
			// Get a known AAS by its ID
			String result = getResult(client.get(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("microscope#A-18", "UTF-8")));

			// Check if all AAS are contained in result
			assertTrue(result.equals("content.aas2"));
		} catch (Exception e) {
			fail("Get specific AAS test case did throw exception:" + e);
		}

		// Get a specific AAS (3)
		try {
			// Get a known AAS by its ID
			String result = getResult(client.get(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("microscope#A-17", "UTF-8")));

			// Check if all AAS are contained in result
			assertTrue(result.equals("content.aas3"));
		} catch (Exception e) {
			fail("Get specific AAS test case did throw exception:" + e);
		}

		// Get a specific AAS (4)
		try {
			// Get a known AAS by its ID
			String result = getResult(client.get(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("microscope#A-16", "UTF-8")));

			// Check if all AAS are contained in result
			assertTrue(result.equals("content.aas4"));
		} catch (Exception e) {
			fail("Get specific AAS test case did throw exception:" + e);
		}
	}

	/**
	 * Execute update test case
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testUpdateCall() throws UnsupportedEncodingException {
		// Invoke BaSyx service calls via web services
		WebServiceRawClient client = new WebServiceRawClient();

		// Update a specific AAS
		// Update AAS registration
		client.put(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("microscope#A-16", "UTF-8"), "content.aas5");

		// Get a known AAS by its ID
		String result = getResult(client.get(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("microscope#A-16", "UTF-8")));
		// - Check updated registration
		assertTrue(result.equals("content.aas5"));

		// Update AAS registration
		client.put(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("microscope#A-16", "UTF-8"), "content.aas4");

		// Get a known AAS by its ID
		String result2 = getResult(client.get(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("microscope#A-16", "UTF-8")));
		// - Check updated registration
		assertTrue(result2.equals("content.aas4"));
	}

	/**
	 * Execute create/Delete test cases
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testCreateDeleteCall() throws UnsupportedEncodingException {
		// Invoke BaSyx service calls via web services
		WebServiceRawClient client = new WebServiceRawClient();


		// Update a specific AAS

		// Delete AAS registration (make sure tests work also iff previous test suite
		// did fail)
		client.delete(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("microscope#A-166", "UTF-8"));

		// Get a known AAS by its ID - check if AAS does not exist already
		String result0 = getResult(client.get(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("microscope#A-166", "UTF-8")));
		// - Check updated registration
		assertEquals(null, result0);

		// Create and register AAS descriptor
		// - Create AAS descriptor
		AASDescriptor aasDescriptor = new AASDescriptor("urn:de.FHG:es.iese:aas:0.98:5:lab/microscope#A-166", IdentifierType.URI, "www.endpoint.de");
		// - Create new AAS registration
		String expected = serializer.serialize(aasDescriptor);
		client.post(wsURL + "/api/v1/registry", expected);

		// Get a known AAS by its ID
		Object result = getResult(client.get(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/"
				+ URLEncoder.encode("microscope#A-166", "UTF-8")));

		assertEquals(aasDescriptor, result); // need deep json string compare here

		// Delete AAS registration
		client.delete(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("microscope#A-166", "UTF-8"));

		// Check if it is really deleted
		String result2 = getResult(client.get(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("microscope#A-166", "UTF-8")));
		// - Check updated registration
		assertEquals(null, result2);
	}

	/**
	 * Execute test case that test non-working calls
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testNonWorkingCalls() throws UnsupportedEncodingException {
		// Invoke service call via web services
		WebServiceRawClient client = new WebServiceRawClient();

		// Get unknown AAS ID
		String result = getResult(client.get(wsURL + "/api/v1/registry/urn:de.FHG:es.iese:aas:0.98:5:lab/" + URLEncoder.encode("unknown", "UTF-8")));

		// Check if no AAS are contained in result
		assertEquals(null, result);
	}

	@SuppressWarnings("unchecked")
	private <T> T getResult(String res) {
		try {
			return (T) handler.verify(res);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}