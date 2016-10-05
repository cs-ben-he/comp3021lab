package base;
import java.io.*;

public class TextNote extends Note implements Serializable
{
	 String content;
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
	
		File file =new File(pathFolder + this.getTitle().replaceAll(" ", "_")+".txt");
		//TODO
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(file,true));
			out.write(this.content);
			out.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
