/**
 * @author mayers
 *
 */
import java.awt.Color;

import java.util.ArrayList ;

public class IsoLine {
	//
	private ArrayList<Double> xdata     = new ArrayList();
	private ArrayList<Double> ydata     = new ArrayList();
	private Color             color     = Color.GRAY ;
	private double            lineWidth = 1.0 ;
	// Center =0 // negative value left // positive value right // value = pixel offset
	private int               labelPosition = 0 ;
	private String			  label		= "IsoLine";
	
	public void add(double x, double y){
		this.xdata.add(x);
		this.ydata.add(y);
	}
	
	public void setLabel(String label){
		this.label = label ;
	}
	public String getLabel(){
		return this.label ;
	}	
	public void setLabelPosition(int labelpos ){
		this.labelPosition = labelpos ;
	}
	public int getLabelPosition(){
		return this.labelPosition ;
	}
	public void setColor(Color c){
		this.color = c;
	}
	public Color getColor(){
		return this.color;
	}
	
	public void setLineWidth(double lw){
		this.lineWidth = lw;
	}	
	public double getLineWidth(){
		return this.lineWidth ;
	}

	public double[] getXdata(){
		double[] array = new double[this.xdata.size()];
		for (int i = 0; i < this.xdata.size(); i++){
			array[i] = this.xdata.get(i);
		}
		return array ;
	}
	
	public double[] getYdata(){
		double[] array = new double[this.ydata.size()];
		for (int i = 0; i < this.ydata.size(); i++){
			array[i] = this.ydata.get(i);
		}
		return array ;
	}
}
