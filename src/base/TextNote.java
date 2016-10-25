package base;
import java.io.*;
import java.util.HashMap;


public class TextNote extends Note implements Serializable
{
	 private String content;
	 private static final long serialVersionUID=1L;
	
	public TextNote(String TextNodeName){
		
		super(TextNodeName);
	}
	
	public TextNote(String title, String Content)
	{
		
		super(title);
		this.content=Content;
	}
	
	public TextNote(File f){
		super(f.getName());
		this.content= getTextFromFile(f.getAbsolutePath());
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public void setContent(String content){
		this.content=content;
	}
	// lab 5
	
	private String getTextFromFile(String absolutePath){
		String result = "";
		//TODO
		File f =new File(absolutePath);
		try{
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line;			
			while((line = in.readLine()) != null)
			{
			    result+=(line);
			}
			in.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}

		return result;
	}
	
	public void exportTextToFile(String pathFolder){
		//TODO
		File file=null;
		
	    if(pathFolder=="")
	    	 file =new File(pathFolder + this.getTitle().replaceAll(" ", "_")+".txt");
	    
	    else
	     	 file =new File(pathFolder + File.separator + this.getTitle().replaceAll(" ", "_")+".txt");
	    
		
		//TODO
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(this.content);
			out.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//lab 6
	public Character countLetters(){
		HashMap<Character,Integer> count = new HashMap<Character,Integer>();
		String a = this.getTitle() + this.getContent();
		int b = 0;
		Character r = ' ';
		for (int i = 0; i < a.length(); i++) {
			Character c = a.charAt(i);
			if (c <= 'Z' && c >= 'A' || c <= 'z' && c >= 'a') {
				if (!count.containsKey(c)) {
					count.put(c, 1);
				/*	if (count.get(c) > b) {
						b = count.get(c);
						r = c;
					}*/
				} else {
					count.put(c, count.get(c) + 1);
					if (count.get(c) > b) {
						b = count.get(c);
						r = c;
					}
				}
			}
		}
		return r;
	}
	
}
