package com.baeldung.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class FileOperationsTest {

    @Test
    public void givenFileName_whenUsingClassloader_thenFileData() throws IOException {
        String expectedData = "Hello World from fileTest.txt!!!";

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("fileTest.txt").getFile());
        InputStream inputStream = new FileInputStream(file);
        String data = readFromInputStream(inputStream);
        
        Assert.assertEquals(expectedData, data.trim());
    }

    @Test
    public void givenFileNameAsAbsolutePath_whenUsingClasspath_thenFileData() throws IOException {
        String expectedData = "Hello World from fileTest.txt!!!";
        
        Class clazz = FileOperationsTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/fileTest.txt");
        String data = readFromInputStream(inputStream);

        Assert.assertEquals(expectedData, data.trim());
    }

    @Test
    public void givenFileName_whenUsingJarFile_thenFileData() throws IOException {
        String expectedData = "BSD License";

        Class clazz = Matchers.class;
        InputStream inputStream = clazz.getResourceAsStream("/LICENSE.txt");
        String data = readFromInputStream(inputStream);

        Assert.assertThat(data.trim(), CoreMatchers.containsString(expectedData));
    }

    @Test
    public void givenURLName_whenUsingURL_thenFileData() throws IOException {
        String expectedData = "Baeldung";

        URL urlObject = new URL("http://www.baeldung.com/");

        URLConnection urlConnection = urlObject.openConnection();

        InputStream inputStream = urlConnection.getInputStream();
        String data = readFromInputStream(inputStream);

        Assert.assertThat(data.trim(), CoreMatchers.containsString(expectedData));
    }
    
    @Test
    public void givenFileName_whenUsingFileUtils_thenFileData() throws IOException {
        String expectedData = "Hello World from fileTest.txt!!!";
        
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("fileTest.txt").getFile());
        String data = FileUtils.readFileToString(file);
        
        Assert.assertEquals(expectedData, data.trim());
    }
    
    @Test
    public void givenFilePath_whenUsingFilesReadAllBytes_thenFileData() throws IOException {
        String expectedData = "Hello World from fileTest.txt!!!";
        
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("fileTest.txt").getFile());
        Path path = Paths.get(file.getAbsolutePath());
        
        byte[] fileBytes = Files.readAllBytes(path);
        String data = new String(fileBytes);

        Assert.assertEquals(expectedData, data.trim());
    }
    
    @Test
    public void givenFilePath_whenUsingFilesLines_thenFileData() throws IOException {
        String expectedData = "Hello World from fileTest.txt!!!";
        
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("fileTest.txt").getFile());
        Path path = Paths.get(file.getAbsolutePath());
        
        StringBuilder data = new StringBuilder();
        Stream<String> lines = Files.lines(path);
        lines.forEach(new Consumer<String>() {
            @Override
            public void accept(String line) {
                data.append(line).append("\n");
            }
        });
        
        Assert.assertEquals(expectedData, data.toString().trim());
    }
    
    private String readFromInputStream(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder resultStringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            resultStringBuilder.append(line);
            resultStringBuilder.append("\n");
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        return resultStringBuilder.toString();
    }
}