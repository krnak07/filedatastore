# Data Store<br>
## Installation<br>
`add 'fileDataStore.jar' to your project library`<br>
## Usage
`import org.datastore.*;`<br>
`CRD ds = new CRD(pathToFile);`<br>
path - The path to file where the data-store is to be stored. This is optional, if the field is left blank a file named<br>

### Create<br>
`create(String Key, JSONObject value, int TTL)`<br>
This function creates a new key-value pair in the file.<br>
key - the key for the value, capped at 32Chars<br>
value - json object that store the content, capped at 16KB<br>
TTL - time-to-live parameter for the key-value pair's existence. It has a default time of infinity unless specified while creation.<br>
`data-store.txt` is created in the current working directory.
### Read<br>
`read(String key)`<br>
This functions returns the corresponding value for the given key if and only if the key-value is present in the file.<br>
key - the value for the key, capped at 32Chars
### Delete<br>
`delete(String key)`<br>
This function deletes the key-value pair from the file for the key specified. Also it simultaneously deletes all the expired key-value pairs in the file.
key - the value for the key, capped at 32Chars
# Examples<br>
`import org.datastore.*;`<br>
`CRD ds = new CRD(pathToFile);`<br>

Throws `error : size of the file exceeds 1GB. Choose another File.` , if the size of the selected file exceeds 1GB.<br>

### create<br>
`String c = ds.create(key,value,TTL);`<br>
`System.out.println(c);`<br><br>
<b>OUTPUT</b><br>
`created`<br>
or<br>
`error : Key length exceeded 32 characters`<br>
or<br>
`error : value length exceeded 16KB`<br>
or<br>
`error : Key already exists`<br><br>
### read<br>
`JSONObject r = ds.read(key);`<br>
`System.out.println(r.toString());`<br>
<b>OUTPUT</b><br>
`JSONObject of value for the key`<br>
or<br>
`error : Key length exceeded 32 characters`<br>
or<br>
`error : Key doesn't exists`<br><br>
### delete<br>
`String d = ds.delete(key);`<br>
`System.out.println(d);`<br>
<b>OUTPUT</b><br>
`deleted`<br>
or<br>
`error : Key length exceeded 32 characters`<br>
or<br>
`error : Key doesn't exists`<br><br>
# Unit Testing<br>
<b>JUNIT</b> folder comprises of the unit testing files.<br>
To run test - `run testRunner.java`

# Components<br>
`fileDataStore.jar` is the JAR file for the project<br>

<b>File-Data-Store</b> contains a interface file and a java file.<br>
`fileDataStore.java` is  interface file denoting the functions in the library<br>
`CRD.java` contains the main program that runs the library.

<b>JUNIT</b> contains all the test files.

 


