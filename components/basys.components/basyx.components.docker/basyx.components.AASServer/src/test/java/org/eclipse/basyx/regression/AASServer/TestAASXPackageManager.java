package org.eclipse.basyx.regression.AASServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.eclipse.basyx.aas.factory.aasx.AASXFactory;
import org.eclipse.basyx.aas.factory.aasx.InMemoryFile;
import org.eclipse.basyx.aas.metamodel.api.IAssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.api.parts.asset.AssetKind;
import org.eclipse.basyx.aas.metamodel.api.parts.asset.IAsset;
import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.descriptor.ModelUrn;
import org.eclipse.basyx.aas.metamodel.map.parts.Asset;
import org.eclipse.basyx.components.aas.aasx.AASXPackageManager;
import org.eclipse.basyx.submodel.metamodel.api.ISubModel;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IdentifierType;
import org.eclipse.basyx.submodel.metamodel.api.parts.IConceptDescription;
import org.eclipse.basyx.submodel.metamodel.api.reference.IKey;
import org.eclipse.basyx.submodel.metamodel.api.reference.IReference;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.ISubmodelElement;
import org.eclipse.basyx.submodel.metamodel.map.SubModel;
import org.eclipse.basyx.submodel.metamodel.map.reference.Reference;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.SubmodelElementCollection;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.File;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.valuetypedef.PropertyValueTypeDef;
import org.eclipse.basyx.support.bundle.AASBundle;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * J-Unit tests for AASx package explorer. This test checks the parsing of aas,
 * submodels, assets and concept-descriptions. it also checks whether the aas
 * have correct references to the asets and submodels
 * 
 * @author zhangzai, conradi
 *
 */
public class TestAASXPackageManager {
	/**
	 * path to the aasx package
	 */
	private static final String aasxPath = "aasx/01_Festo.aasx";
	
	private static final String CREATED_AASX_PATH = "test.aasx";

	/**
	 * the aasx package converter
	 */
	private static AASXPackageManager packageConverter;

	/**
	 * this string array is used to check the refs to submodels of aas
	 */
	private String[] submodelids = { "www.company.com/ids/sm/6053_5072_7091_5102", "smart.festo.com/demo/sm/instance/1/1/13B7CCD9BF7A3F24", "www.company.com/ids/sm/4343_5072_7091_3242", "www.company.com/ids/sm/2543_5072_7091_2660",
			"www.company.com/ids/sm/6563_5072_7091_4267"
			
	};
	
	/**
	 * Files that are unzipped
	 */
	private static String[] unzipFiles = { "target/files/aasx/Document/docu.pdf", "target/files/icon.png"
	};

	/**
	 * AAS bundle which will be generated by the XMLAASBundleFactory
	 */
	private Set<AASBundle> aasBundles;

	/**
	 * Submodels parsed by the converter
	 */
	private Set<ISubModel> submodels;

	/**
	 * Initialize the AASX package converter
	 */
	@BeforeClass
	public static void setup() {
		// Create the aasx package converter with the path to the aasx package
		packageConverter = new AASXPackageManager(aasxPath);
	}

	/**
	 * Test parsing of aas, assets, submodels and concept-descriptions
	 */
	@Test
	public void testCheckAasxConverter() {
		// Parse aas from the XML and create the AAS Bundle with refs to submodels
		try {
			aasBundles = packageConverter.retrieveAASBundles();
		} catch (ParserConfigurationException | SAXException | IOException | InvalidFormatException e) {
			e.printStackTrace();
		}

		// check the information in the aas bundles
		checkAASs(aasBundles);

		// Check the submodels
		checkSubmodels(submodels);
	}
	
	
	/**
	 * Creates a new .aasx using the AASXFactory and tries to parse it
	 */
	@Test
	public void testLoadGeneratedAASX()
			throws InvalidFormatException, IOException, ParserConfigurationException, SAXException, TransformerException, URISyntaxException {
		
		List<IAssetAdministrationShell> aasList = new ArrayList<>();
		List<ISubModel> submodelList = new ArrayList<>();
		List<IAsset> assetList = new ArrayList<>();
		List<IConceptDescription> conceptDescriptionList = new ArrayList<>();

		List<InMemoryFile> fileList = new ArrayList<>();
		
		Asset asset = new Asset("asset-id", new ModelUrn("ASSET_IDENTIFICATION"), AssetKind.INSTANCE);
		AssetAdministrationShell aas = new AssetAdministrationShell("aasIdShort", new ModelUrn("aasId"), asset);
		aas.setAssetReference((Reference) asset.getReference());
		
		SubModel sm = new SubModel("smIdShort", new ModelUrn("smId"));
		
		// Create File SubmodelElements
		File file1 = new File("/icon.png", "image/png");
		file1.setIdShort("file1");
		File file2 = new File("/aasx/Document/docu.pdf", "application/pdf");
		file2.setIdShort("file2");
		
		SubmodelElementCollection collection = new SubmodelElementCollection("Marking_RCM");
		collection.addSubModelElement(file1);
		
		sm.addSubModelElement(collection);
		sm.addSubModelElement(file2);
		aas.addSubModel(sm);
		
		aasList.add(aas);
		submodelList.add(sm);
		assetList.add(asset);

		// Build InMemoryFiles for .aasx
		byte[] content1 = {5,6,7,8,9};
		InMemoryFile file = new InMemoryFile(content1, "/icon.png");
		fileList.add(file);
		
		byte[] content2 = {10,11,12,13,14};
		file = new InMemoryFile(content2, "aasx/Document/docu.pdf");
		fileList.add(file);
		
		// Build AASX
		FileOutputStream out = new FileOutputStream(CREATED_AASX_PATH);
		AASXFactory.buildAASX(aasList, assetList, conceptDescriptionList, submodelList, fileList, out);
		
		AASXPackageManager packageManager = new AASXPackageManager(CREATED_AASX_PATH);
		
		checkBundle(packageManager.retrieveAASBundles(), aas, sm);
		
		// Unzip files from the .aasx
		packageManager.unzipRelatedFiles();
		
		// Check if all expected files are present
		for(String path: unzipFiles) {
			assertTrue(new java.io.File(path).exists());
		}
		
	}
	
	private void checkBundle(Set<AASBundle> bundles, IAssetAdministrationShell aas, ISubModel sm) {
		assertEquals(1, bundles.size());
		AASBundle bundle = bundles.stream().findFirst().get();
		
		IAssetAdministrationShell parsedAAS = bundle.getAAS();
		assertEquals(aas.getIdShort(), parsedAAS.getIdShort());
		assertEquals(aas.getIdentification().getId(), parsedAAS.getIdentification().getId());
		
		assertEquals(1, bundle.getSubmodels().size());
		ISubModel parsedSubmodel = bundle.getSubmodels().stream().findFirst().get();
		assertEquals(sm.getIdShort(), parsedSubmodel.getIdShort());
		assertEquals(sm.getIdentification().getId(), parsedSubmodel.getIdentification().getId());
		assertEquals(sm.getSubmodelElements().size(), parsedSubmodel.getSubmodelElements().size());
	}
	

	/**
	 * Check the parsed aas with expected ones
	 * 
	 * @param aasList
	 */
	private void checkAASs(Set<AASBundle> aasBundles) {
		assertEquals(2, aasBundles.size());

		IAssetAdministrationShell aas = null;

		// select the AAS with a specific ID from the list
		Optional<AASBundle> testAASBundleOptional = aasBundles.stream().filter(b -> b.getAAS().getIdShort().equals("Festo_3S7PM0CP4BD")).findFirst();
		// verify there exist one aas with this ID
		assertTrue(testAASBundleOptional.isPresent());

		// Get the aasbundle from the filtered results
		AASBundle testAASBundle = testAASBundleOptional.get();
		aas = testAASBundle.getAAS();

		// get submodels of this aas
		submodels = testAASBundle.getSubmodels();

		// verify short id
		assertEquals("Festo_3S7PM0CP4BD", aas.getIdShort());
		assertEquals("CONSTANT", aas.getCategory());

		// verify id and id-type
		assertEquals("smart.festo.com/demo/aas/1/1/454576463545648365874", aas.getIdentification().getId());
		assertEquals(IdentifierType.IRI, aas.getIdentification().getIdType());


		// Get submodel references
		Collection<IReference> references = aas.getSubmodelReferences();

		// this aas has 5 submodels
		assertEquals(5, references.size());
		List<IReference> referencelist = new ArrayList<>();
		referencelist.addAll(references);

		// sort the list for later assertion
		// list is sorted by the last two characters of the id
		referencelist.sort((x, y) -> {
			String idx = x.getKeys().get(0).getValue();
			String idy = y.getKeys().get(0).getValue();

			String idx_end = idx.substring(idx.length() - 2);
			int idxint = Integer.parseInt(idx_end);
			String idy_end = idy.substring(idy.length() - 2);
			int idyint = Integer.parseInt(idy_end);

			return idxint - idyint;

		});

		// get First submodel reference
		for (int i = 0; i < referencelist.size(); i++) {
			IReference ref = referencelist.get(i);
			List<IKey> refKeys = ref.getKeys();

			// assert the submodel id
			assertEquals(submodelids[i], refKeys.get(0).getValue());
			// assert the id type
			assertEquals("IRI", refKeys.get(0).getIdType().name());
			// assert the model type
			assertEquals("SUBMODEL", refKeys.get(0).getType().name());
			// submodels are local
			assertEquals(true, refKeys.get(0).isLocal());
		}

	}

	/**
	 * Check parsed submodels with expected ones
	 * 
	 * @param submodels
	 */
	private void checkSubmodels(Set<ISubModel> submodels) {
		assertEquals(5, submodels.size());

		// filter the submodel with id "Nameplate"
		Optional<ISubModel> sm1Optional = submodels.stream().filter(s -> s.getIdShort().equals("Nameplate")).findFirst();
		assertTrue(sm1Optional.isPresent());
		ISubModel sm1 = sm1Optional.get();

		// verify short id, id-type, id and model-kind of the submodel
		assertEquals("Nameplate", sm1.getIdShort());
		assertEquals("IRI", sm1.getIdentification().getIdType().name());
		assertEquals("www.company.com/ids/sm/4343_5072_7091_3242", sm1.getIdentification().getId());
		assertEquals("Instance", sm1.getModelingKind().toString());

		// ---------------------------------------------
		// get 1st submodel element
		// Get submodel elements
		Map<String, ISubmodelElement> smElements = sm1.getSubmodelElements();

		// get element manufacturing name
		ISubmodelElement sele = smElements.get("ManufacturerName");

		// verify short id
		assertEquals("ManufacturerName", sele.getIdShort());

		// verify category and model-kind, value and value-type
		assertEquals("PARAMETER", sele.getCategory());
		assertTrue(sele.getModelingKind().name().equalsIgnoreCase("Instance"));
		Property prop = (Property) sele;
		assertEquals("Festo AG & Co. KG", prop.get());
		assertEquals(PropertyValueTypeDef.String, prop.getValueType());

		// get semantic id
		IReference semantic = sele.getSemanticId();

		IKey semanticKey = semantic.getKeys().get(0);
		assertTrue(semanticKey.getType().name().equalsIgnoreCase("ConceptDescription"));
		assertEquals("IRDI", semanticKey.getIdType().name());
		assertEquals("0173-1#02-AAO677#002", semanticKey.getValue());
		assertEquals(true, semanticKey.isLocal());

		/// ---------------------------------------------
		// get 2nd submodel element
		// Get submodel elements
		sele = smElements.get("ManufacturerProductDesignation");
		assertEquals("ManufacturerProductDesignation", sele.getIdShort());
		assertEquals("PARAMETER", sele.getCategory());
		assertTrue(sele.getModelingKind().name().equalsIgnoreCase("Instance"));
		prop = (Property) sele;
		assertEquals("OVEL Vacuum generator", prop.get());
		assertEquals(PropertyValueTypeDef.String, prop.getValueType());

		// get semantic id
		semantic = sele.getSemanticId();
		semanticKey = semantic.getKeys().get(0);
		assertTrue(semanticKey.getType().name().equalsIgnoreCase("ConceptDescription"));
		assertEquals("IRDI", semanticKey.getIdType().name());
		assertEquals("0173-1#02-AAW338#001", semanticKey.getValue());
		assertEquals(true, semanticKey.isLocal());

		// ---------------------------------------------
		// get 3rd submodel element
		// Get submodel elements
		sele = smElements.get("PhysicalAddress");
		assertEquals("PhysicalAddress", sele.getIdShort());
		assertEquals("PARAMETER", sele.getCategory());
		assertTrue(sele.getModelingKind().name().equalsIgnoreCase("Instance"));

		// get semantic id
		semantic = sele.getSemanticId();
		semanticKey = semantic.getKeys().get(0);
		assertTrue(semanticKey.getType().name().equalsIgnoreCase("ConceptDescription"));
		assertEquals("IRI", semanticKey.getIdType().name());
		assertEquals("https://www.hsu-hh.de/aut/aas/physicaladdress", semanticKey.getValue());
		assertEquals(true, semanticKey.isLocal());

		// get values
		assertTrue(sele.getModelType().equalsIgnoreCase("SubmodelElementCollection"));
		SubmodelElementCollection collection = (SubmodelElementCollection) sele;
		Map<String, ISubmodelElement> smElemMap = collection.getSubmodelElements();

		assertEquals(5, smElemMap.size());
		Property prop1 = (Property) smElemMap.get("CountryCode");
		assertEquals("CountryCode", prop1.getIdShort());
		assertEquals("DE", prop1.get());

		Property prop2 = (Property) smElemMap.get("Street");
		assertEquals("Street", prop2.getIdShort());
		assertEquals("Ruiter Straße 82", prop2.get());
	}
	
	
	/**
	 * Delete created files
	 */
	@AfterClass
	public static void cleanUp() {
		for(String path: unzipFiles) {
			new java.io.File(path).delete();
		}
		new java.io.File(CREATED_AASX_PATH).delete();
	}

}
