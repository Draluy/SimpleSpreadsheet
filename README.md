# Simple Spreadsheet



## What is it

SimpleSpreadsheet is an opinionated reader-only of tabular data, be it csv, xls or xlsx. 

It aims to be stupid simple to use, with the following focus:
- All data is returned as strings
- The data returned is the same as what is displayed on the screen when opening the csv/xls/xlsx on LibreOffice/Office, with the exception of dates: in order to be easily parseable by a program, dates are returned as an ISO-like format (could be ISO, but no guarantees)
- empty lines and blank cells are skipped when logical

## How to use

### Import the dependency

Maven
```
<dependency>
  <groupId>fr.raluy.simplespreadsheet</groupId>
  <artifactId>simplespreadsheet</artifactId>
  <version>2.0.0</version>
</dependency>
```

Gradle
```
implementation("fr.raluy.simplespreadsheet:simplespreadsheet:2.0.0")
```

### Read the file

The API has one reader to call. You can get arrays of values, a list of lists, or a list of objects. Whatever the method you call, you will get Strings, one for each cell.

**You can read input streams, byte arrays, or a path**. For brievity, we use paths below. 

> **Warning:** The filename can be provided or omitted, but can lead to bad file type  detection if you dont provide it with a byte array or input stream.

#### Read as an array
```
val array = SSReader(path, fileName).readToArray()
```
Where **array** is an array of arrays of the values in the file as strings

#### Read as a list
```
val collection = SSReader(path, fileName).readToCollection()
```
Where **collection** is a list of lists, containing the values in the file as strings

#### Read as objects
```
val objects = SSReader(path, fileName).readToObjects(GenericLine::class)
```
Where **objects** is a list of objects instanciated (here instances of GenericLine). You can pass any class you like, as long as:
- The object has a constructor accepting strings as parameters. The first column of the file will go in the first parameter, etc. Order is important.
- Or, alternatively, the object has a default constructor and the properties can be set using reflection. The declaration order of the properties is important, as the values of the first column in the tabular data will go in the first atribute, and so on.

> If a constrcutor has too many fields compared to the tabular data, the last parameters will be fed null values. May fail if the values are non-nullable.
> 
> If a constructor has too few parameters, the first columns of the tabular data will be fed to the constructor, and the rest will be skipped.

#### Read skipping headers (skips the first line)
```
val collection = SSReader(path, fileName).skipHeaders().readToCollection()
```

#### Read a particular sheet
```
val collection = SSReader(path, fileName).readToCollection("SheetName")
```

If the sheet does not exist, it will fail. 
If the parameter is not provided, the first sheet will be read.

If you try reading a csv file **AND** specifying a sheet, it will fail.

## But why

Because who has the time, every single project, to create a horrible boilerplate of Apache poi/common csv/opencsv code to read a file. 
The API is too big, too messy, too complicated for simple uses. 90% of the time, you don't need to read the color of the cell, or the alignment of the text. Most of the time, you just want to read the values of the cells, as they appear.

So this project wraps apache poi and opencsv to provide a unified, simple API with the least amount of surprises possible.
