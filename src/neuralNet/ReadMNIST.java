package neuralNet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;


class ReadMNIST {
	public static int imageSize = 28;
	
	private static double[] byteArrayToDoubleArray(byte[] array, int headerSize, boolean pixel){
		double[] doubleArray = new double[array.length-headerSize];
		for (int i=0; i<array.length-headerSize; i++){
			int intValue = array[i+headerSize] & 0xff;
			if (pixel) doubleArray[i] = (double) intValue / 255;
			else doubleArray[i] = (double) intValue;
		}
		return doubleArray;
	}
	
	public static double[] readLabels(int numberOfLabels) throws IOException{
		File MNIST_labels = new File("data/train-labels.idx1-ubyte");
		InputStream fileStream = new FileInputStream(MNIST_labels);
		int headerSize = 8;
		byte[] byteArray = IOUtils.toByteArray(fileStream, numberOfLabels+headerSize);
		return byteArrayToDoubleArray(byteArray, headerSize, false);
	}
	
	public static double[] readImages(int numberOfImages) throws IOException{
		File MNIST_images = new File("data/train-images.idx3-ubyte");
		InputStream fileStream = new FileInputStream(MNIST_images);
		int headerSize = 16;
		int bytesToGet = numberOfImages * imageSize * imageSize + headerSize;
		byte[] byteArray =  IOUtils.toByteArray(fileStream, bytesToGet);
		return byteArrayToDoubleArray(byteArray, headerSize, true);
	}

}
