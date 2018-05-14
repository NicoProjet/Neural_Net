package neuralNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) throws IOException {
		/*
		ArrayList<ArrayList<Double>> test = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> line = new ArrayList<Double>();
		line.add((double) -2.74051506);
		test.add(line);
		line = new ArrayList<Double>();
		line.add((double) -6.40527438);
		test.add(line);
		line = new ArrayList<Double>();
		line.add((double) -1.66373931);
		test.add(line);
		line = new ArrayList<Double>();
		line.add((double) -9.18389012);
		test.add(line);
		line = new ArrayList<Double>();
		line.add((double) -0.88628164);
		test.add(line);
		line = new ArrayList<Double>();
		line.add((double) -6.9870387);
		test.add(line);
		line = new ArrayList<Double>();
		line.add((double) -5.73383871);
		test.add(line);
		line = new ArrayList<Double>();
		line.add((double) -4.44236584);
		test.add(line);
		line = new ArrayList<Double>();
		line.add((double) -5.16724836);
		test.add(line);
		line = new ArrayList<Double>();
		line.add((double) -6.18543175);
		test.add(line);
		NeuralNet.printArrayList(NeuralNet.sigmoid(test));
		
		ArrayList<ArrayList<Double>> OnesList = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i<test.size();i++){
			ArrayList<Double> line0 = new ArrayList<Double>();
			for (int j=0; j<test.get(0).size();j++){
				line0.add((double) 1);
			}
			OnesList.add(line0);
		}
		NeuralNet.printArrayList(OnesList);
		NeuralNet.printArrayList(NeuralNet.sub(OnesList, NeuralNet.sigmoid(test)));
		NeuralNet.printArrayList(NeuralNet.mul(NeuralNet.sigmoid(test), NeuralNet.sub(OnesList, NeuralNet.sigmoid(test))));
		*/
		
		/* 4x3 3x1 4x1
		ArrayList<ArrayList<Double>> w = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> a = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> b = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> r = new ArrayList<ArrayList<Double>>();
		for (int i=0;i<4;i++){
			ArrayList<Double> line = new ArrayList<Double>();
			for (int j=0; j<3; j++){
				line.add((double) i*3+j+1);
			}
			w.add(line);
		}
		NeuralNet.printArrayList(w);
		
		for (int i=0; i<3; i++){
			ArrayList<Double> line = new ArrayList<Double>();
			for (int j=0; j<1; j++){
				line.add((double) i*1+j+1);
			}
			a.add(line);
		}
		NeuralNet.printArrayList(a);
		
		for (int i=0; i<4; i++){
			ArrayList<Double> line = new ArrayList<Double>();
			for (int j=0; j<1; j++){
				line.add((double) i*1+j+1);
			}
			b.add(line);
		}
		NeuralNet.printArrayList(b);
		NeuralNet.printArrayList(NeuralNet.matrixMul(w,a));
		r = NeuralNet.matrixAdd(NeuralNet.matrixMul(w,a),b);
		NeuralNet.printArrayList(r);
		*/
		
		
		
		
		ArrayList<Integer> sizes = new ArrayList<Integer>();
		sizes.add(784); sizes.add(30); sizes.add(10);
		NeuralNet net = new NeuralNet(sizes);
		// gradientDescent(ArrayList<ArrayList<double[]>> data, int iteration, int batchSize, double learningRate)
		ArrayList<ArrayList<double[]>> data = new ArrayList<ArrayList<double[]>>();
		ArrayList<ArrayList<double[]>> testData = new ArrayList<ArrayList<double[]>>();
		int numberOfImages = 5000;
		int numberOfTests = 100;
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
