package base;

import java.util.ArrayList;
import java.io.*;
import java.util.List;
import java.util.Collections;

public class NoteBook implements Serializable{
  
	private ArrayList<Folder> folders;
	private static final long serialVersionUID=1L;
	
	public NoteBook()
	{
		folders= new ArrayList<Folder>();
	}
	
	//lab 8
	public void addFolder(String folderName){
		this.folders.add(new Folder(folderName));
	}
	public boolean createTextNote(String folderName,String title)
	{
		TextNote Tnote= new TextNote(title);
		return insertNote(folderName, Tnote);
	}
	
	public boolean createTextNote(String folderName,String title, String content)
	{
		TextNote Tnote= new TextNote(title,content);
		return insertNote(folderName, Tnote);
	}
	
	public boolean createImageNote(String folderName,String Content)
	{
		ImageNote Inote= new ImageNote(Content);
		return insertNote(folderName, Inote);
	}
	
	public boolean insertNote(String folderName,Note n1)
	{
		Folder newF=null;
		for(Folder f1: folders)
		{
			if(f1.equals(new Folder(folderName)))
			{
				newF=f1;
			}
		}
		
		if(newF==null){
			//add a new folder to list folders.
			newF= new Folder(folderName);
			folders.add(newF);
			}
		
		for(Note n: newF.getNotes())
		{
			// the equals() function is applied by two same type objects
			//so we need create casting objects (because n n1 may be different class types, eg: n: TextNote but n1: ImageNote)
			Object o1= new Note(n.getTitle());
			Object o2= new Note(n1.getTitle());
			
			if(o1.equals(o2))
			{
				System.out.println("Create note "+ n1.getTitle() + " under folder " + folderName + " failed");
				return false;
			}
		}
		
	    newF.addNote(n1);
		return true;
	}
	
	public ArrayList<Folder> getFolders()
	{
		return folders;
		
	}
	
	public void sortFolders()
	{
	//	ArrayList<Folder> folders = new ArrayList<Folder>();
		
		for(Folder f : folders)
		{	
		  f.sortNotes();  
		}
		
		Collections.sort(folders);
		
	}
	
	public List<Note> searchNotes(String Keywords)
	{
		List<Note> newN= new ArrayList<Note>();	
		for(Folder f: folders)
		{
			for(Note n:f.searchNotes(Keywords))
			{
				newN.add(n);
			}
		}
		
		return newN;
	}
	
	public boolean save(String file){
		//todo
		FileOutputStream fos=null;
		ObjectOutputStream out=null;
		try{
			//TODO
			fos=new FileOutputStream(file);
			out=new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public NoteBook(String file){
		//TODO
		FileInputStream fis=null;
		ObjectInputStream in=null;
		try{
			fis =new FileInputStream(file);
			in=new ObjectInputStream(fis);
			NoteBook nb=(NoteBook)in.readObject();
			this.folders=nb.folders;
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
						
	}
	
	
	
}
