package photoRenamer;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class TagsTest {
	
	@Test
	//Add a new tag that is not already in the list
	public void testAddNewTag() throws IOException, ClassNotFoundException {
		System.out.println("Running: testAddNewTag");
		String[] x = {"test"};
		Tags test = Tags.getInstance();
		if (!test.getAllTagsUsed().contains("@test")) {
			System.out.println("@test is not found in list.\n");
		}
		ArrayList<String> expected = test.getAllTagsUsed();
		expected.add("@test");
		test.updateTags(x);
		ArrayList<String> result = test.getAllTagsUsed();
		assertEquals(expected, result);
	}
	
	@Test
	//Add a tag that is already in the list
	public void testAddOldTag() throws IOException, ClassNotFoundException {
		System.out.println("Running: testAddOldTag");
		String[] x = {"test"};
		Tags test = Tags.getInstance();
		if (test.getAllTagsUsed().contains("@test")) {
			System.out.println("@test is found in list.\n");
		}
		ArrayList<String> expected = test.getAllTagsUsed();
		test.updateTags(x);
		ArrayList<String> result = test.getAllTagsUsed();
 		assertEquals(expected, result);
	}
	
	@Test
	//Add multiple tags
	public void testAddMultiTags() throws ClassNotFoundException, IOException {
		System.out.println("Running: testAddMultiTag\n");
		String[] x = {"test2", "test3"};
		Tags test = Tags.getInstance();
		ArrayList<String> expected = test.getAllTagsUsed();
		expected.add("@test2");
		expected.add("@test3");
		test.updateTags(x);
		ArrayList<String> result = test.getAllTagsUsed();
		assertEquals(expected, result);
	}
	
	@Test
	//Delete a tag that is in the list
	public void testDeleteExistingTag() throws ClassNotFoundException, IOException {
		System.out.println("Running: testdeleteExistTag");
		String[] x = {"test"};
		Tags test = Tags.getInstance();
		if (test.getAllTagsUsed().contains("@test")) {
			System.out.println("@test is found in list.\n");
		}
		ArrayList<String> expected = test.getAllTagsUsed();
		expected.remove("@test");
		test.deleteTags(x);
		ArrayList<String> result = test.getAllTagsUsed();
		assertEquals(expected, result);
	}
	
	@Test
	//Delete a tag that is not in the list
	public void testDeleteMissingTag() throws ClassNotFoundException, IOException {
		System.out.println("Running: testDeleteMissingTag");
		String[] x = {"CSC207"};
		Tags test = Tags.getInstance();
		if(!test.getAllTagsUsed().contains("@CSC207")) {
			System.out.println("@CSC207 is not found in list.\n");
		}
		ArrayList<String> expected = test.getAllTagsUsed();
		test.deleteTags(x);
		ArrayList<String> result = test.getAllTagsUsed();
		assertEquals(expected, result);
	}
	
	@Test
	//Delete multiple tags
	public void testDeleteMultiTags() throws ClassNotFoundException, IOException {
		System.out.println("Running: testDeleteMultiTags\n");
		String[] x = {"test2", "test3"};
		Tags test = Tags.getInstance();
		ArrayList<String> expected = test.getAllTagsUsed();
		expected.remove("@test2");
		expected.remove("@test3");
		test.deleteTags(x);
		ArrayList<String> result = test.getAllTagsUsed();
		assertEquals(expected, result);
	}
}
