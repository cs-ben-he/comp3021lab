package base;
import java.io.*;
import java.util.Date;

public class Note implements Comparable<Note>,Serializable{
	private Date date;
	private String title;
	private static final long serialVersionUID=1L;
	
	public Note(String Titlename){
		this.title=Titlename;
		date = new Date(System.currentTimeMillis());
	}
	
	public String getTitle(){
		
		return this.title;
	}
	
	// click "Source" ->click "Generate hashcode() and equals()"-> choose "title"
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Note other = (Note) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Note o)
	{
		int i= this.date.compareTo(o.date);
		if(i<0)
		    return 1;
		else if(i==0)
			return 0;
		else
			return -1;
	
	}
	
	
	public String toString(){
		return date.toString() + "\t" + title;
	}
}

