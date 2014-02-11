package edu.byu.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Generic utilities to help with certain query scenarios.
 *
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 02/10/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 02/10/2014
 */
public final class QueryUtils {

	/**
	 * The default maximum number of items in a parameter list.
	 */
	public static final int DEFAULT_MAX_ITEMS_IN_PARAMETER_LIST = 1000;

	/**
	 * This method executes the query with a maximum number of elements in the parameter list. It uses the default value of {@link #DEFAULT_MAX_ITEMS_IN_PARAMETER_LIST}.
	 *
	 * This method delegates to {@link #doQueryWithParameterList(java.util.List, SelectableQueryExecutor, int) the more verbose method}.
	 * Calling this method is the same as calling {@code doQueryWithParameterList(parameterList, executor, edu.byu.util.QueryUtils.DEFAULT_MAX_ITEMS_IN_PARAMETER_LIST);}.
	 *
	 * This is useful when a database either does not support more than a certain number of items in a parameter list {@code ... COLUMN in (item1, item2, ...)},
	 * or a query is more optimized when such a list is under a certain number of items.
	 *
	 * @param parameterList The full list of parameters.
	 * @param executor The actual execution of the query.
	 * @param <P> The type of each element in the parameter list.
	 * @param <R> The type of each element in the query result.
	 * @return The result of the query.
	 */
	public static <P, R> List<R> doQueryWithParameterList(final List<P> parameterList, final SelectableQueryExecutor<P, R> executor) {
		return doQueryWithParameterList(parameterList, executor, DEFAULT_MAX_ITEMS_IN_PARAMETER_LIST);
	}

	/**
	 * This method executes the query with a maximum number of elements in the parameter list.
	 *
	 * This is useful when a database either does not support more than a certain number of items in a parameter list {@code ... COLUMN in (item1, item2, ...)},
	 * or a query is more optimized when such a list is under a certain number of items.
	 *
	 * @param parameterList The full list of parameters.
	 * @param executor The actual execution of the query.
	 * @param maxItemsInList The maximum number of items in the query list.
	 * @param <P> The type of each element in the parameter list.
	 * @param <R> The type of each element in the query result.
	 * @return The result of the query.
	 */
	public static <P, R> List<R> doQueryWithParameterList(final List<P> parameterList, final SelectableQueryExecutor<P, R> executor, final int maxItemsInList) {
		if (executor == null) throw new IllegalArgumentException("A valid edu.byu.util.SelectableQueryExecutor must be provided.");
		final int[] bounds = determineParamBounds(parameterList != null ? parameterList.size() : 0, maxItemsInList);
		final List<R> list = new LinkedList<R>();
		for (int i = 0; i < bounds.length - 1; i++) {
			final List<R> temp = executor.doQuery(parameterList != null ? parameterList.subList(bounds[i], bounds[i + 1]) : new ArrayList<P>(0));
			if (temp != null) list.addAll(temp);
		}
		return list;
	}

	/**
	 * This method executes the query with a maximum number of elements in the parameter list. It uses the default value of {@link #DEFAULT_MAX_ITEMS_IN_PARAMETER_LIST}.
	 *
	 * This method delegates to {@link #doUpdateQueryWithParameterList(java.util.List, UpdatableQueryExecutor, int) the more verbose method}.
	 * Calling this method is the same as calling {@code doUpdateQueryWithParameterList(parameterList, executor, edu.byu.util.QueryUtils.DEFAULT_MAX_ITEMS_IN_PARAMETER_LIST);}.
	 *
	 * This is useful when a database either does not support more than a certain number of items in a parameter list {@code ... COLUMN in (item1, item2, ...)},
	 * or a query is more optimized when such a list is under a certain number of items.
	 *
	 * @param parameterList The full list of parameters.
	 * @param executor The actual execution of the query.
	 * @param <P> The type of each element in the parameter list.
	 * @return The number of rows or records modified.
	 */
	public static <P> int doUpdateQueryWithParameterList(final List<P> parameterList, final UpdatableQueryExecutor<P> executor) {
		return doUpdateQueryWithParameterList(parameterList, executor, DEFAULT_MAX_ITEMS_IN_PARAMETER_LIST);
	}

	/**
	 * This method executes the query with a maximum number of elements in the parameter list. It uses the default value of {@link #DEFAULT_MAX_ITEMS_IN_PARAMETER_LIST}.
	 *
	 * This is useful when a database either does not support more than a certain number of items in a parameter list {@code ... COLUMN in (item1, item2, ...)},
	 * or a query is more optimized when such a list is under a certain number of items.
	 *
	 * @param parameterList The full list of parameters.
	 * @param executor The actual execution of the query.
	 * @param maxItemsInList The maximum number of items in the query list.
	 * @param <P> The type of each element in the parameter list.
	 * @return The number of rows or records modified.
	 */
	public static <P> int doUpdateQueryWithParameterList(final List<P> parameterList, final UpdatableQueryExecutor<P> executor, final int maxItemsInList) {
		if (executor == null) throw new IllegalArgumentException("A valid edu.byu.util.UpdatableQueryExecutor must be provided.");
		final int[] bounds = determineParamBounds(parameterList != null ? parameterList.size() : 0, maxItemsInList);
		int mods = 0;
		for (int i = 0; i < bounds.length - 1; i++) {
			mods += executor.doQuery(parameterList != null ? parameterList.subList(bounds[i], bounds[i + 1]) : new ArrayList<P>(0));
		}
		return mods;
	}

	/**
	 * Calculates the boundaries of the query sublists. Negative list sizes are considered invalid and produce a result of {@code &#124;0, 0&#125;}.
	 *
	 * @param size The size of the parameter list.
	 * @param maxPerCall The maximum number of items allowed in a parameter list. Must be at least 1. A java.lang.IllegalArgumentException will be thrown when this rule is violated.
	 * @return An array containing the boundaries of the sub lists.
	 */
	static int[] determineParamBounds(final int size, final int maxPerCall) {
		if (size <= 0) return new int[]{0, 0};
		if (maxPerCall < 1) throw new IllegalArgumentException("Must have at least one element per call.");
		final int numExtra = size % maxPerCall;
		final int numCalls = size / maxPerCall + (numExtra > 0 ? 1 : 0);
		final int[] result = new int[numCalls + 1];
		final int baseNumPerCall = size / numCalls;
		final int numPerCall = baseNumPerCall + ((size - (numCalls - 1) * baseNumPerCall <= maxPerCall) ? 0 : 1);

		for (int i = 0; i < numCalls; i++) {
			result[i] = numPerCall * i;
		}
		result[numCalls] = size;

		return result;
	}

	private QueryUtils() {
	}

}
