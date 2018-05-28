package com.hxy.system;

import java.util.ArrayList;
import java.util.List;

import com.hxy.base.entity.TreeString;

public class TreeStringUtil {
	public static void parseTreeString(List<TreeString> rootTreeStrings, List<TreeString> allList) {
		for (TreeString TreeString : rootTreeStrings) {
			getChildren(TreeString, allList);
		}
	}
	
	private static void getChildren(TreeString TreeString, List<TreeString> allList) {
		List<TreeString> children = getChild(TreeString.getId(), allList);
		if (children != null && children.size() > 0) {
			TreeString.setChildren(children);
			for (TreeString TreeString2 : children) {
				getChildren(TreeString2, allList);
			}
		} else {
			TreeString.setState("open");
		}
	}

	/**
	 * 删除使用,获取删除节点的所有子节点
	 * 
	 * @param id
	 * @param allList
	 * @param deptIdList
	 */
	public static void getChildren(String id, List<TreeString> allList, List<String> deptIdList) {
		List<TreeString> children = getChild(id, allList);
		if (children != null && children.size() > 0) {
			for (TreeString TreeString2 : children) {
				deptIdList.add(TreeString2.getId());
				getChildren(TreeString2.getId(), allList, deptIdList);
			}
		}
	}

	private static List<TreeString> getChild(String id, List<TreeString> allList) {
		List<TreeString> children = new ArrayList<TreeString>();
		if (allList != null && allList.size() > 0) {
			for (TreeString TreeString : allList) {
				if (TreeString.getPid().equals(id)) {
					children.add(TreeString);
				}
			}
		}
		return children;
	}
}
