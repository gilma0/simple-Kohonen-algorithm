import java.io.IOException;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class kohonen {
	
	static double [][] neurons;
	double [][] weights;
	static double [][] inputs;
	static double [] distance;
	static double lr;
	
	
	//getting the closest neuron
	public static int Euclidean_distance(double x_input, double y_input) {
		for (int j = 0; j < distance.length; j++) {
			distance[j] = Math.sqrt(Math.pow(x_input - neurons[j][0],2) + Math.pow(y_input - neurons[j][1], 2));
		}
		int index = 0;
		double min_distance = Integer.MAX_VALUE;
		for (int i = 0; i < distance.length; i++) {
			if(min_distance>distance[i]) {
				min_distance = distance[i];
				index = i;
			}
		}
		return index;
	}
	
	public static void train(int iterations, double dist){
		int iter = 1;
		while(iter < iterations + 1) {
			for (int i = 0; i < inputs.length; i++) {
				int smallest = Euclidean_distance(inputs[i][0], inputs[i][1]);
				for (int j = 0; j < neurons.length; j++) {
					if(distance(neurons[smallest], neurons[j]) < dist) { //with A 0.6-0.8 is a good number for neighborhood
						neurons[j][0] += distance[smallest] * lr * (inputs[i][0] - neurons[smallest][0]);
						neurons[j][1] += distance[smallest] * lr * (inputs[i][1] - neurons[smallest][1]);
					}
				}
				
			}
			iter++;
		}
	}
	
    public static int test(double[] pattern){
        double[] distances = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            distances[i] = distance(neurons[i], pattern);
        }
        return smallest(distances);
    }
    
    public static int smallest(double[] distances){
        int output = -1;
        double minor = Double.MAX_VALUE;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] < minor) {
                minor = distances[i];
                output = i;
            }
        }
        return output;
    }
    
    public static double distance(double[] w, double[] point){
        double sum = 0.0;
        for (int i = 0; i < w.length; i++) {
            sum += Math.pow(w[i] - point[i], 2);
        }
        return Math.sqrt(sum);
    }
    
    public static void random_inputs_neurons_for_E_A() {
    	neurons = new double [30][2];
		inputs = new double [10][2];
		for (int i = 0; i < neurons.length; i++) {
			neurons[i][0] = Math.random()*4 - 2;
			//neurons[i][1] = Math.random()*2;
			neurons[i][1] = 0;
		}
		for (int i = 0; i < inputs.length; i++) {
			double angle = Math.random()*Math.PI*2;
			inputs[i][0] = Math.cos(angle) * 4 + Math.random() * Math.random() * (8/9) - Math.random() * 2;
			inputs[i][1] = Math.sin(angle) * 4 + Math.random() * Math.random() * (8/9);
		}
		distance = new double[30];
    }
    
    public static void random_inputs_neurons_for_B_A() {
    	neurons = new double [30][2];
		inputs = new double [5][2];
		for (int i = 0; i < neurons.length; i++) {
			neurons[i][0] = Math.random()*4;
			neurons[i][1] = 2;
		}
		for (int i = 0; i < inputs.length; i++) {
			double angle = Math.random()*Math.PI*2;
			inputs[i][0] = Math.cos(angle) * 2 + 2*Math.random();
			inputs[i][1] = Math.sin(angle) * 2 + 2*Math.random();
		}
		distance = new double[30];
    }
    public static void random_inputs_neurons_for_A() {
    	neurons = new double [30][2];
		inputs = new double [10][2];
		for (int i = 0; i < neurons.length; i++) {
			neurons[i][0] = Math.random()*4;
			neurons[i][1] = 2;
		}
		for (int i = 0; i < inputs.length; i++) {
			double angle = Math.random()*Math.PI*2;
			inputs[i][0] = Math.cos(angle) * Math.random() + Math.random()*2 + 1;
			inputs[i][1] = Math.sin(angle) * Math.random() + Math.random()*2 + 1;
		}
		distance = new double[30];
    }
	
    public static void A(int iterations, double dist) {
		lr = 0.01;
		train(iterations, dist);
		double[] x_neurons = new double[30];
		double[] y_neurons = new double[30];
		for (int i = 0; i < y_neurons.length; i++) {
			x_neurons[i] = neurons[i][0];
			y_neurons[i] = neurons[i][1];
		}
		double[] x_inputs = new double[10];
		double[] y_inputs = new double[10];
		for (int i = 0; i < x_inputs.length; i++) {
			x_inputs[i] = inputs[i][0];
			y_inputs[i] = inputs[i][1];
		}
		XYChart chart = new XYChartBuilder().width(600).height(500).title("Gaussian Blobs").xAxisTitle("X").yAxisTitle("Y").build();

		// Customize Chart
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
		chart.getStyler().setChartTitleVisible(false);
		chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
		chart.getStyler().setMarkerSize(16);

		// Series
		chart.addSeries("neurons", x_neurons, y_neurons);
		XYSeries series = chart.addSeries("inputs", x_inputs, y_inputs);
		double[] aba = {0};
		chart.addSeries("iterations: " + iterations, aba);
		series.setMarker(SeriesMarkers.DIAMOND);

		new SwingWrapper(chart).displayChart();
    }
    
    public static void random_inputs_neurons_for_B() {
    	neurons = new double [30][2];
		inputs = new double [5][2];
		for (int i = 0; i < neurons.length; i++) {
			double angle = Math.random()*Math.PI*2;
			neurons[i][0] = Math.cos(angle) + 2;
			neurons[i][1] = Math.sin(angle) + 2;
		}
		for (int i = 0; i < inputs.length; i++) {
			double angle = Math.random()*Math.PI*2;
			inputs[i][0] = Math.cos(angle) * Math.random() + Math.random()*2 + 1;
			inputs[i][1] = Math.sin(angle) * Math.random() + Math.random()*2 + 1;
		}
		distance = new double[30];
    }
    
    public static void random_inputs_neurons_for_B_B() {
    	neurons = new double [30][2];
		inputs = new double [10][2];
		for (int i = 0; i < neurons.length; i++) {
			double angle = Math.random()*Math.PI*2;
			neurons[i][0] = Math.cos(angle) + 2;
			neurons[i][1] = Math.sin(angle) + 2;
		}
		for (int i = 0; i < inputs.length; i++) {
			double angle = Math.random()*Math.PI*2;
			inputs[i][0] = Math.cos(angle) * Math.random() + Math.random()*2 + 1;
			inputs[i][1] = Math.sin(angle) * Math.random() + Math.random()*2 + 1;
		}
		distance = new double[30];
    }
    
    public static void random_inputs_neurons_for_E_B() {
    	neurons = new double [30][2];
		inputs = new double [10][2];
		for (int i = 0; i < neurons.length; i++) {
			double angle = Math.random()*Math.PI*2;
			neurons[i][0] = Math.cos(angle) ;
			neurons[i][1] = Math.sin(angle) ;
		}
		for (int i = 0; i < inputs.length; i++) {
			double angle = Math.random()*Math.PI*2;
			inputs[i][0] = Math.cos(angle) * 4 + Math.random() * Math.random() * (8/9) - Math.random() * 2;
			inputs[i][1] = Math.sin(angle) * 4 + Math.random() * Math.random() * (8/9);
		}
		distance = new double[30];
    }
    
    
    public static void B(int iterations, double dist) {
		lr = 0.01;
		train(iterations, dist);
		double[] x_neurons = new double[30];
		double[] y_neurons = new double[30];
		for (int i = 0; i < y_neurons.length; i++) {
			x_neurons[i] = neurons[i][0];
			y_neurons[i] = neurons[i][1];
		}
		double[] x_inputs = new double[10];
		double[] y_inputs = new double[10];
		for (int i = 0; i < x_inputs.length; i++) {
			x_inputs[i] = inputs[i][0];
			y_inputs[i] = inputs[i][1];
		}
		XYChart chart = new XYChartBuilder().width(600).height(500).title("Gaussian Blobs").xAxisTitle("X").yAxisTitle("Y").build();

		// Customize Chart
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
		chart.getStyler().setChartTitleVisible(false);
		chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
		chart.getStyler().setMarkerSize(16);

		// Series
		chart.addSeries("neurons", x_neurons, y_neurons);
		XYSeries series = chart.addSeries("inputs", x_inputs, y_inputs);
		double[] aba = {0};
		chart.addSeries("iterations: " + iterations, aba);
		series.setMarker(SeriesMarkers.DIAMOND);

		new SwingWrapper(chart).displayChart();
    }
    
    
    
    public static void C(int iterations, double dist) {
		lr = 0.01;
		train(iterations, dist);
		double[] x_neurons = new double[25];
		double[] y_neurons = new double[25];
		for (int i = 0; i < y_neurons.length; i++) {
			x_neurons[i] = neurons[i][0];
			y_neurons[i] = neurons[i][1];
		}
		double[] x_inputs = new double[5];
		double[] y_inputs = new double[5];
		for (int i = 0; i < x_inputs.length; i++) {
			x_inputs[i] = inputs[i][0];
			y_inputs[i] = inputs[i][1];
		}
		XYChart chart = new XYChartBuilder().width(600).height(500).title("Gaussian Blobs").xAxisTitle("X").yAxisTitle("Y").build();

		// Customize Chart
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
		chart.getStyler().setChartTitleVisible(false);
		chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
		chart.getStyler().setMarkerSize(16);

		// Series

		chart.addSeries("neurons", x_neurons, y_neurons);
		XYSeries series = chart.addSeries("inputs", x_inputs, y_inputs);
		double[] aba = {0};
		chart.addSeries("iterations: " + iterations, aba);
		series.setMarker(SeriesMarkers.DIAMOND);

		new SwingWrapper(chart).displayChart();
    
}
    public static void random_inputs_neurons_for_C() {
    	neurons = new double [25][2];
		inputs = new double [5][2];
		double floor = 2.75;
		double X = 1.5;
		for (int i = 0; i < neurons.length; i++) {
			if(i % 5 == 0) {
				floor -= 0.25;
			}
			neurons[i][1] = floor;
			neurons[i][0] = X;
			X+=0.25;
			if(X == 2.75) {
				X = 1.5;
			}
		}
		for (int i = 0; i < inputs.length; i++) {
			double angle = Math.random()*Math.PI*2;
			inputs[i][0] = Math.cos(angle) * Math.random() + Math.random()*2 + 1;
			inputs[i][1] = Math.sin(angle) * Math.random() + Math.random()*2 + 1;
		}
		distance = new double[25];
    }
    
    public static void random_inputs_neurons_for_B_C() {
    	neurons = new double [25][2];
		inputs = new double [5][2];
		double floor = 2.75;
		double X = 1.5;
		for (int i = 0; i < neurons.length; i++) {
			if(i % 5 == 0) {
				floor -= 0.25;
			}
			neurons[i][1] = floor;
			neurons[i][0] = X;
			X+=0.25;
			if(X == 2.75) {
				X = 1.5;
			}
		}
		for (int i = 0; i < inputs.length; i++) {
			double angle = Math.random()*Math.PI*2;
			inputs[i][0] = Math.cos(angle) * Math.random() + Math.random()*2 + 1;
			inputs[i][1] = Math.sin(angle) * Math.random() + Math.random()*2 + 1;
		}
		distance = new double[25];
    }
	
	public static void main(String[] args) throws IOException {
		//A
		/*random_inputs_neurons_for_A();
		A(0, 1);
		A(1000, 1);
		A(100000, 1);
		A(1000000, 1);*/
		//B
		/*random_inputs_neurons_for_B();
		B(0, 1);
		B(1000, 1);
		B(100000, 1);
		B(1000000, 1);*/
		//C
		/*random_inputs_neurons_for_C();
		C(0, 0.7);
		C(1000, 0.7);
		C(100000, 0.7);
		C(1000000, 0.7);*/
		//D_A
		random_inputs_neurons_for_E_B();
		B(0, 0.9);
		B(1000, 0.9);
		B(100000, 0.9);
		B(1000000, 0.9);
	}

}
