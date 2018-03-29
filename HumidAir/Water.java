/**
 * @author mayers
 *
 */
import java.lang.*;

//import static Constants.Constants.* ;

public class Water {

	static {
        System.loadLibrary("CoolProp");
        System.out.println("CoolProp version:" + " " + CoolProp.get_global_param_string("version"));
        
    }
	
    static public double saturationPressure (double temperature) {
        /**
    	*
    	* returns the saturation pressure of water in bar
        * input Temperature in degrees celsius
        *
        * -50 ...0  °C Antoine Equation from Baehr/Kabelac / Thermodynamik
        * Above Coolprop
        */
        double T = temperature + Constants.K_0;
        //
        double pressure = 0;
        //
        if (temperature < 0.01 )
        	{
        	// using equation from lections on psychrometrics
        	//
            pressure = Constants.PWTR * Math.exp(22.5129 *  ( 1 - Constants.TWTR / T ));
        	}
        else {
        	// using CoolProp
        	//
            pressure = CoolProp.PropsSI("P", "T", T, "Q", 0, "Water")/100000 ;
        	}


		return pressure;
    } // saturationPressure


    static public double saturationTemperature (double pressure) {
        /**
    	*
	    * Returns Degrees Celsius
	    * Calculates the saturation Temperature of water from -50
	    * using various models for two different areas
	    * 
        * -50 ...0  °C Antoine Equation from Baehr/Kabelac / Thermodynamik
        * Above Coolprop Water
	    *
	    */
	    double Temperature = -300 ;
	    double p_bar = pressure ;
	    double p_pa = pressure * 100000 ;
	    //
	    if ( p_bar < Constants.PWTR )
	    	{
	        //
	        // -50 bis Tripelpunkt
	        //
	    	Temperature = ( ( -22.5129 * Constants.TWTR )  /  ( Math.log(p_bar / Constants.PWTR ) - 22.5129 ) )  - Constants.K_0;
	        //
	    	}
	    else {
	        //
	    	Temperature = CoolProp.PropsSI("T", "P", p_pa, "Q", 0, "Water") - Constants.K_0 ;
	    	}
	    return Temperature ;
    }
}



