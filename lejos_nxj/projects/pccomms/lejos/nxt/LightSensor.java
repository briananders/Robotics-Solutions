package lejos.nxt;

import lejos.robotics.Colors;
import lejos.robotics.LampLightDetector;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * This class is used to obtain readings from a LEGO NXT light sensor.
 * The light sensor can be calibrated to low and high values. 
 * 
 */
public class LightSensor implements LampLightDetector, SensorConstants
{
	ADSensorPort port;
	private int _zero = 1023;
	private int _hundred = 0;
	private boolean floodlight = false;
	
	/**
	 * Create a light sensor object attached to the specified port.
	 * The sensor will be set to floodlit mode, i.e. the LED will be turned on.
	 * @param port port, e.g. Port.S1
	 */
	public LightSensor(ADSensorPort port)
	{
		this.port = port;
		port.setTypeAndMode(TYPE_LIGHT_ACTIVE,
                            MODE_PCTFULLSCALE);
	}
	
	/**
	 * Create a light sensor object attached to the specified port,
	 * and sets floodlighting on or off.
	 * @param port port, e.g. Port.S1
	 * @param floodlight true to set floodit mode, false for ambient light.
	 */
	public LightSensor(ADSensorPort port, boolean floodlight)
	{
	   this.port = port;
	   this.floodlight = floodlight;
       port.setTypeAndMode(
    		   (floodlight ? TYPE_LIGHT_ACTIVE
    				       : TYPE_LIGHT_INACTIVE),
    		   MODE_PCTFULLSCALE); 
	}
	
	public void setFloodlight(boolean floodlight)
	{	
		port.setType(floodlight ? TYPE_LIGHT_ACTIVE
		                         : TYPE_LIGHT_INACTIVE);
		this.floodlight = floodlight;
	}
	
	public boolean setFloodlight(Colors.Color color) {
		if(color == Colors.Color.RED) {
			port.setType(TYPE_LIGHT_ACTIVE);
			this.floodlight = true;
			return true;
		} else if (color == Colors.Color.NONE) {
			port.setType(TYPE_LIGHT_INACTIVE);
			this.floodlight = false;
			return true;
		} else return false;
	}

	public int getLightValue()
	{ 
		if(_hundred == _zero) return 0;
		return 100*(port.readRawValue() - _zero)/(_hundred - _zero); 
	}
	
	/**
	 * Get the light reading
	 * 
	 * @return the light level
	 */
	public int readValue() {
		return getLightValue();
	}
	
	/**
	 * 
	 * Get the normalized light reading
	 * 
	 * @return the raw light level
	 */
	public int readNormalizedValue() {
		return getNormalizedLightValue();
	}
	
	/* TODO: Options:
	 * getLightLevel()
	 * readLightLevel()
	 * getIntensity()
	 * readIntensity()
	 * getBrightness()
	 * readBrightness()
	 * getLight()
	 */
	
	/**
	 * Get the normalized light reading
	 * 
	 * @return normalized raw value (0 to 1023) LEGO NXT light sensor values typically range from 
	 * 145 (dark) to 890 (sunlight).
	 */
	public int getNormalizedLightValue() {
		return 1023 - port.readRawValue();
	}

/**
 * call this method when the light sensor is reading the low value - used by readValue
 **/
	public void calibrateLow()
	{
		_zero = port.readRawValue();
	}
/** 
 *call this method when the light sensor is reading the high value - used by readValue
 */	
	public void calibrateHigh()
	{
		_hundred = port.readRawValue();
	}
	/** 
	 * set the normalized value corresponding to readValue() = 0%
	 * @param low the low value
	 */
	public void setLow(int low) { _zero = 1023 - low;}
	  /** 
     * set the normalized value corresponding to  readValue() = 100%
     * @param high the high value
     */
    public void setHigh(int high) { _hundred = 1023 - high;}
    /**
    * return  the normalized value corresponding to readValue() = 0%
    */
   public int getLow() { return 1023 - _zero;}
    /** 
    * return the normalized value corresponding to  readValue() = 100%
    */
   public int  getHigh() {return 1023 - _hundred;}

	public Colors.Color getFloodlight() {
		if(this.floodlight == true)
			return Colors.Color.RED;
		else
			return Colors.Color.NONE;
	}

	public boolean isFloodlightOn() {
		return this.floodlight;
	}
}
