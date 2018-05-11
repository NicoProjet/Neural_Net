package neuralNet;

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

	public ArrayList<Integer> subArrayList(ArrayList<Integer> list, int lower, int upper){

		ArrayList<Integer> subList = new ArrayList<Integer>();

		for (int i=lower; i<upper; i++){
			subList.add(list.get(i));
		}

		return subList;
	}

	public ArrayList<ArrayList<Double>> randn(int nList, int nRandom){

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

	public ArrayList<ArrayList<Integer>> zip(ArrayList<Integer> list1, ArrayList<Integer> list2){

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
		ArrayList<ArrayList<Double>> response = new ArrayList<ArrayList<Double>>();
		for (int nodeIndex = 0; nodeIndex<A.size();nodeIndex++){
			ArrayList<Double> valueList = new ArrayList<Double>();
			double value = 0;
			for (int weightIndex = 0; weightIndex<A.get(0).size(); weightIndex++){
				value += A.get(nodeIndex).get(weightIndex) * B.get(weightIndex).get(0);
			}
			valueList.add(value);
			response.add(valueList);
		}
		return response;
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
	
	
	
	public void gradientDescent(ArrayList<ArrayList<double[]>> data, int iteration, int batchSize, double learningRate){
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
			System.out.println("Completed iteration "+i );
		}
	}
	
	private ArrayList<ArrayList<Double>> newMatrixZeros(int m, int n){
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
			System.out.println(deltaBiases);
			System.out.println(deltaBiases.size());
			System.out.println(deltaBiases.get(0).size());
			System.out.println(deltaBiases.get(0).get(0).size());
			System.out.println(deltaWeights);
			System.out.println(deltaWeights.size());
			System.out.println(deltaWeights.get(0).size());
			System.out.println(deltaWeights.get(0).get(0).size());
			System.out.println(sizes.size()-1);
			System.out.println(sizes.get(0+1));
			System.out.println(sizes.get(0));
			for (int indexLayer = 0; indexLayer < sizes.size()-1; indexLayer++){
				for (int indexNode = 0; indexNode < sizes.get(indexLayer+1); indexNode++){
					newBiases.get(indexLayer).get(indexNode).set(0, newBiases.get(indexLayer).get(indexNode).get(0) + deltaBiases.get(indexLayer).get(indexNode).get(0));
					for (int indexWeight = 0; indexWeight<sizes.get(indexLayer); indexWeight++){
						newWeights.get(indexLayer).get(indexNode).set(indexWeight, newWeights.get(indexLayer).get(indexNode).get(indexWeight) + deltaWeights.get(indexLayer).get(indexNode).get(indexWeight));
					}
				}
			}
		}
		for (int indexLayer = 0; indexLayer < sizes.size()-1; indexLayer++){
			for (int indexNode = 0; indexNode < sizes.get(indexLayer+1); indexNode++){
				// weight = weight - (learningRate/len(batch)) * newWeight
				biases.get(indexLayer).get(indexNode).set(0, biases.get(indexLayer).get(indexNode).get(0) - (learningRate / batch.size()) * newBiases.get(indexLayer).get(indexNode).get(0));
				for (int indexWeight = 0; indexWeight<sizes.get(indexLayer); indexWeight++){
					weights.get(indexLayer).get(indexNode).set(indexWeight, weights.get(indexLayer).get(indexNode).get(indexWeight) - (learningRate / batch.size()) * newWeights.get(indexLayer).get(indexNode).get(indexWeight));
				}
			}
		}
	}
	
	static ArrayList<ArrayList<Double>> transpose(ArrayList<ArrayList<Double>> A){
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
			x = add(mul(weights.get(layerIndex), output),biases.get(layerIndex));
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
		System.out.println("LABEL");
		System.out.println(newLabel);
		System.out.println(newLabel.size());
		System.out.println(newLabel.get(0).size());
		
		System.out.println("LIFE IS GOOD");
		/*
		 * CHECK THE MUL
		 */
		
		
		ArrayList<ArrayList<Double>> delta = mul(sub(outputs.get(outputs.size()-1), newLabel), sigmoidDerivative(xList.get(xList.size()-1)));
		
		newBiases.set(newBiases.size()-1, delta);
		newWeights.set(newWeights.size()-1, mul(delta, transpose(outputs.get(outputs.size()-2))));
		
		// Check mul ...
		for (int layerIndex = 2; layerIndex<sizes.size();layerIndex++){
			x = xList.get(xList.size()-layerIndex);
			delta = mul(mul(delta, transpose(outputs.get(outputs.size()-layerIndex+1))), sigmoidDerivative(x));
			newBiases.set(newBiases.size()-layerIndex, delta);
			newWeights.set(newWeights.size()-layerIndex, mul(delta, transpose(outputs.get(outputs.size()-layerIndex-1))));
		}
		
		Deltas deltas = new Deltas(newBiases, newWeights);
		return deltas;
	}
	
	
	
	
	
	

	public ArrayList<ArrayList<Double>> feedForward(ArrayList<ArrayList<Double>> input){
		// input is (n, 1)
		for (int layerIndex = 0; layerIndex <sizes.size()-1; layerIndex++){ // layerIndex starts at second layer
			input = sigmoid(add(mul(weights.get(layerIndex),input),biases.get(layerIndex)));
		}
		return input;
	}
	
	public int guess(ArrayList<ArrayList<Double>> input){
		ArrayList<ArrayList<Double>> output = feedForward(input);
		int maxIndex = 0;
		double max = (double) 0;
		for(int index = 0; index<output.size();index++){
			if (output.get(index).get(0) > max) {maxIndex = index;}
		}
		return maxIndex;
	}
	
}
