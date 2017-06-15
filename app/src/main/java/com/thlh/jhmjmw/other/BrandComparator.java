package com.thlh.jhmjmw.other;


import com.thlh.baselib.model.Brand;

import java.util.Comparator;

/**
 * 
 *品牌排序
 *
 */
public class BrandComparator implements Comparator<Brand> {

	public int compare(Brand o1, Brand o2) {
		return o1.getSortLetters().compareTo(o2.getSortLetters());
	}

}
