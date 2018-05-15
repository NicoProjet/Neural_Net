package neuralNet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;


class NeuralNet {

	int nLayers;
	ArrayList<Integer> sizes;
	ArrayList<ArrayList<ArrayList<Double>>> biases;
	ArrayList<ArrayList<ArrayList<Double>>> weights;
	/* biases = [layer[node[bias[1]]]
	 * weights = [layer[node[weights[#nodes last layer]]]
	 */



	public NeuralNet(ArrayList<Integer> givenSize){

		sizes = new ArrayList<Integer>();
		biases = new ArrayList<ArrayList<ArrayList<Double>>>();
		weights = new ArrayList<ArrayList<ArrayList<Double>>>();

		nLayers = givenSize.size();
		sizes = givenSize;
		int x,y;
		ArrayList<ArrayList<Integer>> zipped= new ArrayList<ArrayList<Integer>>();
		zipped = zip( subArrayList(sizes, 0, nLayers-1) ,  subArrayList(sizes, 1, nLayers));

		for (int i=1; i<nLayers; i++){
			biases.add(randn(sizes.get(i), 1));
		}

		for (int i=0; i<zipped.size(); i++){
			x = zipped.get(i).get(0);
			y = zipped.get(i).get(1);
			weights.add(randn(y, x));
		}
	}

	private ArrayList<Integer> subArrayList(ArrayList<Integer> list, int lower, int upper){

		ArrayList<Integer> subList = new ArrayList<Integer>();

		for (int i=lower; i<upper; i++){
			subList.add(list.get(i));
		}

		return subList;
	}

	private ArrayList<ArrayList<Double>> randn(int nList, int nRandom){

		Random x = new Random();
		double n;
		ArrayList<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> listnRandom = new ArrayList<Double>();

		for (int i=0; i<nList; i++){
			for (int j=0; j<nRandom; j++){
				n = x.nextGaussian();
				listnRandom.add(n);
			}
			list.add(listnRandom);
			listnRandom = new ArrayList<Double>();
		}

		return list;
	}

	private ArrayList<ArrayList<Integer>> zip(ArrayList<Integer> list1, ArrayList<Integer> list2){

		ArrayList<ArrayList<Integer>> zippedList = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tuple = new ArrayList<Integer>();
		int length = list1.size();
		if (list2.size()<length){
			length = list2.size();
		}

		for (int i=0; i<length;i++){
			tuple.add(list1.get(i));
			tuple.add(list2.get(i));

			zippedList.add(tuple);
			tuple = new ArrayList<Integer>();
		}

		return zippedList;
	}

	private ArrayList<ArrayList<Double>> add(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B){
		for (int index=0; index<A.size();index++){
			//A.get(index).get(0) = A.get(index).get(0) + B.get(index).get(0);
			A.get(index).set(0, A.get(index).get(0) + B.get(index).get(0));
		}
		return A;
	}
	
	private ArrayList<ArrayList<Double>> sub(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B){
		for (int index=0; index<A.size();index++){
			//A.get(index).get(0) = A.get(index).get(0) + B.get(index).get(0);
			A.get(index).set(0, A.get(index).get(0) - B.get(index).get(0));
		}
		return A;
	}

	private ArrayList<ArrayList<Double>> mul(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B){
		if(A.get(0).size() != B.get(0).size()){
			System.out.println("check matrix sizes (mul)");
			return A;
		}
		ArrayList<ArrayList<Double>> response = new ArrayList<ArrayList<Double>>();
		for (int line = 0; line<A.size();line++){
			ArrayList<Double> newLine = new ArrayList<Double>();
			for(int column = 0; column<A.get(0).size(); column++){
				newLine.add(A.get(line).get(column) * B.get(line).get(0));
			}
			response.add(newLine);
		}
		return response;
	}
	
	private ArrayList<ArrayList<Double>> matrixAdd(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B){
		ArrayList<ArrayList<Double>> response = new ArrayList<ArrayList<Double>>();
		for (int line=0; line<A.size();line++){
			ArrayList<Double> responseLine = new ArrayList<Double>();
			for (int column=0; column<A.get(0).size(); column++){
				responseLine.add(A.get(line).get(column) + B.get(line).get(column));
			}
			response.add(responseLine);
		}
		return response;
	}
	
	private ArrayList<ArrayList<Double>> matrixSub(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B){
		ArrayList<ArrayList<Double>> response = new ArrayList<ArrayList<Double>>();
		for (int line=0; line<A.size();line++){
			ArrayList<Double> responseLine = new ArrayList<Double>();
			for (int column=0; column<A.get(0).size(); column++){
				responseLine.add(A.get(line).get(column) - B.get(line).get(column));
			}
			response.add(responseLine);
		}
		return response;
	}
	
	private ArrayList<ArrayList<Double>> matrixMul(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B){
		if(A.get(0).size() == B.size()){
			ArrayList<ArrayList<Double>> response = newMatrixZeros(A.size(), B.get(0).size());
			for (int i=0; i<A.size();i++){
				for (int j=0; j<B.get(0).size();j++){
					for (int k=0; k<A.get(0).size();k++){
						response.get(i).set(j, response.get(i).get(j) + A.get(i).get(k) * B.get(k).get(j));
					}
				}
			}
			return response;
		}else{ System.out.println("check matrix sizes (matrixMul)"); return A;}
	}

	private ArrayList<ArrayList<Double>> sigmoid(ArrayList<ArrayList<Double>> A){
		ArrayList<ArrayList<Double>> response = new ArrayList<ArrayList<Double>>();
		for (int nodeIndex = 0; nodeIndex<A.size();nodeIndex++){
			ArrayList<Double> valueList = new ArrayList<Double>();
			double value = 1.0 / (1.0 + Math.exp(-A.get(nodeIndex).get(0)));
			valueList.add(value);
			response.add(valueList);
		}
		return response;
	}
	
	private ArrayList<ArrayList<Double>> sigmoidDerivative(ArrayList<ArrayList<Double>> A){
		ArrayList<ArrayList<Double>> OnesList = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i<A.size();i++){
			ArrayList<Double> line = new ArrayList<Double>();
			for (int j=0; j<A.get(0).size();j++){
				line.add((double) 1);
			}
			OnesList.add(line);
		}
		return mul(sigmoid(A), sub(OnesList, sigmoid(A)));
	}
	
	
	
	public void gradientDescent(ArrayList<ArrayList<double[]>> data, int iteration, int batchSize, double learningRate, ArrayList<ArrayList<double[]>> testData){
		// change label representation [double] => [false,true,false,false....] with true = (double) 1 and false = (double) 0
		for (int dataIndex = 0; dataIndex<data.size(); dataIndex++){
			int label = (int) data.get(dataIndex).get(1)[0];
			double[] newArray = new double[10];
			newArray[label] = (double) 1;
			data.get(dataIndex).set(1,newArray);
		}
		for (int i = 0; i<iteration; i++){
			Collections.shuffle(data);
			ArrayList<ArrayList<ArrayList<double[]>>> batches = new ArrayList<ArrayList<ArrayList<double[]>>>(); // batches [ batch [ data [ [image, label]]]]
			for (int batchIndex = 0; batchIndex < data.size(); batchIndex+=batchSize){
				ArrayList<ArrayList<double[]>> batch = new ArrayList<ArrayList<double[]>>();
				for (int dataIndex = 0; dataIndex < data.size(); dataIndex++){
					batch.add(data.get(dataIndex));
				}
				batches.add(batch);
			}
			for (ArrayList<ArrayList<double[]>> batch : batches){
				updateBatch(batch, learningRate);
			}
			System.out.print("Completed iteration "+(i+1)+" with " );
			System.out.println(evaluate(testData) + "% success");
		}
	}
	
	private static ArrayList<ArrayList<Double>> newMatrixZeros(int m, int n){
		ArrayList<ArrayList<Double>> newMatrix = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i<m; i++){
			ArrayList<Double> line = new ArrayList<Double>();
			for (int j=0; j<n; j++){
				line.add((double) 0);
			}
			newMatrix.add(line);
		}
		return newMatrix;
	}
	
	private static class Deltas {
		public ArrayList<ArrayList<ArrayList<Double>>> deltaBiases;
		public ArrayList<ArrayList<ArrayList<Double>>> deltaWeights;
		
		Deltas(ArrayList<ArrayList<ArrayList<Double>>> biases, ArrayList<ArrayList<ArrayList<Double>>> weights){
			deltaBiases = biases;
			deltaWeights = weights;
		}
	}
	
	private void updateBatch(ArrayList<ArrayList<double[]>> batch, double learningRate){
		ArrayList<ArrayList<ArrayList<Double>>> newBiases = new ArrayList<ArrayList<ArrayList<Double>>>();
		ArrayList<ArrayList<ArrayList<Double>>> newWeights = new ArrayList<ArrayList<ArrayList<Double>>>();
		for (int layerIndex = 0; layerIndex < sizes.size()-1; layerIndex++){
			newBiases.add(newMatrixZeros(sizes.get(layerIndex+1), 1)); // sizes.get(layerIndex+1) = current layer size 
			newWeights.add(newMatrixZeros(sizes.get(layerIndex+1), sizes.get(layerIndex))); //sizes.get(layerIndex) = previous layer size
		}
		for (int batchIndex = 0; batchIndex < batch.size(); batchIndex++){
			double[] image = batch.get(batchIndex).get(0);
			double[] label = batch.get(batchIndex).get(1);
			Deltas deltas = backPropagation(image, label);
			ArrayList<ArrayList<ArrayList<Double>>> deltaBiases = deltas.deltaBiases;
			ArrayList<ArrayList<ArrayList<Double>>> deltaWeights = deltas.deltaWeights;

			for (int indexLayer = 0; indexLayer < sizes.size()-1; indexLayer++){
				for (int indexNode = 0; indexNode < sizes.get(indexLayer+1); indexNode++){
					//System.out.println("deltaBiases = "+ deltaBiases.get(indexLayer).get(indexNode).get(0));
					newBiases.get(indexLayer).get(indexNode).set(0, newBiases.get(indexLayer).get(indexNode).get(0) + deltaBiases.get(indexLayer).get(indexNode).get(0));
					//System.out.println(newBiases.get(indexLayer).get(indexNode).get(0));
					for (int indexWeight = 0; indexWeight<sizes.get(indexLayer); indexWeight++){
						newWeights.get(indexLayer).get(indexNode).set(indexWeight, newWeights.get(indexLayer).get(indexNode).get(indexWeight) + deltaWeights.get(indexLayer).get(indexNode).get(indexWeight));
					}
				}
			}
		}
		for (int indexLayer = 0; indexLayer < sizes.size()-1; indexLayer++){
			for (int indexNode = 0; indexNode < sizes.get(indexLayer+1); indexNode++){
				// weight = weight - (learningRate/len(batch)) * newWeight
				biases.get(indexLayer).get(indexNode).set(0, biases.get(indexLayer).get(indexNode).get(0) - ((learningRate / batch.size()) * newBiases.get(indexLayer).get(indexNode).get(0)));
				for (int indexWeight = 0; indexWeight<sizes.get(indexLayer); indexWeight++){
					weights.get(indexLayer).get(indexNode).set(indexWeight, weights.get(indexLayer).get(indexNode).get(indexWeight) - (learningRate / batch.size()) * newWeights.get(indexLayer).get(indexNode).get(indexWeight));
				}
			}
		}
	}
	
	private ArrayList<ArrayList<Double>> transpose(ArrayList<ArrayList<Double>> A){
		ArrayList<ArrayList<Double>> transposeMatrix = new ArrayList<ArrayList<Double>>();
		for (int j=0; j<A.get(0).size(); j++){
			ArrayList<Double> transposeLine = new ArrayList<Double>();
			for (int i=0; i<A.size();i++){
				transposeLine.add(A.get(i).get(j));
			}
			transposeMatrix.add(transposeLine);
		}
		return transposeMatrix;
	}
	
	private Deltas backPropagation(double[] image, double[] label){
		ArrayList<ArrayList<ArrayList<Double>>> newBiases = new ArrayList<ArrayList<ArrayList<Double>>>();
		ArrayList<ArrayList<ArrayList<Double>>> newWeights = new ArrayList<ArrayList<ArrayList<Double>>>();
		for (int layerIndex = 0; layerIndex < sizes.size()-1; layerIndex++){
			newBiases.add(newMatrixZeros(sizes.get(layerIndex+1), 1)); 
			newWeights.add(newMatrixZeros(sizes.get(layerIndex+1), sizes.get(layerIndex)));
		}
		
		ArrayList<ArrayList<Double>> output = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<ArrayList<Double>>> outputs = new ArrayList<ArrayList<ArrayList<Double>>>();
		// fill output with image
		for (int pixelIndex = 0; pixelIndex < image.length; pixelIndex++){
			ArrayList<Double> elem = new ArrayList<Double>();
			elem.add(image[pixelIndex]);
			output.add(elem);
		}
		outputs.add(output);
		
		// x represents the output of a perceptron = w*input + b
		// output of our neural net: sigmoid = 1 / (1 + exp(-x))
		ArrayList<ArrayList<ArrayList<Double>>> xList = new ArrayList<ArrayList<ArrayList<Double>>>();
		ArrayList<ArrayList<Double>> x;
		for (int layerIndex = 0; layerIndex <sizes.size()-1; layerIndex++){ // layerIndex starts at second layer
			x = matrixAdd(matrixMul(weights.get(layerIndex), output),biases.get(layerIndex));			//printArrayList(x);
			xList.add(x);
			output = sigmoid(x);
			outputs.add(output);
		}
		// and transform double[] label into ArrayList<ArrayList<Double>>
		ArrayList<ArrayList<Double>> newLabel = new ArrayList<ArrayList<Double>>();
		for (int index = 0; index<label.length; index++){
			ArrayList<Double> labelElem = new ArrayList<Double>();
			labelElem.add(label[index]);
			newLabel.add(labelElem);
		}
		ArrayList<ArrayList<Double>> delta = mul(sub(outputs.get(outputs.size()-1), newLabel), sigmoidDerivative(xList.get(xList.size()-1)));
		newBiases.set(newBiases.size()-1, delta);
		newWeights.set(newWeights.size()-1, matrixMul(delta, transpose(outputs.get(outputs.size()-2))));
		for (int layerIndex = 2; layerIndex<sizes.size();layerIndex++){
			x = xList.get(xList.size()-layerIndex);
			delta = mul(matrixMul(transpose(weights.get(weights.size()-layerIndex+1)), delta), sigmoidDerivative(x));
			newBiases.set(newBiases.size()-layerIndex, delta);
			newWeights.set(newWeights.size()-layerIndex, matrixMul(delta, transpose(outputs.get(outputs.size()-layerIndex-1))));
		}
		Deltas deltas = new Deltas(newBiases, newWeights);
		return deltas;
	}
	
	static void printArrayList(ArrayList<ArrayList<Double>> A){
		String text = String.format("matrix of size %1$d x %2$d", A.size(), A.get(0).size());
		System.out.println(text);
		for(int i =0; i < A.size(); i++){
			System.out.println(A.get(i));
		}
	}
	static void printArrayList3D(ArrayList<ArrayList<ArrayList<Double>>> A){
		for(int i =0; i < A.size(); i++){
			System.out.print("\nLAYER");
			System.out.println(i);
			printArrayList(A.get(i));
		}
	}

	private ArrayList<ArrayList<Double>> feedForward(ArrayList<ArrayList<Double>> input){
		// input is (n, 1)
		for (int layerIndex = 0; layerIndex <sizes.size()-1; layerIndex++){ // layerIndex starts at second layer
			input = sigmoid(add(matrixMul(weights.get(layerIndex),input),biases.get(layerIndex)));
		}
		return input;
	}
	
	public double evaluate(ArrayList<ArrayList<double[]>> testData){
		double numberOfTests = (double) testData.size();
		double numberOfSuccess = 0.0;
		for (ArrayList<double[]> test : testData){
			ArrayList<ArrayList<Double>> input = new ArrayList<ArrayList<Double>>();
			for (int pixelIndex = 0; pixelIndex < test.get(0).length; pixelIndex++){
				ArrayList<Double> elem = new ArrayList<Double>();
				elem.add(test.get(0)[pixelIndex]);
				input.add(elem);
			}
			if (guess(input) == (int) test.get(1)[0]){numberOfSuccess++;}
		}
		return (numberOfSuccess/numberOfTests)*100;
	}
	
	public int guess(ArrayList<ArrayList<Double>> input){
		ArrayList<ArrayList<Double>> output = feedForward(input);
		int maxIndex = 0;
		double max = (double) 0;
		for(int index = 0; index<output.size();index++){
			if (output.get(index).get(0) > max) {
				maxIndex = index;
				max = output.get(index).get(0);
			}
		}
		return maxIndex;
	}
	
	public void _save() throws FileNotFoundException{
		/* reminder:
		 *	   biases = [layer[node[bias[1]]]
		 *	   weights = [layer[node[weights[#nodes last layer]]]
		 */
		// save biases
		/* format: 
		 * 	   #layers in file
		 *	   #biases node0bias node1bias node2bias .... \n (one layer per line)
		 */
		PrintWriter fileStream = new PrintWriter("data/biases.save");
		fileStream.println(sizes.size()-1);
		for (int layerIndex = 0; layerIndex < sizes.size()-1; layerIndex++){
			String line = "" + sizes.get(layerIndex+1) + " ";
			for (int nodeIndex = 0; nodeIndex < sizes.get(layerIndex+1); nodeIndex++){
				line += biases.get(layerIndex).get(nodeIndex).get(0) + " ";
			}
			fileStream.println(line);
		}
		fileStream.close();
		
		// save weights
		/* format:
		 * 	   #layers in file
		 * 	   MxN \n	(first layer matrix sizes)
		 * 	   matrixLine1 \n
		 * 	   matrixLine2 \n
		 * 	   .
		 * 	   .
		 * 	   .
		 * 	   MxN \n 	(second layer matrix sizes)
		 * 	   etc...
		 */
		fileStream = new PrintWriter("data/weights.save");
		fileStream.println(sizes.size()-1);
		for (int layerIndex = 0; layerIndex < sizes.size()-1; layerIndex++){
			String matrix = ""+weights.get(layerIndex).size()+" x "+weights.get(layerIndex).get(0).size()+"\n";
			for (int nodeIndex = 0; nodeIndex < sizes.get(layerIndex+1); nodeIndex++){
				for (int weightIndex = 0; weightIndex<weights.get(layerIndex).get(nodeIndex).size(); weightIndex++){
					matrix += weights.get(layerIndex).get(nodeIndex).get(weightIndex) + " ";
				}
				matrix += "\n";
			}
			fileStream.println(matrix);
		}
		fileStream.close();
	}
	
	public void _import() throws FileNotFoundException{
		// see save formats in _save()
		// import biases
		Scanner scanner = new Scanner(new File("data/biases.save"));
		scanner.useLocale(Locale.US);
		int numberOfLayers = scanner.nextInt();
		for (int layerIndex = 0; layerIndex <numberOfLayers; layerIndex++){
			int numberOfBiases = scanner.nextInt();
			for (int i = 0; i<numberOfBiases; i++){
				biases.get(layerIndex).get(i).set(0, scanner.nextDouble());
			}
		}
		scanner.close();
		System.out.println("biases imported");
		
		// import weights
		scanner = new Scanner(new File("data/weights.save"));
		scanner.useLocale(Locale.US);
		numberOfLayers = scanner.nextInt();
		for (int layerIndex = 0; layerIndex <numberOfLayers; layerIndex++){
			int numberOfLines = scanner.nextInt();
			scanner.next(); // skip x (from M x N)
			int numberOfColumns = scanner.nextInt();
			for (int nodeIndex = 0; nodeIndex < numberOfLines; nodeIndex++){
				for (int weightIndex = 0; weightIndex < numberOfColumns; weightIndex++){
					weights.get(layerIndex).get(nodeIndex).set(weightIndex, scanner.nextDouble());
				}
			}
		}
		scanner.close();
		System.out.println("weights imported");
	}
	
}
