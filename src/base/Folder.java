package base;

import java.util.ArrayList;
import java.util.List;

public class Folder {

	private ArrayList<Note> notes;
	private String name;
	
	public Folder(String FolderName){
		notes= new ArrayList<Note>();
		name=FolderName;
	}
	
	public void addNote(Note n1){
		notes.add(n1);
	}
	
	public String getName(){
		return this.name;
	}
	
	public List<Note> getNotes(){
		
		return notes;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Folder other = (Folder) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		
		int nText=0;
		int nImage=0;
		
		for(Note n1:notes){
			if(n1 instanceof TextNote){
				++nText;
			}
			else if(n1 instanceof ImageNote){
				++nImage;
			}
		}
			
		return this.name + ":"+nText+":"+nImage;
	}
}
