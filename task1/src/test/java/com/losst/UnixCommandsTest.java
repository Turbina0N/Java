package com.losst;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;

import java.io.IOException;

public class UnixCommandsTest 
{
    @Test
    public void testTouch() throws IOException
    {
        String testFileName = "testFile.txt";
	UnixCommands.touch(testFileName);
	File file = new File(testFileName);
	assertTrue(file.exists());
	file.delete();
    }

    @Test 
    public void testRm() throws IOException
    {
	String testFileName = "testFile.txt";
	File file = new File(testFileName);
	if(!file.exists()){
		file.createNewFile();
	}
	UnixCommands.rm(testFileName);
	assertTrue(!file.exists());
    }    

}
