package com.agilecontent.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.agilecontent.test.models.PagedList;

/**
 * Pagination tests. Checks the functionality of our PagedList custom class.
 * 
 * @author Carlos Melero
 *
 */
class PaginationTests {

	/**
	 * Mock list of Integer to be used in our tests. Using Integer instead of User for simplicity;
	 * functionality should apply since PagedList contains a generic type.
	 */
	PagedList<Integer> mockPagedList;

	/**
	 * Pre-initialize the mock list with 8 Integers and page size 3.
	 */
	@BeforeEach
	void initService() {
		mockPagedList = new PagedList<Integer>(3);
		mockPagedList.add(0);
		mockPagedList.add(1);
		mockPagedList.add(2);
		mockPagedList.add(3);
		mockPagedList.add(4);
		mockPagedList.add(5);
		mockPagedList.add(6);
		mockPagedList.add(7);
	}

	/**
	 * Gets all the elements in an only ArrayList.
	 */
	@Test
	void getAllElements() {
		assertThat(mockPagedList.getAllElements()).hasSize(8);
	}

	/**
	 * Gets all the elements in an only ArrayList.
	 */
	@Test
	void getThingsForEmptyList() {
		PagedList<Integer> mockEmptyPagedList =  new PagedList<Integer>(3);
		assertThat(mockEmptyPagedList.getAllElements()).isEmpty();
		assertThat(mockEmptyPagedList.getFirstPage()).isEmpty();
		assertThat(mockEmptyPagedList.getLastPage()).isEmpty();
		assertThat(mockEmptyPagedList.getPage()).isEmpty();
	}

	/**
	 * Gets an element specifying the page index and position index within the page.
	 */
	@Test
	void getOneElementByPageAndPosition() {
		assertThat(mockPagedList.getElement(1, 2)).isEqualTo(5);
	}

	/**
	 * Same as getOneElementByPageAndPosition(), but with a wrong position index.
	 */
	@Test
	void getOneElementByPageAndWrongPosition() {
		assertThat(mockPagedList.getElement(1, 5)).isNull();
	}

	/**
	 * Same as getOneElementByPageAndPosition(), but with a wrong page index.
	 */
	@Test
	void getOneElementByWrongPageAndPosition() {
		assertThat(mockPagedList.getElement(5, 1)).isNull();
	}

	/**
	 * Checks basic pagination functionality.
	 */
	@Test
	void paginate() {
		assertThat(mockPagedList.getPages()).isEqualTo(3);
		assertThat(mockPagedList.getPage()).hasSize(3);
		assertThat(mockPagedList.getLastPage()).hasSize(2);
	}

	/**
	 * Adds one Integer to our list and checks the result.
	 */
	@Test
	void addOne() {		
		mockPagedList.add(8);
		assertThat(mockPagedList.size()).isEqualTo(9);
		assertThat(mockPagedList.getAllElements().get(mockPagedList.size()-1)).isEqualTo(8);
	}

	/**
	 * Adds a list of Integers to our list and checks the result.
	 */
	@Test
	void addMany() {		
		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(8);
		numbers.add(9);
		numbers.add(10);
		numbers.add(11);
		numbers.add(12);
		mockPagedList.addAll(numbers);
		assertThat(mockPagedList.size()).isEqualTo(13);
	}

	/**
	 * Remove one Integer and check it's not contained anymore.
	 */
	@Test
	void removeOne() {		
		mockPagedList.remove(4);
		assertThat(mockPagedList.contains(4)).isFalse();
	}

	/**
	 * Checks the in-page search works correctly.
	 */
	@Test
	void getPageOfNumber() {
		assertThat(mockPagedList.getPageOf(4)).contains(4);
		assertThat(mockPagedList.getPageOf(65)).isNull();
	}

	/**
	 * Tries to clear the contents of our PagedList and checks it's empty.
	 */
	@Test
	void clearContents() {
		mockPagedList.clear();
		assertThat(mockPagedList.isEmpty()).isTrue();
	}

	/**
	 * Performs several page movement operations and verifies the results are the expected ones.
	 */
	@Test
	void movePagesAround() {
		assertThat(mockPagedList.getPage()).contains(0,1,2);
		assertThat(mockPagedList.getPage()).contains(3,4,5);
		mockPagedList.nextPage();
		assertThat(mockPagedList.getPage()).isNull();
		mockPagedList.prevPage();
		assertThat(mockPagedList.getPage()).contains(6,7);
		mockPagedList.setPage(1);
		assertThat(mockPagedList.getPage()).contains(3,4,5);
		assertThat(mockPagedList.getFirstPage()).contains(0,1,2);
		assertThat(mockPagedList.getLastPage()).contains(6,7);
	}

	/**
	 * Sorts the contents in inverse order and checks the result.
	 */
	@Test
	void sortInverse() {
		mockPagedList.sort(Collections.reverseOrder());
		assertThat(mockPagedList.getPage()).contains(7,6,5);
		assertThat(mockPagedList.getPage()).contains(4,3,2);
		assertThat(mockPagedList.getPage()).contains(1,0);
	}

	/**
	 * Generates a stream and checks the elements are not null.
	 */
	@Test
	void checkStream() {
		mockPagedList.stream().forEach(e -> {
			assertThat(e).isNotNull();
		});;
	}

	/**
	 * Generates a parallel stream and checks the elements are not null.
	 */
	@Test
	void checkParallelStream() {
		mockPagedList.parallelStream().forEach(e -> {
			assertThat(e).isNotNull();
		});;
	}

}
