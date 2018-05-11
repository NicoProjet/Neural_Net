package neuralNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("hello");
		ArrayList<Integer> sizes = new ArrayList<Integer>();
		sizes.add(784); sizes.add(30); sizes.add(10);
		NeuralNet net = new NeuralNet(sizes);
		System.out.println("I have a neural net");
		System.out.println("You jelly?");
		// gradientDescent(ArrayList<ArrayList<double[]>> data, int iteration, int batchSize, double learningRate)
		ArrayList<ArrayList<double[]>> data = new ArrayList<ArrayList<double[]>>();
		int numberOfImages = 10;
		double[] images = ReadMNIST.readImages(numberOfImages);
		double[] labels = ReadMNIST.readLabels(numberOfImages);
		for (int i=0; i<numberOfImages; i++){
			ArrayList<double[]> singleData = new ArrayList<double[]>();
			singleData.add(Arrays.copyOfRange(images, i*784, (i+1)*784));
			singleData.add(Arrays.copyOfRange(labels, i, i+1));
			data.add(singleData);
		}
		ArrayList<ArrayList<Double>> input = new ArrayList<ArrayList<Double>>();
		for (int i=0; i<784; i++){
			ArrayList<Double> pixel = new ArrayList<Double>();
			pixel.add(images[i]);
			input.add(pixel);
		}
		
		System.out.println("feed start");
		System.out.println(net.guess(input));
		System.out.println("grad desc start");
		net.gradientDescent(data, 5, 2, 3.0);
		System.out.println("second feed start");
		System.out.println(net.guess(input));
		
	}
	
	public static void printVector(double[] vector){
		for (int i = 0; i<vector.length; i++){
			System.out.print(vector[i]);
		}
		System.out.println();
	}

}
