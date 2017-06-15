package com.thlh.jhmjmw.other;


import com.thlh.baselib.model.FilterSupplier;

import java.util.Comparator;

/**
 * 
 *品牌排序
 *
 */
public class SupplierComparator implements Comparator<FilterSupplier> {

	public int compare(FilterSupplier o1, FilterSupplier o2) {
		return o1.getSortLetters().compareTo(o2.getSortLetters());
	}

}
