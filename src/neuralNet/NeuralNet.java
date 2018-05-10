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

		for (int i=1; i<nLayers; i++){
			biases.add(randn(i, 1));
		}

		int x,y;
		ArrayList<ArrayList<Integer>> zipped= new ArrayList<ArrayList<Integer>>();
		zipped = zip( subArrayList(sizes, 0, nLayers-1) ,  subArrayList(sizes, 1, nLayers));
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
				System.out.print(n);
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

	private ArrayList<ArrayList<Double>> mul(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B){
		ArrayList<ArrayList<Double>> response = new ArrayList<ArrayList<Double>>();
		for (int nodeIndex = 0; nodeIndex<A.size();nodeIndex++){
			ArrayList<Double> valueList = new ArrayList<Double>();
			double value = 0;
			 for (int weightIndex = 0; weightIndex<A.get(0).size(); weightIndex++){
				 value += A.get(nodeIndex).get(weightIndex) * B.get(nodeIndex).get(0);
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
	
	
	
	private void gradientDescent(ArrayList<ArrayList<int[]>> data, int iteration, int batchSize, double learningRate){
		for (int i = 0; i<iteration; i++){
			Collections.shuffle(data);
			ArrayList<ArrayList<ArrayList<int[]>>> batches = new ArrayList<ArrayList<ArrayList<int[]>>>();
			for (int batchIndex = 0; batchIndex < data.size(); batchIndex+=batchSize){
				ArrayList<ArrayList<int[]>> batch = new ArrayList<ArrayList<int[]>>();
				for (int dataIndex = 0; dataIndex < data.size(); dataIndex++){
					batch.add(data.get(dataIndex));
				}
				batches.add(batch);
			}
			for (ArrayList<ArrayList<int[]>> batch : batches){
				//updateBatch(batch, learningRate);
			}
			System.out.println("Completed iteration "+i );
		}
	}
	
	
	
	
	
	

	public ArrayList<ArrayList<Double>> feedForward(ArrayList<ArrayList<Double>> input){
		for (int layerIndex = 0; layerIndex <sizes.size()-1; layerIndex++){ // layerIndex starts at second layer
			input = sigmoid(add(mul(weights.get(layerIndex),input),biases.get(layerIndex)));
		}
		return input;
	}
	
}
