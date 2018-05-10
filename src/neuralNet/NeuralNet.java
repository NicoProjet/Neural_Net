package neuralNet;

import java.util.*;


class NeuralNet {
	
	int nLayers;
	ArrayList<Integer> size;
	ArrayList<ArrayList<ArrayList<Double>>> biases;
	ArrayList<ArrayList<ArrayList<Double>>> weights;


	public NeuralNet(ArrayList<Integer> givenSize){
		
		size = new ArrayList<Integer>();
		biases = new ArrayList<ArrayList<ArrayList<Double>>>();
		weights = new ArrayList<ArrayList<ArrayList<Double>>>();
		
		nLayers = givenSize.size();
		size = givenSize;
		
		for (int i=1; i<nLayers; i++){
			biases.add(randn(i, 1));
		}
		
		
		ArrayList<ArrayList<Integer>> zipped= new ArrayList<ArrayList<Integer>>();
		//zipped = zip(zipped.subList(1,zipped.size()),zipped.subList(0,zipped.size()-1));
		for (int i=1; i<nLayers; i++){
			biases.add(randn(i, 1));
		}
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
}
