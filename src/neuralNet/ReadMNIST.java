package neuralNet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
//import org.apache.commons.io.IOUtils;
import java.lang.Object;

class ReadMNIST {
	/*
	public static ArrayList<Byte> readLabels(){
		ArrayList<Byte> labels = new ArrayList<Byte>();
		return labels;
	}
	*/
	
	public static byte[] readLabels(int numberOfLabels) throws IOException{
		File MNIST_labels = new File("data/train-labels.idx1-ubyte");
		InputStream fileStream = new FileInputStream(MNIST_labels);
		return null;
	}

}
