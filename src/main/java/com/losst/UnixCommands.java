package com.losst;
import java.io.File;
import java.io.IOException;

public class UnixCommands
{

    public static void touch(String fileName){
    	File file = new File(fileName);
	try {
		if (file.exists() || file.createNewFile()){
			System.out.println("Файл создан или уже существует: " + file.getAbsolutePath());
		}
		else {
			System.out.println("Не удалось создать файл: " + file.getAbsolutePath());
		}
	}
	catch (IOException e){
		System.out.println("Произошла ошибка при работе с файлом: " + e.getMessage());
	}
    	
    }


    public static void rm(String fileName){
    	File file = new File(fileName);
	if (file.delete()){
		System.out.println("Файл был удален: " + file.getAbsolutePath());
	}
	else{
		System.out.println("Не удалось удалить файл: " + file.getAbsolutePath());
	}
    
    }    


    public static void main( String[] args )
    {
        if (args.length != 2){
		System.out.println("Неверное использование команды.");
		System.out.println("Правильное использование: java UnixCommands <touch/rm> <имя файла>");
		return;
	}
	String command = args[0];
	String fileName = args[1];

	if ("touch".equals(command)){
		touch(fileName);
	} 
	else if ("rm".equals(command)){
		rm(fileName);
	}
	else{
		System.out.println("Нет реализации такой команды: " + command);
	}
    }
    
}
