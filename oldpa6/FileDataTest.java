import static org.junit.Assert.*;

import org.junit.*;

public class FileDataTest {
	
	@Test
	public void fdt1toString() {
		FileData testData = new FileData("file.txt", "/home", "05/04/2022");
		assertEquals("{Name: file.txt, Directory: /home, Modified Date: 05/04/2022}", testData.toString());
	}
	
	@Test
	public void fdt2toStringNull() {
		FileData testData = new FileData(null, null, null);
		assertEquals("{Name: , Directory: /, Modified Date: 01/01/2021}", testData.toString());
	}
}
