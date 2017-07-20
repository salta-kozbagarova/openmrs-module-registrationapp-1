package org.openmrs.module.registrationapp.fragment.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.ConceptService;
import org.openmrs.module.registrationapp.RegistrationAppUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * Fragment Controller that is used for coded person attributes with a foreign key to the coded concept.
 * Retrieves concept answers that match to the search phrase in a given quantity
 * 
 * @param searchPhrase a phrase concept answers has to match
 * @param maxResults quantity of concept answers
 * @param conceptId id of a concept that person attribute is related to
 * @return Collection
 */
public class PersonAttributeWithConceptFragmentController {

	public Collection<Map<String, ? extends Object>> getConcepts(
			@SpringBean("conceptService") ConceptService conceptService,
			@RequestParam(value = "term", required = true) String searchPhrase,
			@RequestParam(value = "maxResults", required = true) String maxResults,
			@RequestParam(value = "conceptId", required = true) String conceptId){
		
		Concept concept = RegistrationAppUtils.getConcept(conceptId, conceptService);
		Collection<Map<String, ? extends Object>> collection = new ArrayList<Map<String,? extends Object>>();
		Map<String, Object> conceptMap;
		Locale locale = new Locale("ru");
		Collection<ConceptAnswer> conceptAnswers = concept.getAnswers();
		
		int resultCount=0;
		for (ConceptAnswer conceptAnswer : conceptAnswers) {
			if(resultCount==Integer.parseInt(maxResults)){
				break;
			}
			conceptMap = new HashMap<String, Object>();
			String fullname = conceptAnswer.getAnswerConcept().getFullySpecifiedName(locale).getName();
			if(fullname.compareToIgnoreCase(searchPhrase)<0){
				continue;
			}
			conceptMap.put("label", fullname);
			conceptMap.put("value", fullname);
			collection.add(conceptMap);
			resultCount++;
		}
		
		return collection;
	}
}