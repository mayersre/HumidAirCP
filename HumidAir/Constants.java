/**
 * @author mayers
 *
 */
public interface Constants {
			
			/* general Scientific constants */
	
			public static final double N_AVAGADRO = 6.0221419947e23;

			public static final double C_LIGHT = 2.99792458e8;

			public static final double R_GAS = 8.31447215;

			public static final double T_ABS = -273.15;
			public static final double K_0   =  273.15;
			
			public static final double G_FORCE = 9.81;
			
			/* gas constants for air, water and humid air
			 * as used in Lection on Psychrometrics
			 * Some of these are rough approximations for practical use
			 */
			
			public static final double R_AIR   = 287.05;
			//
			public static final double R_STEAM = 461.52;
			// R_AIR / R_Steam = 0.622
			public static final double R_AIR_STEAM = R_AIR / R_STEAM ;
			// Isobaric heat capacity of Air
			public static final double CP_AIR   = 1.0046;
			// Evaporation enthalpy of water at 273.16K
			public static final double R_0   = 2500.5;
			public static final double R_WATER_0   = R_0;
			// Isobaric heat capacity of Steam
			public static final double CP_STEAM   = 1.88;
			// Heat capacity of water
			public static final double CP_WATER   = 4.19;
			// Heat capacity of water
			public static final double CP_ICE   = 2.07;
			// Enthalpy required to freeze water to ice or to melt
			public static final double R_ICE   = 333.5;
			//Molar Mass of Air in gramm/mol
			public static final double MM_AIR   = 28.96;
			//Molar Mass of Water in gramm/mol
			public static final double MM_WATER   = 18.02;
			// Pressure of water at the triple point in bar
			public static final double PWTR   = 0.00611657;
			// Temperature of Water at the triple point
			public static final double TWTR   = 273.16;
			// Temperature of water at the critical point
			public static final double TWCRIT   = 647.096;
			// 
			

}
