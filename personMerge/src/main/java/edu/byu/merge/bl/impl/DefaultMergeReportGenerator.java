package edu.byu.merge.bl.impl;

import edu.byu.merge.bl.MergePartGenerator;
import edu.byu.merge.bl.MergeReportGenerator;
import edu.byu.pro.merge.domain.PersonMerge;

import java.util.Map;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/09/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/09/2014
 */
public class DefaultMergeReportGenerator implements MergeReportGenerator {

	private final MergePartGenerator mergePartGenerator;

	public DefaultMergeReportGenerator(final MergePartGenerator mergePartGenerator) {
		this.mergePartGenerator = mergePartGenerator;
	}

	@Override
	public String apply(final Map<PersonMerge, Map<String, Integer>> list) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<html><head><style type='text/css'>\n");
		sb.append("span.error {text-color: red; font-weight: bold;}\n");
		sb.append("span.positive {font-weight: bold;}");
		sb.append("table, table>thead, table>thead>tr, table>thead>tr>th, table>tbody, table>tbody>tr, table>tbody>tr>td\n{border: 1px solid black; border-collapse: collapse;}");
		sb.append("</style></head>");
		sb.append("<body><table><thead><tr><th>Merge</th><th>Results</th></tr></thead><tbody>\n");
		for (final Map.Entry<PersonMerge, Map<String, Integer>> map : list.entrySet()) {
			sb.append("<tr><td>");
			sb.append(map.getKey().getOldPersonId());
			sb.append(" -> ");
			sb.append(map.getKey().getNewPersonId());
			sb.append("</td><td>");
			sb.append(mergePartGenerator.apply(map.getValue()));
			sb.append("</td></tr>\n");
		}
		sb.append("</tbody></table></body></html>\n");
		return sb.toString();
	}
}
