package neuralNet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Main {
	public static void main(String[] args) throws IOException {
		ArrayList<Integer> sizes = new ArrayList<Integer>();
		sizes.add(784); sizes.add(30); sizes.add(10);
		NeuralNet net = new NeuralNet(sizes);
		ArrayList<ArrayList<double[]>> data = new ArrayList<ArrayList<double[]>>();
		ArrayList<ArrayList<double[]>> testData = new ArrayList<ArrayList<double[]>>();
		int numberOfImages = 100;
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
		//net.gradientDescent(data, 5, 10, 3.0, testData);
		System.out.println("second feed start");
		System.out.println(net.guess(input));
		//net._save();
		net._import();
		
		String userInput = "";
		boolean _continue = true;
		Scanner scanInput = new Scanner(System.in);
		do {
			System.out.println("Please input image name (or q to quit):");
			userInput = scanInput.nextLine();
			if (userInput.equals("q")){
				_continue = false;
				System.out.println("See you soon!");
			} else{
				File imageFile = new File("data/"+userInput);
				BufferedImage image = ImageIO.read(imageFile);
				ArrayList<ArrayList<Double>> imageMatrix = getImageFromJPG(image);
				int guess = net.guess(imageMatrix);
				System.out.println("Your image was a "+guess);
			}
		} while (_continue);
		scanInput.close();
	}
	
	public static void printVector(double[] vector){
		for (int i = 0; i<vector.length; i++){
			System.out.print(String.format("%.2f", vector[i])+ " ");
		}
		System.out.println();
	}
	
	public static void printImageVector(double[] vector){
		for (int i = 0; i<(vector.length/28); i++){
			for (int pixel = 0; pixel<28; pixel++){
				System.out.print(String.format("%.2f", vector[i])+ " ");
			}
			System.out.println();
		}
	}
	
	private static ArrayList<ArrayList<Double>> getImageFromJPG(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				ArrayList<Double> pixel = new ArrayList<Double>();
				pixel.add(((double) (255-(image.getRGB(col, row) ) & 0x000000FF))/255);
				result.add(pixel);
			}
		}
		return result;
	}
}
