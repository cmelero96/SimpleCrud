package com.agilecontent.test.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * A custom class that contains a List of elements and paginates them. This is
 * a custom implementation based on some own ideas and examples found online.
 * 
 * @author Carlos Melero
 *
 * @param <E> Generic type. This class was designed to work for User objects,
 * but no need to restrict the class usage to only User types, thus generic.
 */
public class PagedList<E> {

	// The contents of the PagedList as they are.
	private List<E> elements;
	
	// Size of the pages to be generated. Once set, cannot be changed.
	private final int pageSize;

	// The current page we're currently operating in.
	private int currentPage;
	
	// Total number of pages.
	private int pages;

	/**
	 * Constructor with a specific page size and an empty list of elements.
	 * @param pageSize The size of each page
	 */
	public PagedList(int pageSize) {
		this(new ArrayList<E>(), pageSize);
	}

	/**
	 * Constructor with a specific page size and an initial Collection of elements
	 * @param elements The list of initial elements
	 * @param pageSize The size of each page
	 */
	public PagedList(Collection<E> elements, int pageSize) {
		this.elements = new ArrayList<E>(elements);
		this.pageSize = pageSize;
		this.pages = (elements.size()-1) / pageSize;
	}

	/**
	 * Gets the amount of pages of this PagedList
	 * @return The last page index + 1
	 */
	public int getPages() {
		return pages + 1;
	}

	/**
	 * Gets the total size of the elements of the PagedList
	 * @return The amount of elements in total
	 */
	public int size() {
		return elements.size();
	}

	/**
	 * Returns the whole list of elements, without pagination whatsoever.
	 * @return The whole list of elements
	 */
	public List<E> getAllElements() {
		return elements;
	}

	/**
	 * Returns the element found in the specified index for the specified
	 * page index.
	 * @param pageIndex The index of the page to get the element from
	 * @param index The position index within the page
	 * @return The element found in such a position for such a page, or null
	 * if out of bounds
	 */
	public E getElement(int pageIndex, int index) {
		List<E> page = getPage(pageIndex);

		if (page != null && index <= page.size()-1 && index >= 0) {
			return page.get(index);
		} else return null;
	}

	/**
	 * Returns a whole page.
	 * @param page The index of the page to return.
	 * @return The page found as an ArrayList, or null if out of bounds
	 */
	public List<E> getPage(int page) {

		if (page <= pages && page >= 0) {
			currentPage = page;
			return elements.subList(page * pageSize, Math.min(elements.size(), pageSize + page*pageSize));	
		} else return null;
	}

	/**
	 * Gets the current page we're in, and moves the position to the next page.
	 * This means, if we are currently on page N, getPage() will return the contents
	 * of page N as an arrayList, and then move the position to N+1 if not out of bounds.
	 * @return The page found, or null if out of bounds
	 */
	public List<E> getPage() {
		List<E> res = getPage(currentPage);
		if (res != null) {
			currentPage++;
		}
		return res;
	}

	/**
	 * Changes the working page position to the one specified.
	 * @param pageNumber The index of the page to which to move.
	 */
	public void setPage(int pageNumber) {
		if (pageNumber < 0) {
			currentPage = -1;
		} else if (pageNumber > pages) {
			currentPage = pages+1;
		} else {
			currentPage = pageNumber;
		}
	}

	/**
	 * Returns the elements of the first page as an ArrayList, and moves the
	 * position to such a page.
	 * @return The first page contents as an ArrayList
	 */
	public List<E> getFirstPage() {
		currentPage = 0;
		return getPage(currentPage);
	}

	/**
	 * Returns the elements of the last page as an ArrayList, and moves the
	 * position to such a page.
	 * @return The last page contents as an ArrayList
	 */
	public List<E> getLastPage() {
		currentPage = elements.size() / pageSize;
		return getPage(currentPage);
	}

	/**
	 * Moves the position to the next page, and then returns the contents
	 * within that page. If out of bounds, it resets the position to the
	 * last page + 1 (so that we return null if we try to get anything there)
	 * @return The next page contents, or null if out of bounds
	 */
	public List<E> nextPage() {
		if (++currentPage <= pages) {
			return getPage(currentPage);
		} else {
			currentPage = pages+1;
			return null;
		}
	}

	/**
	 * Moves the position to the previous page, and then returns the contents
	 * within that page. If out of bounds, it resets the position to -1
	 * (so that we return null if we try to get anything there)
	 * @return The previous page contents, or null if out of bounds
	 */
	public List<E> prevPage() {
		if (--currentPage >= 0) {
			return getPage(currentPage);
		} else {
			currentPage = -1;
			return null;
		}
	}

	/**
	 * Searches for an element within the contents and returns the page
	 * that contains that element, or null if not found.
	 * @param e The element to search for
	 * @return The page, as an ArrayList, that contains the element, or null
	 * if no such element was found
	 */
	public List<E> getPageOf(E e) {
		int idx = elements.indexOf(e);

		if (idx != -1) {
			return getPage(idx/pageSize);
		} else return null;
	}

	/**
	 * Searches for an element within the contents and returns the page
	 * that contains that element, or null if not found.
	 * @param e The element to search for
	 * @return The page, as an ArrayList, that contains the element, or null
	 * if no such element was found
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * Returns the stream of the whole contents of the PagedList.
	 * @return The stream of the contents
	 */
	public Stream<E> stream() {
		return elements.stream();
	}

	/**
	 * Returns the parallelStream of the whole contents of the PagedList.
	 * @return The parallelStream of the contents
	 */
	public Stream<E> parallelStream() {
		return elements.parallelStream();
	}

	/**
	 * Checks if the element is contained within the PagedList.
	 * @param e The element to search for
	 * @return true if found, false if not
	 */
	public boolean contains (E e) {
		int i = elements.indexOf(e);

		return (i != -1);
	}

	/**
	 * Adds an element to the end of the PagedList.
	 * @param e The element to add
	 * @return true if added, false if not
	 */
	public boolean add(E e) {
		boolean res = elements.add(e);
		repaginate();
		return res;
	}

	/**
	 * Adds an element to the specified global position on the list.
	 * Shifts to the right every element after the new one.
	 * @param e The element to add
	 */
	public void add(int index, E e) {
		elements.add(index, e);
		repaginate();
	}

	/**
	 * Adds an element to the specified page, in the specified position.
	 * Shifts to the right every element after the new one.
	 * @param pageIndex The page to which to insert the element
	 * @param positionIndex The position within the page
	 * @param e The element to insert
	 */
	public void add(int pageIndex, int positionIndex, E e) {
		elements.add(pageIndex*pageSize + positionIndex, e);
		repaginate();
	}

	/**
	 * Adds a collection of elements to the end of the PagedList.
	 * @param c The collection to add
	 * @return true if added, false if not
	 */
	public boolean addAll(Collection<E> c) {
		boolean res = elements.addAll(c);
		repaginate();
		return res;
	}

	/**
	 * Removes the selected element from the PagedList.
	 * @param e The element to remove
	 * @return true if removed, false if not (not found)
	 */
	public boolean remove(E e) {
		boolean res = elements.remove(e);
		repaginate();
		return res;
	}

	/**
	 * Empties the PagedList.
	 */
	public void clear() {
		elements.clear();
		repaginate();
	}

	/**
	 * Checks if the contents of our PagedList are empty.
	 * @return true if empty, false if not
	 */
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	/**
	 * Sorts the contents of the elements of the PagedList.
	 * @param c The comparator to sort the contents by
	 */
	public void sort(Comparator<? super E> c) {
		elements.sort(c);
		repaginate();
	}

	/**
	 * Recalculates the number of pages of the PagedList and resets
	 * the page position to 0. Performed after contents modification
	 * such as additions, substractions or sorting.
	 */
	private void repaginate() {
		pages = (elements.size()!=0) ? ((elements.size()-1) / pageSize) : 0;
		currentPage = 0;
	}

}
