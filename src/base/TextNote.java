package base;


public class TextNote extends Note
{
	 String content;
	
	public TextNote(String TextNodeName){
		
		super(TextNodeName);
	}
	
	public TextNote(String title, String Content)
	{
		
		super(title);
		this.content=Content;
	}
}
