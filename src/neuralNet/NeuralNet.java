package neuralNet;

import java.util.*;


class NeuralNet {
	
	int nLayers;
	ArrayList<Integer> size;
	ArrayList biases;
	ArrayList weights;


	public NeuralNet(ArrayList<Integer> givenSize){
		
		size = new ArrayList<Integer>();
		biases = new ArrayList();
		weights = new ArrayList();
		
		nLayers = givenSize.size();
		size = givenSize;
		
		for (int i=1; i<nLayers; i++){
			biases.add(randn(i, 1));
		}	
	}
	
	public ArrayList randn(int nList, int nRandom){
		
		Random x = new Random();
        double n;
		ArrayList list = new ArrayList();
		ArrayList listnRandom = new ArrayList();
		
		for (int i=0; i<nList; i++){
			for (int j=0; j<nRandom; j++){
				n = x.nextGaussian();
				System.out.print(n);
				listnRandom.add(n);
			}
			list.add(listnRandom);
			listnRandom = new ArrayList();
		}
		
		return list;
	}
}
