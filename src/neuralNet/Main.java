package neuralNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) throws IOException {
		ArrayList<Integer> sizes = new ArrayList<Integer>();
		sizes.add(784); sizes.add(30); sizes.add(10);
		NeuralNet net = new NeuralNet(sizes);
		// gradientDescent(ArrayList<ArrayList<double[]>> data, int iteration, int batchSize, double learningRate)
		ArrayList<ArrayList<double[]>> data = new ArrayList<ArrayList<double[]>>();
		ArrayList<ArrayList<double[]>> testData = new ArrayList<ArrayList<double[]>>();
		int numberOfImages = 100;
		int numberOfTests = 10;
		double[] images = ReadMNIST.readImages(numberOfImages+numberOfTests);
		double[] labels = ReadMNIST.readLabels(numberOfImages+numberOfTests);
		for (int i=0; i<numberOfImages; i++){
			ArrayList<double[]> singleData = new ArrayList<double[]>();
			singleData.add(Arrays.copyOfRange(images, i*784, (i+1)*784));
			singleData.add(Arrays.copyOfRange(labels, i, i+1));
			data.add(singleData);
		}
		for (int i=0; i<numberOfTests; i++){
			ArrayList<double[]> singleData = new ArrayList<double[]>();
			singleData.add(Arrays.copyOfRange(images, (numberOfImages+i)*784, (numberOfImages+i+1)*784));
			singleData.add(Arrays.copyOfRange(labels, numberOfImages+i, numberOfImages+i+1));
			testData.add(singleData);
		}
		ArrayList<ArrayList<Double>> input = new ArrayList<ArrayList<Double>>();
		for (int i=0; i<784; i++){
			ArrayList<Double> pixel = new ArrayList<Double>();
			pixel.add(images[i]);
			input.add(pixel);
		}
		for (int i=0; i<28; i++){
			for (int j=0;j<28;j++){
				System.out.print(String.format("%.2f", input.get(i*28+j).get(0)));
			}
			System.out.println();
		}
		
		System.out.println("feed start");
		System.out.println(net.guess(input));
		System.out.println("grad desc start");
		net.gradientDescent(data, 15, 10, 3.0, testData);
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
