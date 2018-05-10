package neuralNet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;


class ReadMNIST {
	public static int imageSize = 28;
	
	private static int[] byteArrayTointArray(byte[] array, int headerSize){
		int[] intArray = new int[array.length];
		for (int i=0; i<array.length-headerSize; i++){
			intArray[i] = array[i+headerSize] & 0xff;
		}
		return intArray;
	}
	
	public static int[] readLabels(int numberOfLabels) throws IOException{
		File MNIST_labels = new File("data/train-labels.idx1-ubyte");
		InputStream fileStream = new FileInputStream(MNIST_labels);
		byte[] byteArray = IOUtils.toByteArray(fileStream, numberOfLabels);
		int headerSize = 8;
		return byteArrayTointArray(byteArray, headerSize);
	}
	
	public static int[] readImages(int numberOfImages) throws IOException{
		File MNIST_images = new File("data/train-images.idx3-ubyte");
		InputStream fileStream = new FileInputStream(MNIST_images);
		int headerSize = 16;
		int bytesToGet = numberOfImages * imageSize * imageSize + headerSize;
		byte[] byteArray =  IOUtils.toByteArray(fileStream, bytesToGet);
		return byteArrayTointArray(byteArray, headerSize);
	}

}
