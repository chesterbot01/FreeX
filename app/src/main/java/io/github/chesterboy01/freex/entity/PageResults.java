package io.github.chesterboy01.freex.entity;

import java.util.List;

/**
 * 分页封装类, 不参与hibernate的数据持久化 用于做分页查询的基础类，封装了一些分页的相关属性
 * 			首次分页查询时必须指定 pageNo 和 pageSize
 * 
 * @author Freddy Lee
 * @version v1.0
 * @param <T>
 */
public class PageResults<T> {

	// 下一页
	private int pageNo;

	// 当前页
	private int currentPage;

	// 每页个数
	private int pageSize;

	// 总条数
	private int totalCount;

	// 总页数
	private int pageCount;

	// 记录
	private List<T> results;

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * 获取请求页的页码
	 * @return
	 */
	public int getPageNo() {
		if(pageCount == 0){
			//第一次查询，没有pageCount   or  db没有任何数据
			if(pageNo <= 0) {
				return 1;
			} 
			return pageNo;
		}
		else {
			if(pageNo <= 0) {
				return 1;
			} 
			return pageNo > pageCount ? pageCount : pageNo;
		}
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 10 : pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void resetPageNo() {
		pageNo = currentPage + 1;
		pageCount = totalCount % pageSize == 0 ? totalCount / pageSize
				: totalCount / pageSize + 1;
	}

}
