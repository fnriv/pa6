import static org.junit.Assert.*;

import org.junit.*;

public class FileSystemTest {

    @Test
    public void t1fileSystemInput(){
        FileSystem tFileSystem = new FileSystem("/Users/frivera3/Documents/UCSD/CS/CSE 12/cse12-pa6-HashMap-master/pa6-starter/src/input.txt");
        System.out.println(tFileSystem.findAllFilesName());
        System.out.println(tFileSystem.findFilesByName("mySample.txt").size());
        System.out.println(tFileSystem.findFilesByDate("02/01/2021"));
    }
}
