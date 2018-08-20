package com.aem.community.core.utils;

import com.day.cq.search.Predicate;
import com.day.cq.search.eval.AbstractPredicateEvaluator;
import com.day.cq.search.eval.EvaluationContext;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.query.Row;

/**
 * Custom case insensitive predicate for like operation
 * caseinsensitive.property=jcr:content/@jcr:title caseinsensitive.value=
 * queryString %
 *
 */
@Component(factory = "com.day.cq.search.eval.PredicateEvaluator/caseinsensitive")
public class CaseInsensitiveLikePredicate extends AbstractPredicateEvaluator {
	public static final String PROPERTY = "property";
	public static final String VALUE = "value";
	public static final String WILDCARD = "%";
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean includes(Predicate predicate, Row row, EvaluationContext context) {
		if (predicate.hasNonEmptyValue(PROPERTY)) {
			return true;
		}
		return super.includes(predicate, row, context);
	}

	@Override
	public String getXPathExpression(Predicate predicate, EvaluationContext context) {
		if (!predicate.hasNonEmptyValue(PROPERTY)) {
			return null;
		}
		if (predicate.hasNonEmptyValue(PROPERTY) && null == predicate.get(VALUE)) {
			return super.getXPathExpression(predicate, context);
		}
		if (predicate.hasNonEmptyValue(PROPERTY)) {
			predicate.get(VALUE);
			if (WILDCARD.equals(predicate.get(VALUE))) {
				logger.info("Case Insensitive Query only has wildcard, ignoring predicate");
				return "";
			}
			logger.info("jcr:like(fn:lower-case(" + predicate.get(PROPERTY) + "), '"
					+ predicate.get(VALUE).toLowerCase() + "')");
			return "jcr:like(fn:lower-case(" + predicate.get(PROPERTY) + "),'" + predicate.get(VALUE).toLowerCase()
					+ "')";
		}
		return null;
	}
}