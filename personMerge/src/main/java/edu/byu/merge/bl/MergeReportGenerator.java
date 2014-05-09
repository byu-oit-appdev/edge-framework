package edu.byu.merge.bl;

import com.google.common.base.Function;
import edu.byu.pro.merge.domain.PersonMerge;

import java.util.Map;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/09/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/09/2014
 */
public interface MergeReportGenerator extends Function<Map<PersonMerge, Map<String, Integer>>, String> {
}
