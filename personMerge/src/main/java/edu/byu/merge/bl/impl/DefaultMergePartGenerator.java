package edu.byu.merge.bl.impl;

import edu.byu.merge.bl.MergePartGenerator;

import java.util.Map;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/09/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/09/2014
 */
public class DefaultMergePartGenerator implements MergePartGenerator {

	@Override
	public String apply(final Map<String, Integer> input) {
		final StringBuilder sb = new StringBuilder();
		for (final Map.Entry<String, Integer> entry : input.entrySet()) {
			sb.append("<span");
			if (entry.getValue() > 0) sb.append(" class='positive'");
			if (entry.getValue() == -1) sb.append(" class='error'");
			sb.append(">");
			sb.append(entry.getKey());
			sb.append(" : ");
			sb.append(entry.getValue());
			sb.append("</span><br/>\n");
		}
		return sb.toString();
	}
}
