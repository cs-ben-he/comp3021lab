package base;

import java.io.*;

public class ImageNote extends Note implements Serializable{

    File image;
    private static final long serialVersionUID=1L;
	public ImageNote(String ImageNotename){
		
		super(ImageNotename);
		
	}
}
