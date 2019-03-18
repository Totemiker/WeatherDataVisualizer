/*
 * Copyright (c) 2019 Tobias Teumert
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gui.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
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
	
	//TODO Implement all overriden methods
	
	public LocalDateAxis() {
		lowerBound = LocalDate.now();
		upperBound = LocalDate.now();
	}
	
	@Override
	public double getZeroPosition() {
		//only graphical manipulatioin	
		return 0;
	}
	
	//returns the absolute Pixel Position
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
	//get the Local Date for given Pixel position
	@Override
	public LocalDate getValueForDisplay(double displayPosition) 
	{
		double fractional = displayPosition/getWidth();
		double days = ChronoUnit.DAYS.between(lowerBound, upperBound);
		return lowerBound.plus((long)(fractional*days), ChronoUnit.DAYS);
	}
	
	//
	@Override
	public boolean isValueOnAxis(LocalDate value) {									
		return (value.isAfter(lowerBound) && value.isBefore(upperBound)) || value.isEqual(lowerBound) || value.isEqual(upperBound);
	}
	//
	@Override
	public double toNumericValue(LocalDate value) {
		return ChronoUnit.DAYS.between(lowerBound,value);
	}
	//
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

	@SuppressWarnings("unchecked")
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
			//create new bounds
			lowerBound = upperBound = LocalDate.now();
		}else
		{
			//calculate new range
			List<LocalDate> sortedDates = Lists.newArrayList(data);
			Collections.sort(sortedDates);
			lowerBound = sortedDates.get(0);
			upperBound = sortedDates.get(sortedDates.size()-1);
			
		}
		
	}

}
