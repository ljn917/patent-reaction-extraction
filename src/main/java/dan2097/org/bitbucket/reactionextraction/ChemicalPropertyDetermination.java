package dan2097.org.bitbucket.reactionextraction;

import java.util.List;

import org.apache.log4j.Logger;

import uk.ac.cam.ch.wwmm.opsin.XOMTools;

import dan2097.org.bitbucket.utility.ChemicalTaggerTags;

import nu.xom.Element;
import nu.xom.Elements;

/**
 * Determine a chemicals properties e.g. quantity, phase etc. from local semantic information
 * @author dl387
 *
 */
public class ChemicalPropertyDetermination {

	static Logger LOG = Logger.getLogger(ChemicalPropertyDetermination.class);
	
	static void determineProperties(Chemical chemical, Element molecule){
	    List<Element> quantityElements = XOMTools.getDescendantElementsWithTagName(molecule, ChemicalTaggerTags.QUANTITY_Container);
		for (int i = 0; i < quantityElements.size(); i++) {
		    Element quantityElement = quantityElements.get(i);
			determineMass(chemical, quantityElement);
			determineAmount(chemical, quantityElement);
			determineMolarity(chemical, quantityElement);
			determineQuantity(chemical, quantityElement);
			determineYield(chemical, quantityElement);
		}
	}

	private static void determineQuantity(Chemical chemical, Element quantityElement) {
		Elements volumes = quantityElement.getChildElements(ChemicalTaggerTags.VOLUME_Container);
		if (volumes.size()>1){
			LOG.debug("More than 1 volume given for same chemical");
		}
		else if (volumes.size()>0){
			if (chemical.getVolumeValue()!=null){
				LOG.debug("More than 1 volume given for same chemical");
			}
			else{
				Element volume = volumes.get(0);
				chemical.setVolumeValue(volume.getFirstChildElement(ChemicalTaggerTags.CD).getValue());
				chemical.setVolumeUnits(volume.getFirstChildElement(ChemicalTaggerTags.NN_VOL).getValue());
			}
		}
	}

	private static void determineAmount(Chemical chemical, Element quantityElement) {
		Elements amounts = quantityElement.getChildElements(ChemicalTaggerTags.AMOUNT_Container);
		if (amounts.size()>1){
			LOG.debug("More than 1 amount given for same chemical");
		}
		else if (amounts.size()>0){
			if (chemical.getAmountValue()!=null){
				LOG.debug("More than 1 amount given for same chemical");
			}
			else{
				Element amount = amounts.get(0);
				chemical.setAmountValue(amount.getFirstChildElement(ChemicalTaggerTags.CD).getValue());
				chemical.setAmountUnits(amount.getFirstChildElement(ChemicalTaggerTags.NN_AMOUNT).getValue());
			}
		}
	}
	
	private static void determineMolarity(Chemical chemical, Element quantityElement) {
		Elements molarAmounts = quantityElement.getChildElements(ChemicalTaggerTags.MOLAR_Container);
		if (molarAmounts.size()>1){
			LOG.debug("More than 1 molarity given for same chemical");
		}
		else if (molarAmounts.size()>0){
			if (chemical.getAmountValue()!=null){
				LOG.debug("More than 1 molarity given for same chemical");
			}
			else{
				Element molarity = molarAmounts.get(0);
				chemical.setMolarityValue(molarity.getFirstChildElement(ChemicalTaggerTags.CD).getValue());
			}
		}
	}

	private static void determineMass(Chemical chemical, Element quantityElement) {
		Elements masses = quantityElement.getChildElements(ChemicalTaggerTags.MASS_Container);
		if (masses.size()>1){
			LOG.debug("More than 1 mass given for same chemical");
		}
		else if (masses.size()>0){
			if (chemical.getMassValue()!=null){
				LOG.debug("More than 1 mass given for same chemical");
			}
			else{
				Element mass = masses.get(0);
				chemical.setMassValue(mass.getFirstChildElement(ChemicalTaggerTags.CD).getValue());
				chemical.setMassUnits(mass.getFirstChildElement(ChemicalTaggerTags.NN_MASS).getValue());
			}
		}
	}
	
	private static void determineYield(Chemical chemical, Element quantityElement) {
		Elements yields = quantityElement.getChildElements(ChemicalTaggerTags.YIELD_Container);
		if (yields.size()>1){
			LOG.debug("More than 1 yield given for same chemical");
		}
		else if (yields.size()>0){
			if (chemical.getPercentYield()!=null){
				LOG.debug("More than 1 yield given for same chemical");
			}
			else{
				Element yield = yields.get(0);
				String value = yield.query(".//" + ChemicalTaggerTags.CD).get(0).getValue();
				try{ 
					double d = Double.parseDouble(value);
					chemical.setPercentYield(d);
				}
				catch (NumberFormatException e) {
					LOG.debug("Yield was not a numeric percentage");
				}
			}
		}
	}
}
