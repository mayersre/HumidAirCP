import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.text.DecimalFormatSymbols;
//
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.text.DecimalFormat;
//
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;

public class HXDiagram extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int width 					= 707;
    private int heigth             		= 1000;
    //
    private int top_padding 			= 40;
    private int bottom_padding 			= 40;
    private int right_padding 			= 10;
    private int left_padding 			= 50;
    //
    private Color frameColor 			= Color.BLACK;
    private Color gridColor 			= Color.GRAY;
    //
    // Y-Max/Min Values must be divideable by 5 in kJ/kg of dry air Values between -50 and +60 !
    private static  int YAxMin					= -20;
    private static  int YAxMax  				= 55;
    private static  int YAxMajorTickDistance 	= 5 ;
    private static  int numberYDivisions 		= (YAxMax-YAxMin)/YAxMajorTickDistance;
    //
    // X-Max/Min Values must be divideable by 5 in Gramms per kg between 0 and 60 !
    private static  int XAxMin					=  0  ;
    private static  int XAxMax   				=  25 ;
    private static  int XAxMajorTickDistance 	= 5 ;
    private static  int numberXDivisions 		= (XAxMax-XAxMin)/XAxMajorTickDistance;
    //
    private static  int hSteps					= 5;
    //
    private static double AirPressure = 101.325; // in kPa
    //
    // Partial pressure of steam 
    // We want this in hPa = mbar so multiply by 10 and we need XAxM* in kg so /1000
    private static  double papw_min = AirPressure*(double)(XAxMin/1000.0)/(Constants.R_AIR_STEAM+(double)(XAxMin/1000.0)) *10.0;
    private static  double papw_max = AirPressure*(double)(XAxMax/1000.0)/(Constants.R_AIR_STEAM+(double)(XAxMax/1000.0)) *10.0;
    //We need the humidity ratio in kg/kg as double
    private static double hr_min = (double)(XAxMin/1000.0);
    private static double hr_max = (double)(XAxMax/1000.0);
    //
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
	//
    int x_left      = 0;
    int x_right     = 0;
    int y_top       = 0;
    int y_bottom    = 0;
    int diaWidth    = 0;
    int diaHeight   = 0;
    //
    double YscaleSize   = 0;
    double XscaleSize   = 0;
    double X2scaleSize   = 0;
    //
    ArrayList<IsoLine> IsoLines     = new ArrayList();
    //
    int[] isenthalps = calculate_isenthalp_values(XAxMax,YAxMin,YAxMax,hSteps);
    //
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //
        setupAxes(g2,XAxMin,XAxMax,YAxMin,YAxMax,XAxMajorTickDistance,YAxMajorTickDistance);
        //
        //CalculateIsolines(IsoLines);
        //
        PlotIsolines(IsoLines,g2, (double)x_left,(double)x_right,(double)y_top,(double)y_bottom);
    }

    public static void CalculateIsolines(List<IsoLine> IsoLines){    
    //public static void CalculateIsolines(){ 
    	//
        for (int i = YAxMin; i < YAxMax ; i++) {
        	//System.out.println("IsoLine t : " + i);
        	IsoLines.add( calculate_Isothermal(i,AirPressure/100,hr_min,hr_max,(double)YAxMin,(double)YAxMax) );
        }
        //
        IsoLines.add( calculate_Isenthalp(50.0, AirPressure/100, XAxMin, XAxMax, YAxMin, YAxMax) );
        IsoLines.add( calculate_Isenthalp(0.0, AirPressure/100, XAxMin, XAxMax, YAxMin, YAxMax) );
        IsoLines.add( calculate_Isenthalp(-20.0, AirPressure/100, XAxMin, XAxMax, YAxMin, YAxMax) );
        IsoLines.add( calculate_Isenthalp(60.0, AirPressure/100, XAxMin, XAxMax, YAxMin, YAxMax) );
        //
        double[] rh_values = new double[] { 0.05, 0.1,0.15,0.2,0.25,0.3, 0.35, 0.4, 0.5,0.6,0.7,0.8,0.9,1.0 };
        for(double value : rh_values ) {
        	IsoLines.add( calculate_RH_line((double)value,hr_min,hr_max, AirPressure) );
        }
        //IsoLines.add( calculate_RH_line(1.0,hr_min,hr_max, AirPressure));

    }

    public static int[] calculate_isenthalp_values(double xmax, double ymin, double ymax, int steps) {
    	//
    	// calculate enthalpy top right
    	int h_top_right =  (int)Math.round(ymax + xmax/1000 * Constants.R_0);
    	// divide it by our enthalpy steps
    	int x =  h_top_right % steps;
    	h_top_right = h_top_right - x;
    	// top right value div steps
    	// now count number of lines
    	int anzahl= (int)((h_top_right - ymin) / steps)+1;
    	// create array of that size
    	int[] isenthalpen = new int[anzahl];
    	// fill array with appropriate values
    	for (int i = 0 ; i < anzahl; i++) {
    		if (i==0) {
    			isenthalpen[i]=(int)ymin;
    		} 
    		else {
    			isenthalpen[i]=(int)(ymin + steps * i);
    		}
    	}
    	//
    	System.out.println("Values for Isenthalps : " + Arrays.toString(isenthalpen) );
    	//
    	return isenthalpen;
    }
    
    public static void PlotIsolines(List<IsoLine> IsoLines,Graphics2D g2, double x_left, double x_right, double y_top, double y_bottom){ 
    	//
        for(IsoLine value : IsoLines ) {
        	plot_IsoLine(g2,value,hr_min,hr_max,(double)YAxMin,(double)YAxMax, x_left,x_right,y_top,y_bottom);
        }
    }
        
    public static void drawRotate(Graphics2D g, double x, double y, int angle, String text) 
    {    
        g.translate((float)x,(float)y);
        g.rotate(Math.toRadians(angle));
        
        FontMetrics metrics = g.getFontMetrics();
        int labelHeight = metrics.getHeight();
        int labelWidth = metrics.stringWidth(text);
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0-labelHeight, labelWidth, labelHeight);
        
        
        g.setColor(Color.BLACK);
        g.drawString(text,0,0);
        g.rotate(-Math.toRadians(angle));
        g.translate(-(float)x,-(float)y);
    }  

    public static double interpol_lin(double x1,double x2,double x3, double y1,double y3){
    	/**
    	/*  Returns an interpolated value for y2
    	/*  based on the correlation series x1 x2 x3
    	/*  and                             y1    y3
    	 */
    	return y3 + ((y1 - y3) / (x1 - x3)) * (x2 - x3);
    }
    
    public static IsoLine calculate_RH_line(double rh, double xmin, double xmax, double AtmosphericPressure){
    	/**
    	/*  Returns the IsoLine for relative humidity with calculated data
    	/*  input is relative humidity as a value between 0 and 1
    	 *  and the values of the humidity ratio min/max on the X-Axis as kg Water / kg dry Air
    	 */
    	//
    	IsoLine il = new IsoLine();
    	//
    	// Now create datarange from min to max
    	// Numpy I miss you :)
    	double firstxrange   = 0.001 ;
    	double xrange        = xmax - 0.001 ;
    	System.out.println("calculate_RH_line :: xmax = "+ rh);
    	int    steps    = 300;
    	int firststeps  = 300;
    	double firststepSize = (double)0.001/firststeps;
    	double stepSize = (double)xrange/steps;
    	
    	double[] xdata = new double[steps+firststeps];
    	double[] ydata = new double[steps+firststeps];
    	//
    	// first gramm of water needs many points for the curve to look nice
    	//
    	for (int i = 0; i < firststeps; i++) {
    		xdata[i] = (double)(i * firststepSize);
    		ydata[i] = Air.Enthalpy_p_phi_x(AtmosphericPressure/100.0,rh,xdata[i])-(double)(xdata[i]*Constants.R_0);
    		il.add(xdata[i], ydata[i]);
    		//System.out.println("Werte x/y = "+xdata[i]+" / "+ ydata[i]);
    	}    	
    	//
    	for (int i = firststeps+1; i < (steps+firststeps); i++) {
    		xdata[i] = (double)((firststepSize * firststeps) + (stepSize*(i-firststeps)));
    		ydata[i] = Air.Enthalpy_p_phi_x(AtmosphericPressure/100.0,rh,xdata[i])-(double)(xdata[i]*Constants.R_0);
    		il.add(xdata[i], ydata[i]);
    		//System.out.println("Werte x/y = "+xdata[i]+" / "+ ydata[i]);
    	}
    	String Label = new DecimalFormat(" #").format(rh *100.0);
    	//String Label = (rh *100.0) + "";
    	il.setLabel(Label +"% ");
    	//
    	return il;
    }

    public static IsoLine calculate_Isothermal(double temperature, double AtmosphericPressure, double xmin, double xmax, double ymin, double ymax){
    	/**
    	/*  Returns the IsoLine for temperature
    	/*  AtmosphericPressure in bar
    	 *  temperature in °C
    	 */
    	//
    	IsoLine il = new IsoLine();
    	//    	
    	double[] xdata = new double[2];
    	double[] ydata = new double[2];
    	// Left side, Humidity Ratio = 0
    	//
		xdata[0] = 0.0;
		ydata[0] = temperature * Constants.CP_AIR ;
		il.add(xdata[0], ydata[0]);
    	//(double)(i * stepSize)
		xdata[1] = Air.HumidityRatio_p_t_phi(AtmosphericPressure,temperature,1);
		if (xdata[1] > xmax ) {
			// we are right side outside, use right side limit
			xdata[1] = xmax ;
			ydata[1] = Air.Enthalpy_p_t_x(AtmosphericPressure,temperature,xmax)-(double)(xdata[1]*Constants.R_0);
		} else {
			ydata[1] = Air.Enthalpy_p_t_phi(AtmosphericPressure,temperature,1)-(double)(xdata[1]*Constants.R_0);
		}
		if (ydata[1] > ymax ) {
			// we are top outside, use top limit
			// now some algebra
			double slope = (ydata[1]-ydata[0])/(xdata[1]-xdata[0]);
			double point = ydata[1] -(slope * xdata[1]);
			//
			ydata[1] = ymax ;
			// for this y we need x
			xdata[1] = ( ydata[1] - point ) / slope ;
					
		} 
		
		il.add(xdata[1], ydata[1]);
		il.setColor(Color.RED);
		//
    	//String Label = new DecimalFormat(" #").format(rh *100.0);
    	//String Label = (rh *100.0) + "";
    	//il.setLabel(Label +"% ");
    	//
    	return il;
    }

    public static IsoLine calculate_Isenthalp(double enthalpy, double pressure, double xmin, double xmax, double ymin, double ymax){
    	/**
    	/*  Returns the IsoLine for enthalpy
    	/*  Method needs to be implemented and Parameters need to be changed
    	 *  
    	 */
    	//
    	IsoLine il = new IsoLine();
    	//   
    	// There are three types of isenthalps
    	// Type one starts on th y axis and ends on the 100% line
    	//
    	// Type two starts on the top X-Axis and ends on the 100% Line
    	//
    	// Type three starts on the top X-Axis and ends on the right y-axis
    	//
    	double[] xdata = new double[2];
    	double[] ydata = new double[2];
    	// Left side, Humidity Ratio = 0
    	//
		xdata[0] = 0.0;
		ydata[0] = enthalpy ;
		il.add(xdata[0], ydata[0]);
		//
		xdata[1] = Air.HumidityRatio_p_h_phi(pressure,enthalpy,1.05);
		//xdata[1] = 0.013;
		ydata[1] = enthalpy-(double)(xdata[1]*Constants.R_0);
		System.out.println(" xdata[1] : "+ xdata[1]);
		System.out.println(" ydata[1] : "+ ydata[1]);
		
		//ydata[1] = 18;
		//
		if (ydata[1] > ymax ) {
			// higher than Y max 
			// we are right side outside, 
			// use right side limit if x > xmax
			System.out.println(" higher than Y max ");
		} else {
			// Y < Ymax

		}
		il.add(xdata[1], ydata[1]);
		il.setColor(Color.BLUE);
		//
    	//String Label = new DecimalFormat(" #").format(rh *100.0);
    	//String Label = (rh *100.0) + "";
    	//il.setLabel(Label +"% ");
    	//
    	return il;
    }

    public static void plot_IsoLine(Graphics2D g,IsoLine iLine, double xmin, double xmax, double ymin, double ymax,
    								double x_left, double x_right, double y_top, double y_bottom){
    	/**
    	/*  plots the IsoLine onto our canvas
    	/*  input is an IsoLine
    	 *  
    	 */
    	double[] xdata = iLine.getXdata() ;
    	double[] ydata = iLine.getYdata() ;
    	Color plotcolor = iLine.getColor();
    	g.setColor(plotcolor);
    	//
    	int datalength = xdata.length;
    	double[] x_plot_data = new double[datalength];
    	double[] y_plot_data = new double[datalength];
    	//
    	boolean FirstYData = true ;
    	boolean LastYData  = true ;
    	//
    	int FirstYIndex=0;
    	int LastYIndex=0;
    	//
    	if (xdata.length == 2 ) {
			x_plot_data[0] = interpol_lin(xmin,xdata[0],xmax, x_left, x_right);
			y_plot_data[0] = interpol_lin(ymax,ydata[0],ymin, y_top, y_bottom);
			x_plot_data[1] = interpol_lin(xmin,xdata[1],xmax, x_left, x_right);
			y_plot_data[1] = interpol_lin(ymax,ydata[1],ymin, y_top, y_bottom);
			if (ydata[0] < ymax ) {
				g.draw(new Line2D.Double(x_plot_data[0], y_plot_data[0], x_plot_data[1], y_plot_data[1]));
			}
    	} else {
     	for (int i = 1; i < xdata.length; i++) {
    		//
    		if (ydata[i] > ymin && ydata[i-1] > ymin){
    			// fit data into our screen with linear interpolation
    			x_plot_data[i] = interpol_lin(xmin,xdata[i],xmax, x_left, x_right);
    			y_plot_data[i] = interpol_lin(ymax,ydata[i],ymin, y_top, y_bottom);
    			//System.out.println("x_plot_data : " + x_plot_data[i] + " y_plot_data : " + y_plot_data[i] );
    			//
    			if (x_plot_data[i-1] >= x_left ) {
    				// We will plot something and want a label position
					if (FirstYData){
						FirstYIndex = i ;
						FirstYData = false;
					}
    				if (y_plot_data[i-1] > y_top){
    					//

    					g.draw(new Line2D.Double(x_plot_data[i-1], y_plot_data[i-1], x_plot_data[i], y_plot_data[i]));
    				} else {
    					// outside we are
    					if (LastYData){
    						LastYIndex = i-1 ;
    						LastYData = false;
    					}
    				}
    				
    			}
    			
    		}
    		
    	}
     	if (LastYData){
     		LastYIndex = xdata.length ;
     	}
     	if (FirstYData){
     		System.out.println("This should never happen");
     		FirstYIndex=0;
     	}
    	// Label
    	int LabelPos = iLine.getLabelPosition();
    	//
    	String Label = iLine.getLabel();
    	//
    	if ( LabelPos == 0 ){
    		int LabelIndex = (int)(( LastYIndex + FirstYIndex )*0.7);
    		double dx = (x_plot_data[LabelIndex+1]-x_plot_data[LabelIndex-1]);
    		double dy = (y_plot_data[LabelIndex+1]-y_plot_data[LabelIndex-1]);
    		double rotation = Math.toDegrees( Math.atan2(dy, dx) );
            FontMetrics metrics = g.getFontMetrics();
            int labelHeight = metrics.getHeight();
            int labelWidth = metrics.stringWidth(Label);
    		//System.out.println("Rotation = " + (int)rotation);
    		drawRotate(g, (int)x_plot_data[LabelIndex]+labelWidth/2-2 ,  (int)y_plot_data[LabelIndex],(int)rotation,Label );
    		//g.drawString(Label,(int)x_plot_data[LabelIndex], (int)y_plot_data[LabelIndex] );
    	} else {
    		; // no strategy yet 
    	}
    	}		
    	
    }
    
    private void setupAxes(Graphics2D g, int xmin, int xmax, int ymin, int ymax, int tickX, int tickY ){
    	//
    	int tickLineWidth = 4;
    	//
        
        // create x and y axes 
    	// Calculate Diagram corners, height and width
    	//
        x_left      = left_padding;
        x_right     = getWidth() - left_padding ;
        y_top       = top_padding ;
        y_bottom    = getHeight() - bottom_padding ;
        diaWidth    = x_right-x_left;
        diaHeight   = getHeight() - top_padding - bottom_padding ;
        //
        YscaleSize   = ymax-ymin ;
        XscaleSize   = xmax-xmin ;
        X2scaleSize  = papw_max-papw_min ;
        //
        //System.out.println("XAxMax : " + XAxMax/1000 + " papw_max : " + papw_max );
        // draw white background
        g.setColor(Color.WHITE);
        g.fillRect(x_left, y_top, diaWidth, diaHeight);
        g.setColor(Color.BLACK);
        //
        g.drawLine(x_left, y_bottom, x_left, y_top);
        g.drawLine(x_right, y_bottom, x_right, y_top);
        //
        g.drawLine(x_left, y_bottom, x_right, y_bottom);
        g.drawLine(x_left, y_top, x_right, y_top);
        //
        // Y-Axis, create hatch marks.
        for (int i = 0; i <= YscaleSize; i++ ) {
        	// Calculate Y Position
            double dy	  = (double)(y_bottom - ( i * (diaHeight/YscaleSize)));
            int y         = (int)Math.floor(dy+0.5d);
            //
            if ( i%5 == 0 ){
            	//label und gridline + tick
                g.setColor(Color.BLACK);
                g.draw(new Line2D.Double(x_left-(tickLineWidth*2), dy, x_left, dy));
                g.setColor(Color.RED);
                String xLabel = (ymin + i) + "";
                FontMetrics metrics = g.getFontMetrics();
                int labelHeight = metrics.getHeight();
                int labelWidth = metrics.stringWidth(xLabel);
                // We fake add a temperature scale in Centigrade, truely it is the cp of Air
                g.drawString(xLabel, x_left - labelWidth - 4*tickLineWidth, y + labelHeight/2 -2 );
                
            } else {
                g.setColor(Color.BLACK);
                g.draw(new Line2D.Double(x_left-tickLineWidth, dy, x_left, dy));
            }
        }
        // X-Axis, create hatch marks and grid lines.
        for (int i = 0; i <= XscaleSize; i++ ) {
        	// Calculate X Position
            double dx	  = (double)(x_left + ( i * (diaWidth/XscaleSize)));
            int x         = (int)Math.floor(dx+0.5d);
            //
            if ( i%5 == 0 ){
            	//label und gridline + tick
                g.setColor(Color.CYAN.darker());
                g.draw(new Line2D.Double(dx, y_bottom, dx, y_bottom+(tickLineWidth*2)));
                String yLabel = (xmin + i) + "";
                FontMetrics metrics = g.getFontMetrics();
                int labelHeight = metrics.getHeight();
                int labelWidth = metrics.stringWidth(yLabel);
                drawRotate(g, x + labelHeight/2 -2 ,  y_bottom+4*tickLineWidth+labelWidth,-90,yLabel );
                if (i != 0 && i!= XscaleSize){
                	Stroke old_stroke=g.getStroke();
                	g.setStroke(new BasicStroke(1));
                	g.draw(new Line2D.Double(dx, y_bottom, dx, y_top));
                	g.setStroke(old_stroke); 	
                }

                
            } else {
                g.setColor(Color.CYAN.darker());
                g.draw(new Line2D.Double(dx, y_bottom, dx, y_bottom+tickLineWidth));
            	Stroke old_stroke=g.getStroke();
            	g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
            	g.draw(new Line2D.Double(dx, y_bottom, dx, y_top));
            	g.setStroke(old_stroke); 	
            }
        }
        // second X-Axis, Partial pressure of water in mbar create hatch marks.
        for (int i = 0; i <= X2scaleSize; i++ ) {
        	// Calculate X Position
            double dx2	  = (double)(x_left + ( i * (diaWidth/X2scaleSize)));
            int x2         = (int)Math.floor(dx2+0.5d);
            //
            if ( i%5 == 0 ){
            	//label und gridline + tick
                g.setColor(Color.DARK_GRAY);
                g.draw(new Line2D.Double(dx2, y_top, dx2, y_top-(tickLineWidth*2)));
                String x2Label = (xmin + i) + "";
                FontMetrics metrics = g.getFontMetrics();
                int labelHeight = metrics.getHeight();
                int labelWidth = metrics.stringWidth(x2Label);
                drawRotate(g, x2 + labelHeight/2 -2 ,  y_top-2*tickLineWidth-15+labelWidth,-90,x2Label );

            } else {
            	g.setColor(Color.DARK_GRAY);
                g.draw(new Line2D.Double(dx2, y_top, dx2, y_top-tickLineWidth));	
            }
        }
    }

    
    private static void createAndShowGui() {

        HXDiagram mainPanel 			= new HXDiagram();
        mainPanel.CalculateIsolines(mainPanel.IsoLines);
        mainPanel.setPreferredSize(new Dimension(600, 900));
        JFrame frame = new JFrame("h,x Diagramm");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      });
   }
}
