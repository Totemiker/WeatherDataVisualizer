package gui.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.google.common.collect.Lists;

import javafx.scene.chart.Axis;
import javafx.util.Pair;

/**
 * Implementation of a LocalDateAxis instead of Number Axis
 * @author Tobias
 *
 */
public class LocalDateAxis extends Axis<LocalDate> {
	
	private LocalDate lowerBound, upperBound;
	
	//FIXME Klasse korrekt programmieren
	
	public LocalDateAxis() {
		lowerBound = LocalDate.now();
		upperBound = LocalDate.now();
	}
	
	@Override
	public double getZeroPosition() {
		//only graphical manipulatioin	
		return 0;
	}
	
	//macht irgendwas mit Pixeln
	@Override
	public double getDisplayPosition(LocalDate value)
	{
		if(isValueOnAxis(value))
		{
			return ChronoUnit.DAYS.between(lowerBound, value);//Maybe wrong
		}
		else
			return Double.NaN;
	}
	//Macht irgendwas mit Pixeln
	@Override
	public LocalDate getValueForDisplay(double displayPosition) 
	{
		double fractional = displayPosition/getWidth();
		double days = ChronoUnit.DAYS.between(lowerBound, upperBound);
		return lowerBound.plus((long)(fractional*days), ChronoUnit.DAYS);
	}
	
	//??????????????????????????
	@Override
	public boolean isValueOnAxis(LocalDate value) {									
		return (value.isAfter(lowerBound) && value.isBefore(upperBound)) || value.isEqual(lowerBound) || value.isEqual(upperBound);
	}
	//???????????????????????????
	@Override
	public double toNumericValue(LocalDate value) {
		return ChronoUnit.DAYS.between(lowerBound,value);
	}
	//??????????????????????????
	@Override
	public LocalDate toRealValue(double value) {
		return lowerBound.plus((long) value, ChronoUnit.DAYS);
	}

	@Override
	protected List<LocalDate> calculateTickValues(double length, Object range) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTickMarkLabel(LocalDate value) {
		return value.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).localizedBy(Locale.GERMANY));
	}
	
	@Override
	protected Object getRange() {
		
		return new Pair<LocalDate,LocalDate>(lowerBound, upperBound);
	}
	
	@Override
	protected Object autoRange(double length) {
		return new Pair<LocalDate, LocalDate>(lowerBound, upperBound);
	}

	@Override
	protected void setRange(Object range, boolean animate) {
		lowerBound = ((Pair<LocalDate,LocalDate>)range).getKey();
		upperBound = ((Pair<LocalDate,LocalDate>)range).getValue();
		
	}
	
	@Override
	public void invalidateRange(List<LocalDate> data) {		
		super.invalidateRange(data);
		
		if(data.isEmpty())
		{
			//eigene bounds erzeugen
			lowerBound = upperBound = LocalDate.now();
		}else
		{
			//neue range calc
			List<LocalDate> sortedDates = Lists.newArrayList(data);
			Collections.sort(sortedDates);
			lowerBound = sortedDates.get(0);
			upperBound = sortedDates.get(sortedDates.size()-1);
			
		}
		
	}

}
