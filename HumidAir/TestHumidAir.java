/**
 * @author mayers
 *
 */
public class TestHumidAir {
	
	static {
	    //System.loadLibrary("CoolProp");
	}

	public static void main( String[ ] args ) {
		
		double t = Water.saturationTemperature(1.01325);
		System.out.println("1. Temperature at standard atmosphere :        (99.9) " + t + " °C");
		
		double p = Water.saturationPressure(100);
		System.out.println("2. Pressure at 100 °C :                        (1.01) " + p + " bar");
		
		double h = Air.Enthalpy_p_t_x(1.01325,20,0.01);
		System.out.println("3. Enthalpy at 1013.25 mBar 20 °C 10 Gramms : (45.47) " + h +" kJ/kg");
		
		double h2 = Air.Enthalpy_p_t_phi(1.01325,20,0.68);
		System.out.println("4. Enthalpy at 1013.25 mBar 20 °C 68% RH :    (45.28) " + h2 +" kJ/kg");
		
		double h3 = Air.Enthalpy_p_phi_x(1.01325,0.68,0.01);
		System.out.println("5. Enthalpy at 1013.25 mBar 20 °C 68% RH :    (45.6) " + h3 +" kJ/kg");
	}
	
}
