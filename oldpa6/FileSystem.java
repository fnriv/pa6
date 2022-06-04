import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileSystem {

    MyHashMap<String, ArrayList<FileData>> nameMap;
    MyHashMap<String, ArrayList<FileData>> dateMap;

    // TODO
    /**
     * default FileSystem constructor
     */
    public FileSystem() {
    	this.nameMap = new MyHashMap<String, ArrayList<FileData>>();
    	this.dateMap = new MyHashMap<String, ArrayList<FileData>>();
    }
 
    // TODO
    /**
     * Constructs FileSystem by parsing from file; adds Filedata into Filesystem
     * @param inputFile
     */
    public FileSystem(String inputFile) {
        // Add your code here
    	this.nameMap = new MyHashMap<String, ArrayList<FileData>>();
    	this.dateMap = new MyHashMap<String, ArrayList<FileData>>();
        try {
            File f = new File(inputFile);
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(", ");
                System.out.println("0: |" + data[0] + "| 1: |" +  data[1] + "| 2: |" + data[2]);
                this.add(data[0], data[1], data[2]);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    // TODO
    /**
     * Create a FileData object with the given file information and 
     *      add it to the instance variables of FileSystem. 
     * The default values for filenName, dir, and modifiedDate are "", "/", "01/01/2021" if null.
     * @param fileName the String name of the FileData to be added
     * @param directory the String directory name of the FileData to be added
     * @param modifiedDate the String date of the FileData to be added
     * @return true if the file is successfully added to the FileSystem, 
     *      and false if a file with the same name already exists in that directory. 
     */
    public boolean add(String fileName, String directory, String modifiedDate) {
        if (fileName == null){
            fileName = "";
        }
        if (directory == null) {
			directory = "/";
		}
        if (modifiedDate == null) {
			modifiedDate = "01/01/2021";
		}
        FileData file = new FileData(fileName, directory, modifiedDate);
        String oldDate = null;
        FileData sameFileDiffDateFile = null;
        if (nameMap.get(fileName) == null){
            ArrayList<FileData> list = new ArrayList<FileData>();
            list.add(file);
            nameMap.set(fileName, list);
        } else {
            ArrayList<FileData> nameFileList = nameMap.get(fileName);
            boolean sameFileDiffDate = false;
            for (FileData currFile : nameFileList){
                if (currFile.equals(file)){
                    return false;
                } else if (currFile.dir.equals(directory)){
                    sameFileDiffDate = true;
                    sameFileDiffDateFile = currFile;
                    oldDate = currFile.lastModifiedDate;
                    break;
                }
            }
            if (sameFileDiffDate){
                nameFileList.remove(sameFileDiffDateFile);
            }
            nameFileList.add(file);
            nameMap.set(fileName, nameFileList);
        }
        
        if (dateMap.get(modifiedDate) == null && !dateMap.containsKey(modifiedDate)){
            ArrayList<FileData> list = new ArrayList<FileData>();
            list.add(file);
            dateMap.put(fileName, list);
        } else if (dateMap.containsKey(oldDate)){
            ArrayList<FileData> oldDateFileList = dateMap.get(oldDate);
            oldDateFileList.remove(sameFileDiffDateFile);
            if (oldDateFileList.isEmpty()){
                dateMap.remove(oldDate);
            }

            ArrayList<FileData> dateFileList = dateMap.get(modifiedDate);
            dateFileList.add(file);
            dateMap.replace(modifiedDate, dateFileList);
        } else{
            ArrayList<FileData> dateFileList = dateMap.get(modifiedDate);
            dateFileList.add(file);
            dateMap.replace(modifiedDate, dateFileList);
        }
        return true;
    }

    // TODO
    /**
     * This method should return a single FileData object with the given name and directory. 
     * You should not modify the FileSystem itself. Return null if such a file does not exist.
     * @param name the String name of the FileData to be added
     * @param directory the String directory name of the FileData to be added
     * @return FileData in FileSystem; or null if FileData is not found
     */
    public FileData findFile(String name, String directory) {
    	if (!nameMap.containsKey(name)){
            return null;
        } 
        else{
            ArrayList<FileData> nameList = nameMap.get(name);
            if(nameList.isEmpty()){
                return null;
            }
            for (FileData file : nameList){
                if(file.dir.equals(directory) && file.name.equals(name)){
                    return file;
                }
            }
        }
        return null;
    }

    // TODO
    /**
     * Returns ArrayList<String> representing all unique file names 
     *      across all directories within the fileSystem. 
     * You should not modify the FileSystem itself. 
     * Return an empty array list if there is no file in the file system yet.
     * @return ArrayList<String> of all unique file names in system; 
     *         null if there are no files in system yet.
     */
    public ArrayList<String> findAllFilesName() {
    	ArrayList<String> alistToReturn = new ArrayList<>();
    	if (nameMap.isEmpty()){
            return alistToReturn;
        }
    	return (ArrayList<String>)nameMap.keys();
    }

    // TODO
    /**
     * Returns a list of FileData with the given name. You should not modify the FileSystem itself. 
     * Return an empty list if such a file does not exist.
     * @param name the String name of the FileData to be added
     * @return ArrayList<FileData> with all filedata; empty if such file doesn't exist
     */
    public ArrayList<FileData> findFilesByName(String name) {
    	ArrayList<FileData> fileDataALToReturn = new ArrayList<>();
    	if (!nameMap.containsKey(name)){
            return fileDataALToReturn;
        } else{
            fileDataALToReturn = nameMap.get(name);
        }
    	return fileDataALToReturn;
    }

    // TODO
    /**
     * Returns a list of FileData with the given modifiedDate while not modifying the FileSystem. 
     * Return an empty list if such a file does not exist.
     * @param modifiedDate the String date of the Date to be added
     * @return ArrayList<FileData> with the modified dates; empty if it doesn't exist
     */
    public ArrayList<FileData> findFilesByDate(String modifiedDate) {
    	ArrayList<FileData> fileDataALToReturn = new ArrayList<>();
    	if (dateMap.containsKey(modifiedDate)){
            return nameMap.get(modifiedDate);
        } else{
            return fileDataALToReturn;
        }
    }

    // TODO
    /**
     * Return list of FileData with given modifiedDate that has 
     *      at least another file with the same file name in a different directory. 
     * You should not modify the FileSystem itself. 
     * Return an empty list if such a file does not exist.
     * @param modifiedDate the String date of the Date to be added
     * @return ArrayList<FileData> with the modified dates; empty if it doesn't exist
     */
    public ArrayList<FileData> findFilesInMultDir(String modifiedDate) {
    	ArrayList<FileData> fileDataALToReturn = new ArrayList<>();
    	ArrayList<FileData> dateList = dateMap.get(modifiedDate);
        for (FileData dateFile : dateList){
            String fileName = dateFile.name;
            if (nameMap.get(fileName).size() <= 1){
                continue;
            } else {
                fileDataALToReturn.add(dateFile);
            }

        }
    	return fileDataALToReturn;
    }

    // TODO
    /**
     * Removes all the files with the given name in the FileSystem. 
     * Return true if success, false otherwise. Remove the entry in the hashmap if necessary.
     * @param name the String name of the FileData to be added
     * @return true if successfully removed; false if otherwise
     */
    public boolean removeByName(String name) {
    	if (nameMap.get(name) == null){
            return false;
        } else {
            ArrayList<FileData> nameList = nameMap.get(name);
            for (FileData file : nameList){
                String modDate = file.lastModifiedDate;
                dateMap.get(modDate).remove(file);
                if (dateMap.get(modDate).isEmpty()){
                    dateMap.remove(modDate);
                }
            }
            nameMap.remove(name);
            return true;
        }
       
    }

    // TODO
    /**
     * This method should remove a certain file with the given name and directory. 
     * Return true if success, false otherwise. Remove the entry in the hashmap if necessary.
     * @param name the String name of the FileData to be added
     * @param directory the String directory name of the FileData to be added
     * @return true if successfully removed; false if otherwise
     */
    public boolean removeFile(String name, String directory) {
    	if (this.findFile(name, directory) == null){
            return false;
        } else {
            ArrayList<FileData> nameList = nameMap.get(name);
            FileData fileToRemove = null;
            String date = "";
            for (FileData file : nameList){
                if (file.name.equals(name) && file.dir.equals(directory)){
                    fileToRemove = file;
                    date = file.lastModifiedDate;
                    break;
                }
            }
            nameList.remove(fileToRemove);
            dateMap.get(date).remove(fileToRemove);
            if (nameMap.get(name).isEmpty()) {
            	nameMap.remove(name);
            }
            if (dateMap.get(date).isEmpty()) {
            	dateMap.remove(date);
            }
            return true;
        }
    }

}
