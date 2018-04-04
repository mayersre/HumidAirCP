HumidAirCP
###########

Mollier h,x Chart using eclipse, java, ant with CoolProp plus other equations
to work with AC and humid air.

It uses Coolprop.PropsSI only for the temperature/pressure relation of water above 0°C.

There is an Antoine equation for the steam pressure of water over ice.

This project requires Java 8 jdk and at least ant to build.

You need to download the CoolProp Library for your Platform from sf :

https://sourceforge.net/projects/coolprop/files/CoolProp/nightly/Java/

Place the library where it is found in the path or library path ...

In Windows put coolprop.dll into the PATH

In Linux OSX ... set LD_LIBRARY_PATH to the directory containig libCoolProp.so

Coolprop is a very nice Software for Property Data, look here :

http://www.coolprop.org/index.html

The Example and 7z platform-independent sources from Coolprop 6.1.1 are included here.

Import the Project to your eclipse workspace from git and run build.xml

or

Go to the HumidAirCP directory and run "ant hxdiagram"

The Mollier Chart plots currently only Isothermals and Relative humidity lines, it
was intended as a basic example on how to do it for students. However, there is
progress on the isenthalp lines. 

Some Isenthalp lines are plotted, but this is not ready yet

Help would be great.
 
No warranty, provided as is.

