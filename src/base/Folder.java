package base;

import java.util.ArrayList;
import java.io.*;
import java.util.List;
import java.util.Collections;

public class Folder implements Comparable<Folder>,Serializable {

	private ArrayList<Note> notes;
	private String name;
	private static final long serialVersionUID=1L;
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
	
	@Override
	public int compareTo(Folder f)
	{
		return this.name.compareTo(f.name);
	}
	
	public void sortNotes()
	{
	//	List<Note> notes = new ArrayList<Note>();
		Collections.sort(notes);
	}
	
	/*  my method on sorting notes.
	 *    1. find the total no. of boolean values we need to obtain. 
	 *  e.g. ex1: " key1 or key2 key3 or key4" we need to know 2 boolean values: "key1 or key2"   "key3 or key4"
	 *       ex2: "key1 key2 key3 or key4 or key5 or key6 key7" we need to know 4 boolean values: key1 , key2, "key3 or key4 or key5 or key6", key7
	 *       then we operate these boolean values use AND operator
	 *       for ex1: we do "key1 or key2" &&  "key3 or key4"
	 *       for ex2: we do key1 && key2 && (key3 or key4 or key5 or key6) && key7
	 *       if we want to get a true value of the AND operation, all the operands must be true value.
	 *       
	 *    2. Hence, we can first split the keywords into the smallest expression form. 
	 *    e.g. We split " key1 or key2 key3 or key4" into  "key1 or key2" and  "key3 or key4" .
	 *    then we evaluate "key1 or key2" , "key3 or key4", and store then to a boolean array. 
	 *    then we check the no. of "true" in this boolean array.
	 *    if all the elements of the boolean array has a "true" value, which means num_true = boolean array.length;
	 *    the final AND operation will be true. 
	 *    
	 *    3. create a new note list and add the note which satisfies the condition to the new list.
	 *    
	 *    4. return the new note list.
	 *   
	 */
	public List<Note> searchNotes(String keywords)
	{
		List<Note> NOTE= new ArrayList<Note>();
		String []S_array=keywords.split(" ");
		int num_OR=0;
		for(String s: S_array)
		{
			if(s.compareToIgnoreCase("or")==0)
				num_OR+=1;
	 
		}
		
		int bool_len= S_array.length-2*num_OR;
		 
		for( Note n: notes)
		{
			    boolean []bArray = new boolean[bool_len]; 
			    boolean temp; 
			    int record=0;
				for(int i=0, j=0;i<S_array.length && j<bool_len;++j)
				{
					
					if(S_array.length==1) 
					{
						if(n instanceof TextNote)
						   bArray[j]=((TextNote) n).getContent().toLowerCase().contains(S_array[i].toLowerCase())||((TextNote) n).getTitle().toLowerCase().contains(S_array[i].toLowerCase());
						else	
						   bArray[j] = n.getTitle().toLowerCase().contains(S_array[0].toLowerCase());
						   ++i;	
					}
					
					else 
					{
						if(S_array[i].compareToIgnoreCase("or")!=0 && S_array[i+1].compareToIgnoreCase("or")!=0)
					{
						if(n instanceof TextNote)
							bArray[j]=((TextNote) n).getContent().toLowerCase().contains(S_array[i].toLowerCase())||((TextNote) n).getTitle().toLowerCase().contains(S_array[i].toLowerCase());
						else	
						bArray[j]= n.getTitle().toLowerCase().contains(S_array[i].toLowerCase());
						++i;
						
					}
					    else if(S_array[i].compareToIgnoreCase("or")!=0 && S_array[i+1].compareToIgnoreCase("or")==0)
					 {
						if(n instanceof TextNote)
						temp=((TextNote) n).getContent().toLowerCase().contains(S_array[i].toLowerCase())||((TextNote) n).getTitle().toLowerCase().contains(S_array[i].toLowerCase())
						||((TextNote) n).getContent().toLowerCase().contains(S_array[i+2].toLowerCase())||((TextNote) n).getTitle().toLowerCase().contains(S_array[i+2].toLowerCase());
						else
						temp=n.getTitle().toLowerCase().contains(S_array[i].toLowerCase())||n.getTitle().toLowerCase().contains(S_array[i+2].toLowerCase());
						
						
						
						for(int k=i+2;k<S_array.length-1 ;)
						{
						  if(S_array[k+1].compareToIgnoreCase("or")==0)
						  {
							if(n instanceof TextNote)
								temp=temp||((TextNote) n).getContent().toLowerCase().contains(S_array[k+2].toLowerCase())||n.getTitle().toLowerCase().contains(S_array[k+1].toLowerCase());
								
							else
							temp=temp||n.getTitle().toLowerCase().contains(S_array[k+2].toLowerCase());
							
							k+=2;
						    record=k;
						  }
						  else{
							  record=k;
							  break;
						  }
						}  
						bArray[j]=temp;
						i=record+1;
					}
				}
			}
		    int num=0;
			for(boolean b: bArray)
			{
				if(b)
				num++;		
			}
			
			if(num==bool_len)
				NOTE.add(n);
		}

		return NOTE;
	}
	
	
}
