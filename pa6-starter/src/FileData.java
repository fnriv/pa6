public class FileData {

	public String name;
	public String dir;
	public String lastModifiedDate;

	// TODO
	public FileData(String name, String directory, String modifiedDate) {
		if (name == null) {
			this.name = "";
		} else {
			this.name = name;
		}

		if (directory == null) {
			this.dir = "/";
		} else {
			this.dir = directory;
		}

		if (modifiedDate == null) {
			this.lastModifiedDate = "01/01/2021";
		} else {
			this.lastModifiedDate = modifiedDate;
		}
	}

	// TODO
	public String toString() {
		return "{Name: " + this.name + 
			", Directory: " + this.dir + 
			", Modified Date: " + this.lastModifiedDate + "}";
	}
}
